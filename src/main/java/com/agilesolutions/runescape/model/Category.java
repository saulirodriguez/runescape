package com.agilesolutions.runescape.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="CATEGORY")
public class Category {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;

    @OneToMany(mappedBy = "player")
    private Set<PlayerCategory> players = new HashSet<>();

    public void addPlayer(Player player, PlayerCategory playerCategory) {
        playerCategory.setPlayer(player);
        playerCategory.setCategory(this);
//        PlayerCategory playerCategory = new PlayerCategory(player, this, level, score);
        this.getPlayers().add(playerCategory);
        player.addCategory(playerCategory);
    }
}
