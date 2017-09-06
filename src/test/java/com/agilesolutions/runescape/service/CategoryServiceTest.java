package com.agilesolutions.runescape.service;

import com.agilesolutions.runescape.exception.ResourceNotFoundException;
import com.agilesolutions.runescape.model.Category;
import com.agilesolutions.runescape.model.PlayerCategory;
import com.agilesolutions.runescape.repository.CategoryRepository;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Java6Assertions.assertThat;


@RunWith(SpringRunner.class)
public class CategoryServiceTest {
    // Mock Data
    private String[] categoryNames = { "Attack", "Defense", "Magic", "Cooking", "Crafting"};
    private List<Category> categories = new ArrayList<>();
    private Category newCategory = new Category("magic_resist", "Magic Resist", "Magic Resit Scores");
    private List<PlayerCategory> playerCategories = new ArrayList<>();
    private List<Object[]> playerCategoriesOverall = Arrays.asList(
            new Object[4],
            new Object[4],
            new Object[4]
    );

    @TestConfiguration
    static class CategoryServiceTestContextConfiguration {

        @Bean
        public CategoryService categoryService() {
            return new CategoryService();
        }
    }

    @Autowired
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    @Before
    public void setUp() {
        categories.forEach(category -> {
            PlayerCategory p1 = new PlayerCategory(10, 100);
            p1.setCategory(category);
            playerCategories.add(p1);
            PlayerCategory p2 = new PlayerCategory(10, 120);
            p2.setCategory(category);
            playerCategories.add(p2);
        });

        Arrays.stream(categoryNames)
                .forEach((c) -> categories.add(new Category(c.toLowerCase(), c, c + " Scores")));

        Mockito.when(categoryRepository.findAll())
                .thenReturn(categories);

        Mockito.when(categoryRepository.findOne(categoryNames[0].toLowerCase()))
                .thenReturn(categories.get(0));

        Mockito.when(categoryRepository.save(newCategory))
                .thenReturn(newCategory);

        Mockito.when(categoryRepository.findTop10Players("attack"))
                .thenReturn(playerCategories);

        Mockito.when(categoryRepository.findTop10PlayersOverall())
                .thenReturn(playerCategoriesOverall);
    }

    @Test
    public void findAllShouldGetAllCategories() {
        List<Category> found = categoryService.findAll(Optional.ofNullable(null), Optional.ofNullable(null));

        assertThat(found.size())
                .isEqualTo(5);
    }

    @Test
    public void findOneShouldGetSelectedCategory() {
        Category found;
        try {
            found = categoryService.findOne("attack");
        } catch (ResourceNotFoundException e) {
            found = null;
        }

        assertThat(found).isNotNull();
    }

    @Test
    public void saveShouldReturnCategory() {
        Category found = categoryService.save(newCategory);

        assertThat(found).isEqualTo(newCategory);
    }

    @Test
    public void findTop10ShouldReturnAttackCategories() {
        List<LinkedHashMap> found = categoryService.findTop("attack");

        assertThat(found.size()).isEqualTo(2);
    }

    @Test
    public void findTopOverallShouldReturnOverall() {
        List<LinkedHashMap> found = categoryService.findTop("overall");

        assertThat(found.size()).isEqualTo(3);
    }
}
