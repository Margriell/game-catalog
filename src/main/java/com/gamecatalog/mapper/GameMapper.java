package com.gamecatalog.mapper;

import com.gamecatalog.dto.game.GameDetailDTO;
import com.gamecatalog.dto.game.GameSummaryDTO;
import com.gamecatalog.model.Game;
import com.gamecatalog.model.GameSales;
import com.gamecatalog.model.OperatingSystem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface GameMapper {
    String STEAM_IMG_URL = "https://cdn.akamai.steamstatic.com/steam/apps/%s/header.jpg";

    @Mapping(source = "genre.name", target = "genre")
    @Mapping(source = "platform.name", target = "platform")
    @Mapping(target = "headerImage", expression = "java(getImgUrl(game.getSteamAppId()))")
    GameSummaryDTO toSummaryDTO(Game game);

    @Mapping(source = "genre.name", target = "genre")
    @Mapping(source = "publisher.name", target = "publisher")
    @Mapping(source = "platform.name", target = "platform")
    @Mapping(source = "operatingSystems", target = "operatingSystems", qualifiedByName = "mapOS")
    @Mapping(target = "headerImage", expression = "java(getImgUrl(game.getSteamAppId()))")
    @Mapping(target = "sales", expression = "java(mapSales(game.getSalesData()))")
    GameDetailDTO toDetailDTO(Game game);

    @Named("mapOS")
    default Set<String> mapOperatingSystems(Set<OperatingSystem> systems) {
        if (systems == null) return Set.of();
        return systems.stream()
                .map(OperatingSystem::getName)
                .collect(Collectors.toSet());
    }

    default String getImgUrl(Long steamAppId) {
        if (steamAppId == null) return null;
        return String.format(STEAM_IMG_URL, steamAppId);
    }

    default List<GameDetailDTO.SalesDTO> mapSales(Set<GameSales> salesData) {
        if (salesData == null) return List.of();
        return salesData.stream()
                .map(s -> GameDetailDTO.SalesDTO.builder()
                        .region(s.getRegion().getName())
                        .platform(s.getPlatform().getName())
                        .amount(s.getSales())
                        .build())
                .collect(Collectors.toList());
    }
}