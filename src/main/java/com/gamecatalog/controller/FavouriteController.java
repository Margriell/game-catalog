package com.gamecatalog.controller;

import com.gamecatalog.dto.FavouriteDTO;
import com.gamecatalog.service.FavouriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FavouriteController {
    private final FavouriteService favouriteService;

    @PostMapping("/games/{gameId}/favourite")
    public ResponseEntity<String> toggleFavourite(
            @PathVariable Long gameId,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        String result = favouriteService.toggleFavourite(userDetails.getUsername(), gameId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/users/me/favorites")
    public ResponseEntity<List<FavouriteDTO>> getMyFavorites(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(favouriteService.getMyFavourites(userDetails.getUsername()));
    }
}
