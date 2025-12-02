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
    private static final String STEAM_IMG_URL = "https://cdn.akamai.steamstatic.com/steam/apps/%s/header.jpg";

    @Transactional
    public String toggleFavourite(String userEmail, Long gameId){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika"));

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono gry"));

        Optional<Favourite> existing = favouriteRepository.findByUserIdAndGameId(user.getId(), gameId);

        if(existing.isPresent()) {
            favouriteRepository.delete(existing.get());
            return "Gra usunięta z ulubionych";
        } else  {
            Favourite favourite = Favourite.builder()
                    .user(user)
                    .game(game)
                    .build();
            favouriteRepository.save(favourite);
            return "Gra dodana do ulubionych";
        }
    }

    public List<FavouriteDTO> getMyFavourites(String userEmail){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika"));

        return favouriteRepository.findAllByUserId(user.getId()).stream()
                .map(fav -> FavouriteDTO.builder()
                        .gameId(fav.getGame().getId())
                        .gameName(fav.getGame().getName())
                        .headerImage(String.format(STEAM_IMG_URL, fav.getGame().getSteamAppId()))
                        .build())
                .collect(Collectors.toList());
    }
}