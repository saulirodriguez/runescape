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
@Table(name = "PLAYER")
public class Player {
    @Id
    @GeneratedValue
    private Long id;
    private String firstname;
    private String lastname;

    @OneToMany(mappedBy = "category")
    private Set<PlayerCategory> categories = new HashSet<>();

    public void addCategory(PlayerCategory pc) {
        this.getCategories().add(pc);
    }
}
