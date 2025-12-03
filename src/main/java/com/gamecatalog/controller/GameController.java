package com.gamecatalog.controller;

import com.gamecatalog.dto.game.GameDetailDTO;
import com.gamecatalog.dto.game.GameSummaryDTO;
import com.gamecatalog.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @GetMapping
    public ResponseEntity<Page<GameSummaryDTO>> getAllGames(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(gameService.searchGames(search, pageable));
        }
        return ResponseEntity.ok(gameService.getAllGames(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameDetailDTO> getGameById(@PathVariable Long id) {
        return ResponseEntity.ok(gameService.getGameById(id));
    }

    @GetMapping("/{id}/similar")
    public ResponseEntity<List<GameSummaryDTO>> getSimilarGames(@PathVariable Long id) {
        return ResponseEntity.ok(gameService.getSimilarGames(id));
    }
}