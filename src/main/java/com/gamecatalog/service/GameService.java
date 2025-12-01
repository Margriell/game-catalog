package com.gamecatalog.service;

import com.gamecatalog.dto.game.GameDetailDTO;
import com.gamecatalog.dto.game.GameSummaryDTO;
import com.gamecatalog.mapper.GameMapper;
import com.gamecatalog.model.Game;
import com.gamecatalog.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    @Transactional(readOnly = true)
    public Page<GameSummaryDTO> getAllGames(Pageable pageable) {
        return gameRepository.findAll(pageable)
                .map(gameMapper::toSummaryDTO);
    }

    @Transactional(readOnly = true)
    public Page<GameSummaryDTO> searchGames(String query, Pageable pageable) {
        return gameRepository.findByNameContainingIgnoreCase(query, pageable)
                .map(gameMapper::toSummaryDTO);
    }

    @Transactional(readOnly = true)
    public GameDetailDTO getGameById(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nie znaleziono gry o id: " + id));
        return gameMapper.toDetailDTO(game);
    }
}