package com.gamecatalog.dto.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameSummaryDTO {
    private Long id;
    private String name;
    private String shortDescription;
    private Float price;
    private String currency;
    private Boolean isFree;
    private String genre;
    private String platform;
    private String headerImage;
}