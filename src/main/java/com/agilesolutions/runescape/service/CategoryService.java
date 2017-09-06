package com.agilesolutions.runescape.service;

import com.agilesolutions.runescape.exception.ResourceNotFoundException;
import com.agilesolutions.runescape.model.Category;
import com.agilesolutions.runescape.model.Player;
import com.agilesolutions.runescape.model.PlayerCategory;
import com.agilesolutions.runescape.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll(Optional<String> optionalName, Optional<String> optionalDescription) {
        List<Category> todoList = new ArrayList<>();

        this.categoryRepository
                .findAll()
                .forEach(todoList::add);

        if(optionalName.isPresent()) {
            String name = optionalName.get().toLowerCase();
            todoList = todoList.stream()
                    .filter(category -> category.getName().toLowerCase().contains(name))
                    .collect(Collectors.toList());
        }

        if(optionalDescription.isPresent()) {
            String description = optionalDescription.get().toLowerCase();
            todoList = todoList.stream()
                    .filter(category -> category.getDescription().toLowerCase().contains(description))
                    .collect(Collectors.toList());
        }

        return todoList;
    }

    public Category findOne(String id) throws ResourceNotFoundException {
        Category category = this.categoryRepository.findOne(id);
        if(category == null) {
            throw new ResourceNotFoundException("Category: " + id);
        }

        return category;
    }

    public Category save(Category category) {
        if(category.getDescription() == null) {
            category.setDescription("");
        }

        this.categoryRepository.save(category);
        return category;
    }

    public Category update(String id, Category category) throws ResourceNotFoundException {
        Category t = this.categoryRepository.findOne(id);

        if(t == null) {
            throw new ResourceNotFoundException("Category", id);
        }

        if(!category.getName().isEmpty()) {
            t.setName(category.getName());
        }

        if(!category.getDescription().isEmpty()) {
            t.setDescription(category.getDescription());
        }

        this.categoryRepository.save(t);
        return t;
    }

    public void delete(String id) {
        this.categoryRepository.delete(id);
    }

    public Category savePlayerScore(String categoryId, Player player, Integer level, Integer score)
            throws ResourceNotFoundException {

        PlayerCategory playerCategory;
        Category category = this.findOne(categoryId);
        Optional<PlayerCategory> playerCategoryOptional = player.getPlayerCategories()
                .stream().filter(c -> c.getCategory().equals(category)).findFirst();

        if(playerCategoryOptional.isPresent()) {
            playerCategory = playerCategoryOptional.get();
            playerCategory.setLevel(level);
            playerCategory.setScore(score);
        } else {
            playerCategory = new PlayerCategory(level, score);
            playerCategory.setPlayer(player);
            playerCategory.setCategory(category);
            player.getPlayerCategories().add(playerCategory);
        }

        this.categoryRepository.save(category);

        return category;
    }

    public List<LinkedHashMap> findTop(String id) {
        List<LinkedHashMap> result = new ArrayList<>();
        if(id.equals("overall")) {
            findTopOverall(result);
        } else {
            List<PlayerCategory> playerCategories = this.categoryRepository.findTop10Players(id);

            playerCategories.stream().forEach(pc -> {
                LinkedHashMap map = new LinkedHashMap();
                map.put("player", pc.getPlayer().getName());
                map.put("level", pc.getLevel());
                map.put("score", pc.getScore());
                result.add(map);
            });
        }

        return result;
    }

    private void findTopOverall(List<LinkedHashMap> result) {
        List<Object[]> playerCategories = this.categoryRepository.findTop10PlayersOverall();

        playerCategories.stream().forEach(pc -> {
            LinkedHashMap map = new LinkedHashMap();
            map.put("player", pc[1]);
            map.put("level", pc[2]);
            map.put("score", pc[3]);
            result.add(map);
        });
    }
}
