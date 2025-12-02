package com.gamecatalog.dto.game;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameSummaryDTO {
    private Long id;
    private String name;
    private String shortDescription;
    private Double price;
    private String currency;
    private Boolean isFree;
    private String genre;
    private String platform;
    private String headerImage;
}