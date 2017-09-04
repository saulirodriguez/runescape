package com.agilesolutions.runescape.service;

import com.agilesolutions.runescape.exception.ResourceNotFoundException;
import com.agilesolutions.runescape.model.Category;
import com.agilesolutions.runescape.model.Player;
import com.agilesolutions.runescape.model.PlayerCategory;
import com.agilesolutions.runescape.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private LoggerManager logger = LoggerManager.getInstance();
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PlayerService playerService;

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

    public Category findOne(Long id) {
        return this.categoryRepository.findOne(id);
    }

    public Category create(Category category) {
        if(category.getDescription() == null) {
            category.setDescription("");
        }

        this.categoryRepository.save(category);
        return category;
    }

    public Category update(Long id, Category category) throws ResourceNotFoundException {
        Category t = this.categoryRepository.findOne(id);

        if(t == null) {
            throw new ResourceNotFoundException("Category");
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

    public void delete(Long id) {
        this.categoryRepository.delete(id);
    }

    public Category addPlayer(Long categoryId, Long playerId, PlayerCategory playerCategory) {
        Category cat = this.findOne(categoryId);
        Player player = this.playerService.findOne(playerId);
        logger.log(player);
        logger.log(cat);
        cat.addPlayer(player, playerCategory);
        return cat;
    }
}
