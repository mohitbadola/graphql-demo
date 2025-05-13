package com.jason.graphql_demo.controller;

import com.jason.graphql_demo.model.Player;
import com.jason.graphql_demo.model.Team;
import com.jason.graphql_demo.service.PlayerService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @QueryMapping
    private List<Player> findAll(){
        return playerService.findAll();
    }

    @QueryMapping
    private Optional<Player> findOne(@Argument Integer id){
        return playerService.findOne(id);
    }

    @MutationMapping
    private Player create(@Argument String name, @Argument Team team){
        return playerService.create(name, team);
    }

    @MutationMapping
    private Player update(@Argument Integer id, @Argument String name, @Argument Team team){
        return playerService.update(id, name, team);
    }

    @MutationMapping
    private Player delete(@Argument Integer id){
        return playerService.delete(id);
    }

}
