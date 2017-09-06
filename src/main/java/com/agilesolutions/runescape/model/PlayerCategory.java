package com.agilesolutions.runescape.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import java.io.Serializable;
import java.util.LinkedHashMap;

@NoArgsConstructor
@Entity
@Table(name="PLAYER_CATEGORY")
public class PlayerCategory  implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "player_id")
    @JsonBackReference
    @Setter @Getter
    private Player player;

    @Id
    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    @Setter @Getter
    private Category category;

    @Setter @Getter private Integer level;
    @Setter @Getter private Integer score;

    public PlayerCategory(Integer level, Integer score) {
        this.setLevel(level);
        this.setScore(score);
    }

    public LinkedHashMap getPlayerInfo() {
        LinkedHashMap set = new LinkedHashMap();
        set.put("id", this.getPlayer().getId());
        set.put("name", this.getPlayer().getName());
        return set;
    }

    public LinkedHashMap getCategoryId() {
        LinkedHashMap set = new LinkedHashMap();
        set.put("id", this.getCategory().getId());
        set.put("name", this.getCategory().getName());
        return set;
    }
    @Override
    public String toString() {
        return this.getCategory().getId() + " " + this.getPlayer().getName();
    }
}
