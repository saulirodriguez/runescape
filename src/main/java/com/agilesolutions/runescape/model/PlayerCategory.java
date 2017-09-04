package com.agilesolutions.runescape.model;

import lombok.*;

import javax.persistence.*;


@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="PLAYER_CATEGORY")
public class PlayerCategory {
    @EmbeddedId
    private PlayerCategoryPK id;

    @ManyToOne
    @MapsId("playerId")
    @JoinColumn(name = "PLAYER_ID")
    private Player player;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @NonNull
    private Integer level;
    @NonNull
    private Integer score;

//    public PlayerCategory(Integer level, Integer score) {
//        this.setLevel(level);
//        this.setScore(score);
//    }
}
