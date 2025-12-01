package com.gamecatalog.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavouriteDTO {
    private Long gameId;
    private String gameName;
}
