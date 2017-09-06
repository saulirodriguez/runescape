package com.agilesolutions.runescape.repository;

import com.agilesolutions.runescape.model.Category;
import com.agilesolutions.runescape.model.PlayerCategory;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {
    @Query("SELECT pc FROM PlayerCategory pc WHERE pc.category.id = :id ORDER BY pc.score DESC")
    public List<PlayerCategory> findPlayerCategories(@Param("id") String id, Pageable pageable);

    default List<PlayerCategory> findTop10Players(String id) {
        return findPlayerCategories(id, new PageRequest(0, 10));
    }

    default List<Object[]> findTop10PlayersOverall() {
        return findCategoriesOverall(new PageRequest(0, 10));
    }

    @Query("SELECT pc.player.id, pc.player.name, SUM(pc.level), SUM(pc.score) FROM PlayerCategory pc GROUP BY pc.player.id, pc.player.name ORDER BY SUM(pc.score) DESC")
    public List<Object[]> findCategoriesOverall(Pageable pageable);
}