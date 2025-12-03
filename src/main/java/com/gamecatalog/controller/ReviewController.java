package com.gamecatalog.controller;

import com.gamecatalog.dto.review.ReviewRequest;
import com.gamecatalog.dto.review.ReviewResponse;
import com.gamecatalog.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/{gameId}/reviews")
    public ResponseEntity<ReviewResponse> addReview(
            @PathVariable Long gameId,
            @Valid @RequestBody ReviewRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(reviewService.addReview(userDetails.getUsername(), gameId, request));
    }

    @GetMapping("/{gameId}/reviews")
    public ResponseEntity<List<ReviewResponse>> getReviews(@PathVariable Long gameId) {
        return ResponseEntity.ok(reviewService.getGameReviews(gameId));
    }
    @DeleteMapping("/{reviewId}") // /api/games/{reviewId}
    public ResponseEntity<String> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            reviewService.deleteReview(reviewId, userDetails.getUsername());
            return ResponseEntity.ok("Recenzja została usunięta");
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Brak uprawnień")) {
                return ResponseEntity.status(403).body(e.getMessage());
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
