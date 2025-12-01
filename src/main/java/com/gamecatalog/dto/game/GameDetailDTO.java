package com.gamecatalog.dto.game;

import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.Set;

@Data
@Builder
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

    @Data
    @Builder
    public static class SalesDTO {
        private String region;
        private String platform;
        private Float amount;
    }
}