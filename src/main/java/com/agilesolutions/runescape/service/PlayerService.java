package com.agilesolutions.runescape.service;

import com.agilesolutions.runescape.exception.ResourceNotFoundException;
import com.agilesolutions.runescape.model.Player;
import com.agilesolutions.runescape.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerService {
    private LoggerManager logger = LoggerManager.getInstance();
    @Autowired
    private PlayerRepository playerRepository;

    public List<Player> findAll(Optional<String> optionalFirstname, Optional<String> optionalLastname) {
        List<Player> todoList = new ArrayList<>();

        this.playerRepository
                .findAll()
                .forEach(todoList::add);

        if(optionalFirstname.isPresent()) {
            String name = optionalFirstname.get().toLowerCase();
            todoList = todoList.stream()
                    .filter(player -> player.getFirstname().toLowerCase().contains(name))
                    .collect(Collectors.toList());
        }

        if(optionalLastname.isPresent()) {
            String description = optionalLastname.get().toLowerCase();
            todoList = todoList.stream()
                    .filter(player -> player.getLastname().toLowerCase().contains(description))
                    .collect(Collectors.toList());
        }

        return todoList;
    }

    public Player findOne(Long id) {
        return this.playerRepository.findOne(id);
    }

    public Player create(Player player) {
        if(player.getLastname() == null) {
            player.setLastname("");
        }

        this.playerRepository.save(player);
        return player;
    }

    public Player update(Long id, Player player) throws ResourceNotFoundException {
        Player t = this.playerRepository.findOne(id);

        if(t == null) {
            throw new ResourceNotFoundException("Player");
        }

        if(!player.getFirstname().isEmpty()) {
            t.setFirstname(player.getFirstname());
        }

        if(!player.getLastname().isEmpty()) {
            t.setLastname(player.getLastname());
        }

        this.playerRepository.save(t);
        return t;
    }

    public void delete(Long id) {
        this.playerRepository.delete(id);
    }
}
