package com.agilesolutions.runescape.service;

import com.agilesolutions.runescape.model.Category;
import com.agilesolutions.runescape.model.Player;
import com.agilesolutions.runescape.repository.CategoryRepository;
import com.agilesolutions.runescape.repository.PlayerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class Seeder implements CommandLineRunner {
    private PlayerRepository playerRepository;
    private CategoryRepository categoryRepository;
    private LoggerManager logger = LoggerManager.getInstance();

    public Seeder(PlayerRepository playerRepository, CategoryRepository categoryRepository) {
        this.playerRepository = playerRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String ...args) {
        logger.log("Running Seeder...");
        String[] categoryNames = { "Overall", "Attack", "Defense", "Magic", "Cooking", "Crafting"};
        List<Category> categories = new ArrayList<>();
        Arrays.stream(categoryNames)
                .forEach((categoryName) -> categories.add(new Category(categoryName, categoryName + " Scores")));

        List<Player> players = new ArrayList<>();
        for(Integer i = 1; i <21; i++) {
            players.add(new Player("Player", i.toString()));
        }

        this.categoryRepository.save(categories);
        this.playerRepository.save(players);
    }
}
