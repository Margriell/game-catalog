package com.gamecatalog;

import com.gamecatalog.dto.review.ReviewRequest;
import com.gamecatalog.dto.review.ReviewResponse;
import com.gamecatalog.model.Game;
import com.gamecatalog.model.Review;
import com.gamecatalog.model.user.User;
import com.gamecatalog.repository.GameRepository;
import com.gamecatalog.repository.ReviewRepository;
import com.gamecatalog.repository.UserRepository;
import com.gamecatalog.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void addReview_ShouldThrowException_WhenReviewAlreadyExists() {
        String email = "test@test.pl";
        Long gameId = 1L;
        Long userId = 10L;

        User user = new User();
        user.setId(userId);
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(reviewRepository.existsByUserIdAndGameId(userId, gameId)).thenReturn(true);

        ReviewRequest request = new ReviewRequest();

        assertThrows(RuntimeException.class, () -> {
            reviewService.addReview(email, gameId, request);
        });
    }

    @Test
    void addReview_ShouldSave_WhenNewReview() {
        String email = "test@test.pl";
        Long gameId = 1L;
        User user = new User(); user.setId(10L); user.setFirstName("Jan");
        Game game = new Game(); game.setId(gameId);

        ReviewRequest request = new ReviewRequest();
        request.setRating(5);
        request.setReviewText("Super!");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(reviewRepository.existsByUserIdAndGameId(user.getId(), gameId)).thenReturn(false);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArguments()[0]);

        ReviewResponse result = reviewService.addReview(email, gameId, request);

        assertNotNull(result);
        assertEquals(5, result.getRating());
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    void addReview_ShouldThrowException_WhenGameNotFound(){
        String email = "test@test.pl";
        Long nonExistentGameId = 9999L;
        Long userId = 10L;

        User user = new User();
        user.setId(userId);

        //user istnieje
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        //nie ma duplikatu recenzji
        when(reviewRepository.existsByUserIdAndGameId(userId, nonExistentGameId)).thenReturn(false);

        //gra nie istnieje
        when(gameRepository.findById(nonExistentGameId)).thenReturn(Optional.empty());

        ReviewRequest request = new ReviewRequest();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reviewService.addReview(email, nonExistentGameId, request);
        });

        assertEquals("Game not found", exception.getMessage());

        //czy na pewno w bazie nic sie nie zapisało
        verify(reviewRepository, never()).save(any());
    }
}