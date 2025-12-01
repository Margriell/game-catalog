package com.gamecatalog.service;

import com.gamecatalog.dto.FavouriteDTO;
import com.gamecatalog.model.Favourite;
import com.gamecatalog.model.Game;
import com.gamecatalog.model.user.User;
import com.gamecatalog.repository.FavouriteRepository;
import com.gamecatalog.repository.GameRepository;
import com.gamecatalog.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavouriteService {
    private final FavouriteRepository favouriteRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    @Transactional
    public String toggleFavourite(String userEmail, Long gameId){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        Optional<Favourite> existing = favouriteRepository.findByUserIdAndGameId(user.getId(), gameId);

        if(existing.isPresent()) {
            favouriteRepository.delete(existing.get());
            return "Game removed from favourites";
        } else  {
            Favourite favourite = Favourite.builder()
                    .user(user)
                    .game(game)
                    .build();
            favouriteRepository.save(favourite);
            return "Game added to favourites";
        }
    }
    public List<FavouriteDTO> getMyFavourites(String userEmail){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return favouriteRepository.findAllByUserId(user.getId()).stream()
                .map(fav -> FavouriteDTO.builder()
                        .gameId(fav.getGame().getId())
                        .gameName(fav.getGame().getName())
                        .build())
                .collect(Collectors.toList());
    }
}
