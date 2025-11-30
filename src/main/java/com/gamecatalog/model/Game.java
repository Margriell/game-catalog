package com.gamecatalog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "games")
@Getter @Setter @NoArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long steamAppId;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String shortDescription;

    private Boolean isFree;
    private Float price;
    private String currency;
    private String priceStatus;
    private String controllerSupport;

    private Short releaseYear;
    private Byte requiredAge;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "platform_id")
    private Platform platform;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "game_os",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "os_id"))
    private Set<OperatingSystem> operatingSystems = new HashSet<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GameSales> salesData = new HashSet<>();

    public void addSales(GameSales sales) {
        salesData.add(sales);
        sales.setGame(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return steamAppId != null && steamAppId.equals(game.steamAppId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(steamAppId);
    }
}