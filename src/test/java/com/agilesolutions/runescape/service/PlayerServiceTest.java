package com.agilesolutions.runescape.service;

import com.agilesolutions.runescape.exception.ResourceNotFoundException;
import com.agilesolutions.runescape.model.Player;
import com.agilesolutions.runescape.repository.PlayerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.Java6Assertions.assertThat;


@RunWith(SpringRunner.class)
public class PlayerServiceTest {
    // Mock Data
    private List<Player> players = new ArrayList<>();
    private Player newPlayer = new Player("Player 6");


    @TestConfiguration
    static class PlayerServiceTestContextConfiguration {

        @Bean
        public PlayerService playerService() {
            return new PlayerService();
        }
    }

    @Autowired
    private PlayerService playerService;

    @MockBean
    private PlayerRepository playerRepository;

    @Before
    public void setUp() {
        for(int i = 1; i <6; i++) {
            Player p = new Player("Player " + i);
            p.setId((long) i);
            players.add(p);
        }

        newPlayer.setId((long) 6);

        Mockito.when(playerRepository.findAll())
                .thenReturn(players);

        Mockito.when(playerRepository.findOne(players.get(0).getId()))
                .thenReturn(players.get(0));

        Mockito.when(playerRepository.save(newPlayer))
                .thenReturn(newPlayer);
    }

    @Test
    public void findAllShouldGetAllPlayers() {
        List<Player> found = playerService.findAll(Optional.ofNullable(null));

        assertThat(found.size())
                .isEqualTo(5);
    }

    @Test
    public void findOneShouldGetSelectedPlayer() {
        Player found;
        try {
            found = playerService.findOne((long) 1);
        } catch (ResourceNotFoundException e) {
            found = null;
        }

        assertThat(found).isNotNull();
    }

    @Test
    public void saveShouldReturnPlayer() {
        Player found = playerService.save(newPlayer);

        assertThat(found).isEqualTo(newPlayer);
    }
}
