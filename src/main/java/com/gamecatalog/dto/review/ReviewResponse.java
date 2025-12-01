package com.gamecatalog.dto.review;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ReviewResponse {
    private Long id;
    private String userName;
    private Integer rating;
    private String reviewText;
    private Date createdAt;
}
