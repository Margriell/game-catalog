package com.gamecatalog.dto.importdata;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GameImportDTO {
    @JsonProperty("Name")
    private String name;
    @JsonProperty("SteamGamesSales")
    private SteamGamesSalesDTO salesInfo;
    @JsonProperty("SteamGames")
    private SteamGamesDTO steamInfo;
}