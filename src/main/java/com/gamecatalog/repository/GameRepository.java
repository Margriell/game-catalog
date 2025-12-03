package com.gamecatalog.repository;
import com.gamecatalog.model.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Page<Game> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Game> findByGenreIdAndIdNot(Long genreId, Long id, Pageable pageable);
}