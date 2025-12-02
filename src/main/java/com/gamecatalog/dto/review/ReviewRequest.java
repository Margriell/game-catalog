package com.gamecatalog.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {
    @NotNull(message = "Ocena jest wymagana")
    @Min(value = 1, message = "Ocena musi być w zakresie 1-5")
    @Max(value = 5, message = "Ocena musi być w zakresie 1-5")
    private Integer rating;
    private String reviewText;
}