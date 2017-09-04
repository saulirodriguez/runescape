package com.agilesolutions.runescape.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@Embeddable
public class PlayerCategoryPK implements Serializable {
    @Column(name = "PLAYER_ID")
    private Long playerId;

    @Column(name = "CATEGORY_ID")
    private Long categoryId;
}
