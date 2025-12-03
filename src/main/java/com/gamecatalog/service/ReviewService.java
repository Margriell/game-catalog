package com.gamecatalog.service;

import com.gamecatalog.dto.review.ReviewRequest;
import com.gamecatalog.dto.review.ReviewResponse;
import com.gamecatalog.model.Game;
import com.gamecatalog.model.Review;
import com.gamecatalog.model.user.User;
import com.gamecatalog.repository.GameRepository;
import com.gamecatalog.repository.ReviewRepository;
import com.gamecatalog.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    @Transactional
    public ReviewResponse addReview(String userEmail, Long gameId, ReviewRequest reviewRequest) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika"));

        if (reviewRepository.existsByUserIdAndGameId(user.getId(), gameId)) {
            throw new RuntimeException("Już dodałeś recenzję dla tej gry");
        }

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono gry"));


        Review review = Review.builder()
                .user(user)
                .game(game)
                .rating(reviewRequest.getRating())
                .reviewText(reviewRequest.getReviewText())
                .build();

        Review saved= reviewRepository.save(review);

        return mapToResponse(saved);
    }

    public List<ReviewResponse> getGameReviews(Long gameId) {
        return reviewRepository.findAllByGameId(gameId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ReviewResponse mapToResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .userName(review.getUser().getFullName())
                .rating(review.getRating())
                .reviewText(review.getReviewText())
                .createdAt(review.getCreatedAt())
                .build();
    }
    @Transactional
    public void deleteReview(Long reviewId, String userEmail) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono recenzji"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika"));

        boolean isAuthor = review.getUser().getId().equals(user.getId());
        boolean isAdmin = user.getRole() == com.gamecatalog.model.user.enums.Role.ADMIN;

        if (!isAuthor && !isAdmin) {
            throw new RuntimeException("Brak uprawnień do usunięcia tej recenzji"); // To zwróci błąd 500, w kontrolerze obsłużymy to lepiej lub dodaj @ResponseStatus(HttpStatus.FORBIDDEN) w exception handlerze
        }

        reviewRepository.delete(review);
    }
}
