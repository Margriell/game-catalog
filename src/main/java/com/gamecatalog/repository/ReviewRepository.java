package com.gamecatalog.repository;

import com.gamecatalog.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByGameId(Long gameId);
    boolean existsByUserIdAndGameId(Long userId, Long gameId);
}
