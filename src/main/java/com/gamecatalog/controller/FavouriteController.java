package com.gamecatalog.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.gamecatalog.dto.FavouriteDTO;
import com.gamecatalog.service.FavouriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FavouriteController {

    private final FavouriteService favouriteService;
    private final ObjectMapper objectMapper;
    private final XmlMapper xmlMapper = new XmlMapper();

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

    @GetMapping("/users/me/favorites/export")
    public ResponseEntity<byte[]> exportFavorites(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "txt") String format
    ) throws JsonProcessingException {

        List<FavouriteDTO> favorites = favouriteService.getMyFavourites(userDetails.getUsername());

        byte[] content;
        String fileName;
        MediaType mediaType;

        switch (format.toLowerCase()) {
            case "json" -> {
                String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(favorites);
                content = jsonString.getBytes(StandardCharsets.UTF_8);
                fileName = "moje_gry.json";
                mediaType = MediaType.APPLICATION_JSON;
            }
            case "xml" -> {
                String xmlString = xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(favorites);
                content = xmlString.getBytes(StandardCharsets.UTF_8);
                fileName = "moje_gry.xml";
                mediaType = MediaType.APPLICATION_XML;
            }
            default -> {
                StringBuilder builder = new StringBuilder();
                builder.append("MOJE ULUBIONE GRY - RAPORT\n");

                favorites.forEach(fav ->
                        builder.append("- ").append(fav.getGameName())
                                .append(" (ID: ").append(fav.getGameId()).append(")\n")
                );

                content = builder.toString().getBytes(StandardCharsets.UTF_8);
                fileName = "moje_gry.txt";
                mediaType = MediaType.TEXT_PLAIN;
            }
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(mediaType)
                .body(content);
    }
}