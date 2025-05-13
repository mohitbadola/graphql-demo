package com.jason.graphql_demo.service;

import com.jason.graphql_demo.model.Player;
import com.jason.graphql_demo.model.Team;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PlayerService {

    List<Player> players = new ArrayList<>();
    AtomicInteger id = new AtomicInteger(0);

    public List<Player> findAll(){
        return players;
    }

    public Optional<Player> findOne(Integer id){
        return players.stream().filter(player -> player.id() == id).findFirst();
    }

    public Player create(String name, Team team){
        Player player = new Player(id.incrementAndGet(), name, team);
        players.add(player);
        return player;
    }

    public Player update(Integer id, String name, Team team){
        Player updatedPlayer = new Player(id, name, team);
        Optional<Player> optional = players.stream().filter(p->p.id() == id).findFirst();
        if(optional.isPresent()){
            Player player = optional.get();
            int index = players.indexOf(player);
            players.set(index, updatedPlayer);
        }
        else {
            throw new IllegalArgumentException("Invalid Player ");
        }
        return updatedPlayer;
    }

    public Player delete(Integer id){
        Player player = players.stream().filter(p->p.id() == id).findFirst()
                .orElseThrow(IllegalArgumentException::new);
        players.remove(player);
        return player;
    }

    @PostConstruct
    private void init() {
        players.add(new Player(id.incrementAndGet(), "MS Dhoni", Team.CSK));
        players.add(new Player(id.incrementAndGet(), "Virat Kohli", Team.RCB));
        players.add(new Player(id.incrementAndGet(), "Shreyas Iyer", Team.KKR));
        players.add(new Player(id.incrementAndGet(), "Sanju Samson", Team.RR));
        players.add(new Player(id.incrementAndGet(), "KL Rahul", Team.LSG));
        players.add(new Player(id.incrementAndGet(), "Rishabh Pant", Team.DC));
        players.add(new Player(id.incrementAndGet(), "Hardik Pandya", Team.GT));
        players.add(new Player(id.incrementAndGet(), "Ruturaj Gaikwad", Team.CSK));
        players.add(new Player(id.incrementAndGet(), "Faf du Plessis", Team.RCB));
        players.add(new Player(id.incrementAndGet(), "Andre Russell", Team.KKR));
    }

}
