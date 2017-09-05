package com.agilesolutions.runescape.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//@Data
@NoArgsConstructor
@Entity
@Table(name = "PLAYER")
public class Player extends BaseModel {
    @Setter @Getter private String firstname;
    @Setter @Getter private String lastname;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Setter @Getter private Set<PlayerCategory> playerCategories;

    public Player(String firstname, String lastname) {
        super();
        this.setFirstname(firstname);
        this.setLastname(lastname);
        this.setPlayerCategories(new HashSet<>());
    }
//    public void addCategory(PlayerCategory pc) {
//        this.getCategories().add(pc);
//    }
}
