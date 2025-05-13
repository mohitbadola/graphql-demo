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

    @Test
    void testFindAllPlayerShouldReturnAllPlayers(){
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
    void testValidIdShouldReturnPlayer(){
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
                .variable("id", 1)
                .execute()
                .path("findOne")
                .entity(Player.class)
                .satisfies(player -> {
                    Assertions.assertEquals("MS Dhoni", player.name());
                    Assertions.assertEquals(Team.CSK, player.team());
                });
    }

    @Test
    void testInvalidIdShouldReturnNull(){
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
}