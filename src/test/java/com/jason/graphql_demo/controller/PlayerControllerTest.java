package com.jason.graphql_demo.controller;

import com.jason.graphql_demo.model.Player;
import com.jason.graphql_demo.model.Team;
import com.jason.graphql_demo.service.PlayerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;


@Import(PlayerService.class)
@GraphQlTest(PlayerController.class)
class PlayerControllerTest {

    @Autowired
    GraphQlTester tester;

    @Autowired
    PlayerService playerService;

    @Test
    void testFindAllPlayerShouldReturnAllPlayers() {
        String document = """
                query MyQuery {
                  findAll {
                    id
                    name
                    team
                  }
                }
                """;

        tester.document(document)
                .execute()
                .path("findAll")
                .entityList(Player.class)
                .hasSizeGreaterThan(9);
    }

    @Test
    void testValidIdShouldReturnPlayer() {
        String document = """
                query findOne($id: ID) {
                  findOne(id: $id) {
                    id
                    name
                    team
                  }
                }
                """;

        tester.document(document)
                .variable("id", 2)
                .execute()
                .path("findOne")
                .entity(Player.class)
                .satisfies(player -> {
                    Assertions.assertEquals("Virat Kohli", player.name());
                    Assertions.assertEquals(Team.RCB, player.team());
                });
    }

    @Test
    void testInvalidIdShouldReturnNull() {
        String document = """
                query findOne($id: ID) {
                  findOne(id: $id) {
                    id
                    name
                    team
                  }
                }
                """;

        tester.document(document)
                .variable("id", 100)
                .execute()
                .path("findOne")
                .valueIsNull();
    }

    @Test
    void testShouldCreateNewPlayer() {
        int currentCount = playerService.findAll().size();
        String document = """
                mutation create($name: String, $team: Team) {
                  create(name: $name, team: $team) {
                    id
                    name
                    team
                  }
                }
                """;

        tester.document(document)
                .variable("name", "Sam Curran")
                .variable("team", Team.CSK)
                .execute()
                .path("create")
                .entity(Player.class)
                .satisfies(player -> {
                    Assertions.assertEquals("Sam Curran", player.name());
                    Assertions.assertEquals(Team.CSK, player.team());
                    Assertions.assertEquals(currentCount + 1, playerService.findAll().size());
                });
    }

    @Test
    void testShouldUpdateExistingPlayer() {
        String document = """
                mutation update($id: ID, $name: String, $team: Team) {
                  update(id: $id, name: $name, team: $team) {
                    id
                    name
                    team
                  }
                }
                """;

        tester.document(document)
                .variable("id", 1)
                .variable("name", "Thala")
                .variable("team", Team.CSK)
                .execute()
                .path("update")
                .entity(Player.class);

        Player updatedPlayer = playerService.findOne(1).get();
        Assertions.assertEquals("Thala", updatedPlayer.name());
        Assertions.assertEquals(Team.CSK, updatedPlayer.team());

    }

    @Test
    void testShouldRemovePlayerWithValidId() {

        int currentCount = playerService.findAll().size();

        String document = """
                mutation delete($id: ID) {
                  delete(id: $id) {
                    id
                    name
                    team
                  }
                }
                """;
        tester.document(document)
                .variable("id", 1)
                .executeAndVerify();

        Assertions.assertEquals(currentCount - 1, playerService.findAll().size());
        Assertions.assertTrue(playerService.findOne(1).isEmpty());
    }

}