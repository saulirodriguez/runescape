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
    @Autowired
    private PlayerRepository playerRepository;

    public List<Player> findAll(Optional<String> optionalName) {
        List<Player> todoList = new ArrayList<>();

        this.playerRepository
                .findAll()
                .forEach(todoList::add);

        if(optionalName.isPresent()) {
            String name = optionalName.get().toLowerCase();
            todoList = todoList.stream()
                    .filter(player -> player.getName().toLowerCase().contains(name))
                    .collect(Collectors.toList());
        }

        return todoList;
    }

    public Player findOne(Long id) throws ResourceNotFoundException{
        Player player = this.playerRepository.findOne(id);
        if(player == null) {
            throw new ResourceNotFoundException("Player", id);
        }

        return player;
    }

    public Player save(Player player) {
        this.playerRepository.save(player);
        return player;
    }

    public Player update(Long id, Player player) throws ResourceNotFoundException {
        Player t = this.playerRepository.findOne(id);

        if(t == null) {
            throw new ResourceNotFoundException("Player", id);
        }

        if(!player.getName().isEmpty()) {
            t.setName(player.getName());
        }

        this.playerRepository.save(t);

        return t;
    }

    public void delete(Long id) {
        this.playerRepository.delete(id);
    }
}
