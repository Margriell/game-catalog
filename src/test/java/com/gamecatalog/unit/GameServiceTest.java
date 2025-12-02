package com.gamecatalog.unit;

import com.gamecatalog.dto.game.GameDetailDTO;
import com.gamecatalog.mapper.GameMapper;
import com.gamecatalog.model.Game;
import com.gamecatalog.repository.GameRepository;
import com.gamecatalog.service.GameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameMapper gameMapper;

    @InjectMocks
    private GameService gameService;

    @Test
    void shouldReturnGame_WhenExists() {
        Long gameId = 1L;
        Game game = new Game();
        game.setId(gameId);
        game.setName("Witcher 3");

        GameDetailDTO dto = GameDetailDTO.builder().id(gameId).name("Witcher 3").build();

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(gameMapper.toDetailDTO(game)).thenReturn(dto);

        GameDetailDTO result = gameService.getGameById(gameId);

        assertNotNull(result);
        assertEquals("Witcher 3", result.getName());
    }

    @Test
    void shouldThrowException_WhenGameNotFound() {
        Long gameId = 999L;
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> gameService.getGameById(gameId));
    }
}