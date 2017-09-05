package com.agilesolutions.runescape.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@NoArgsConstructor
@Entity
@Table(name="CATEGORY")
public class Category extends BaseModel {
    @Setter @Getter
    private String name;
    @Setter @Getter
    private String description;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference
    @Setter @Getter private Set<PlayerCategory> playersScores;

    public Category(String name, String description) {
        super();
        this.setName(name);
        this.setDescription(description);
    }

//    public void addPlayer(Player player, Integer level, Integer score) {
//        PlayerCategory playerCategory = new PlayerCategory(level, score);
//        playerCategory.setPlayer(player);
//        playerCategory.setCategory(this);
//        this.getPlayers().add(playerCategory);
//        System.out.println(level + " -> " + score);
//        player.addCategory(playerCategory);
//    }
}
