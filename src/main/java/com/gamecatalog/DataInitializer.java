package com.gamecatalog;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamecatalog.dto.importdata.GameImportDTO;
import com.gamecatalog.model.*;
import com.gamecatalog.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final GameRepository gameRepository;
    private final GenreRepository genreRepository;
    private final PublisherRepository publisherRepository;
    private final PlatformRepository platformRepository;
    private final RegionRepository regionRepository;
    private final OperatingSystemRepository osRepository;
    private final ObjectMapper objectMapper;

    // Cache w pamięci, żeby nie pytać bazy tysiąc razy o to samo
    private final Map<String, Genre> genreCache = new HashMap<>();
    private final Map<String, Publisher> publisherCache = new HashMap<>();
    private final Map<String, Platform> platformCache = new HashMap<>();
    private final Map<String, OperatingSystem> osCache = new HashMap<>();
    private final Map<String, Region> regionCache = new HashMap<>();

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (gameRepository.count() > 0) {
            return;
        }

        System.out.println("START IMPORTU DANYCH");

        // Inicjalizacja słowników
        initRegions();
        initOS();

        // Wczytanie pliku
        try (InputStream inputStream = getClass().getResourceAsStream("/steam_games_database.json")) {
            List<GameImportDTO> dtos = objectMapper.readValue(inputStream, new TypeReference<>() {});

            for (GameImportDTO dto : dtos) {
                if (dto.getSteamInfo() == null || dto.getSalesInfo() == null) continue;

                Game game = new Game();

                // Mapowanie proste
                game.setSteamAppId(dto.getSteamInfo().getAppId());
                game.setName(dto.getSteamInfo().getName());
                game.setShortDescription(dto.getSteamInfo().getShortDescription());
                game.setIsFree(dto.getSteamInfo().getIsFree());
                game.setPrice(dto.getSteamInfo().getPrice() != null ? dto.getSteamInfo().getPrice().floatValue() : 0f);
                game.setCurrency(dto.getSteamInfo().getCurrency());
                game.setPriceStatus(dto.getSteamInfo().getPriceStatus());
                game.setControllerSupport(dto.getSteamInfo().getControllerSupport());

                // Rzutowanie na zoptymalizowane typy
                if (dto.getSalesInfo().getYear() != null && !dto.getSalesInfo().getYear().equals("N/A")) {
                    try {
                        game.setReleaseYear(Short.valueOf(dto.getSalesInfo().getYear()));
                    } catch (NumberFormatException e) {
                        System.err.println("Błąd parsowania roku: " + dto.getSalesInfo().getYear());
                    }
                }
                if (dto.getSteamInfo().getRequiredAge() != null) {
                    game.setRequiredAge(dto.getSteamInfo().getRequiredAge().byteValue());
                }

                // Mapowanie słowników (cache)
                game.setGenre(getGenre(dto.getSalesInfo().getGenre()));
                game.setPublisher(getPublisher(dto.getSalesInfo().getPublisher()));
                Platform platform = getPlatform(dto.getSalesInfo().getPlatform());
                game.setPlatform(platform);

                // Systemy operacyjne
                if (Boolean.TRUE.equals(dto.getSteamInfo().getIsWindows())) game.getOperatingSystems().add(osCache.get("Windows"));
                if (Boolean.TRUE.equals(dto.getSteamInfo().getIsMac())) game.getOperatingSystems().add(osCache.get("MacOS"));
                if (Boolean.TRUE.equals(dto.getSteamInfo().getIsLinux())) game.getOperatingSystems().add(osCache.get("Linux"));

                // Sprzedaż
                addSalesData(game, regionCache.get("NA"), platform, dto.getSalesInfo().getNaSales());
                addSalesData(game, regionCache.get("EU"), platform, dto.getSalesInfo().getEuSales());
                addSalesData(game, regionCache.get("JP"), platform, dto.getSalesInfo().getJpSales());
                addSalesData(game, regionCache.get("Other"), platform, dto.getSalesInfo().getOtherSales());

                gameRepository.save(game);
            }
        }
        System.out.println("IMPORT ZAKOŃCZONY SUKCESEM");
    }

    private void initRegions() {
        List.of(
                new Region("NA", "North America"),
                new Region("EU", "Europe"),
                new Region("JP", "Japan"),
                new Region("Other", "Rest of World"),
                new Region("Global", "Global")
        ).forEach(r -> {
            regionRepository.save(r);
            regionCache.put(r.getCode(), r);
        });
    }

    private void initOS() {
        List.of("Windows", "MacOS", "Linux").forEach(name -> {
            OperatingSystem os = new OperatingSystem(name);
            osRepository.save(os);
            osCache.put(name, os);
        });
    }

    private Genre getGenre(String name) {
        if (name == null) return null;
        return genreCache.computeIfAbsent(name, k -> genreRepository.save(new Genre(k)));
    }

    private Publisher getPublisher(String name) {
        if (name == null) return null;
        return publisherCache.computeIfAbsent(name, k -> publisherRepository.save(new Publisher(k)));
    }

    private Platform getPlatform(String name) {
        if (name == null) return null;
        return platformCache.computeIfAbsent(name, k -> platformRepository.save(new Platform(k)));
    }

    private void addSalesData(Game game, Region region, Platform platform, Double amount) {
        if (amount != null && amount > 0) {
            game.addSales(new GameSales(game, region, platform, amount.floatValue()));
        }
    }
}