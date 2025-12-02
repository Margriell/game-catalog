package com.gamecatalog.dto.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameDetailDTO {
    private Long id;
    private Long steamAppId;
    private String name;
    private String shortDescription;
    private Boolean isFree;
    private Float price;
    private String currency;
    private String controllerSupport;
    private Short releaseYear;
    private Byte requiredAge;
    private String headerImage;

    private String genre;
    private String publisher;
    private String platform;
    private Set<String> operatingSystems;

    private List<SalesDTO> sales;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SalesDTO {
        private String region;
        private String platform;
        private Float amount;
    }
}