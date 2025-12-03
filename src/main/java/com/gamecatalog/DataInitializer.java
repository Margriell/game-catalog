package com.gamecatalog;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamecatalog.dto.importdata.GameImportDTO;
import com.gamecatalog.model.*;
import com.gamecatalog.model.user.User;
import com.gamecatalog.model.user.enums.Role;
import com.gamecatalog.model.user.enums.Status;
import com.gamecatalog.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    private final Map<String, Genre> genreCache = new HashMap<>();
    private final Map<String, Publisher> publisherCache = new HashMap<>();
    private final Map<String, Platform> platformCache = new HashMap<>();
    private final Map<String, OperatingSystem> osCache = new HashMap<>();
    private final Map<String, Region> regionCache = new HashMap<>();

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        createDefaultAdmin();

        if (gameRepository.count() > 0) {
            return;
        }

        System.out.println("START IMPORTU DANYCH");

        initRegions();
        initOS();

        try (InputStream inputStream = getClass().getResourceAsStream("/steam_games_database.json")) {
            List<GameImportDTO> dtos = objectMapper.readValue(inputStream, new TypeReference<>() {});

            dtos.stream()
                    .filter(dto -> dto.getSteamInfo() != null && dto.getSalesInfo() != null)
                    .map(this::mapToGame)
                    .forEach(gameRepository::save);
        }
        System.out.println("IMPORT ZAKOŃCZONY SUKCESEM");
    }

    private Game mapToGame(GameImportDTO dto) {
        Game game = new Game();

        game.setSteamAppId(dto.getSteamInfo().getAppId());
        game.setName(dto.getSteamInfo().getName());
        game.setShortDescription(dto.getSteamInfo().getShortDescription());
        game.setIsFree(dto.getSteamInfo().getIsFree());
        game.setPrice(dto.getSteamInfo().getPrice() != null ? dto.getSteamInfo().getPrice().floatValue() : 0f);
        game.setCurrency(dto.getSteamInfo().getCurrency());
        game.setPriceStatus(dto.getSteamInfo().getPriceStatus());
        game.setControllerSupport(dto.getSteamInfo().getControllerSupport());

        if (dto.getSalesInfo().getYear() != null && !dto.getSalesInfo().getYear().equals("N/A")) {
            try {
                game.setReleaseYear(Short.valueOf(dto.getSalesInfo().getYear()));
            } catch (NumberFormatException e) {
            }
        }
        if (dto.getSteamInfo().getRequiredAge() != null) {
            game.setRequiredAge(dto.getSteamInfo().getRequiredAge().byteValue());
        }

        game.setGenre(getGenre(dto.getSalesInfo().getGenre()));
        game.setPublisher(getPublisher(dto.getSalesInfo().getPublisher()));
        Platform platform = getPlatform(dto.getSalesInfo().getPlatform());
        game.setPlatform(platform);

        if (Boolean.TRUE.equals(dto.getSteamInfo().getIsWindows())) game.getOperatingSystems().add(osCache.get("Windows"));
        if (Boolean.TRUE.equals(dto.getSteamInfo().getIsMac())) game.getOperatingSystems().add(osCache.get("MacOS"));
        if (Boolean.TRUE.equals(dto.getSteamInfo().getIsLinux())) game.getOperatingSystems().add(osCache.get("Linux"));

        addSalesData(game, regionCache.get("NA"), platform, dto.getSalesInfo().getNaSales());
        addSalesData(game, regionCache.get("EU"), platform, dto.getSalesInfo().getEuSales());
        addSalesData(game, regionCache.get("JP"), platform, dto.getSalesInfo().getJpSales());
        addSalesData(game, regionCache.get("Other"), platform, dto.getSalesInfo().getOtherSales());

        return game;
    }

    private void createDefaultAdmin() {
        String adminEmail = "admin@gamecatalog.com";
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = User.builder()
                    .firstName("Admin")
                    .lastName("System")
                    .email(adminEmail)
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .status(Status.ACTIVE)
                    .build();
            userRepository.save(admin);
            System.out.println("Utworzono konto administratora: " + adminEmail);
        }
    }

    private void initRegions() {
        List.of(
                new Region("NA", "North America"),
                new Region("EU", "Europe"),
                new Region("JP", "Japan"),
                new Region("Other", "Rest of World"),
                new Region("Global", "Global")
        ).forEach(r -> {
            var existing = regionRepository.findAll().stream()
                    .filter(reg -> reg.getCode().equals(r.getCode()))
                    .findFirst();

            if (existing.isPresent()) {
                regionCache.put(r.getCode(), existing.get());
            } else {
                regionRepository.save(r);
                regionCache.put(r.getCode(), r);
            }
        });
    }

    private void initOS() {
        List.of("Windows", "MacOS", "Linux").forEach(name -> {
            var existing = osRepository.findAll().stream()
                    .filter(os -> os.getName().equals(name))
                    .findFirst();

            if (existing.isPresent()) {
                osCache.put(name, existing.get());
            } else {
                OperatingSystem os = new OperatingSystem(name);
                osRepository.save(os);
                osCache.put(name, os);
            }
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