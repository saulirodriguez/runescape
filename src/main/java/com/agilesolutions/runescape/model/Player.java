package com.agilesolutions.runescape.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Entity
@Table(name = "PLAYER")
public class Player {
    @Id
    @GeneratedValue
    @Setter
    @Getter
    private Long id;

    @Setter @Getter
    private String name;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Setter @Getter
    private Set<PlayerCategory> playerCategories;

    public Player(String name) {
        super();
        this.setName(name);
        this.setPlayerCategories(new HashSet<>());
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Player)) {
            return false;
        }
        return ((Player) o).getId().equals(this.getId());
    }
}
