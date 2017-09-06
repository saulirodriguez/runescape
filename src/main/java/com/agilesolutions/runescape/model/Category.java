package com.agilesolutions.runescape.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@Entity
@Table(name="CATEGORY")
public class Category {
    @Id
    @Setter @Getter
    private String id;
    @Setter @Getter
    private String name;
    @Setter @Getter
    private String description;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference
    @Setter @Getter private Set<PlayerCategory> playersScores;

    public Category(String id, String name, String description) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Category)) {
            return false;
        }
        return ((Category) o).getId().equals(this.getId());
    }
}
