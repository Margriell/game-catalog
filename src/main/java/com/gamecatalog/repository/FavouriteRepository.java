package com.gamecatalog.repository;

import com.gamecatalog.model.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
    Optional<Favourite> findByUserIdAndGameId(Long userId, Long gameId);
    List<Favourite> findAllByUserId(Long userId);
}
