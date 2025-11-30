package com.gamecatalog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "operating_systems")
@Getter @Setter @NoArgsConstructor
public class OperatingSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "operatingSystems")
    private Set<Game> games = new HashSet<>();

    public OperatingSystem(String name) {
        this.name = name;
    }
}