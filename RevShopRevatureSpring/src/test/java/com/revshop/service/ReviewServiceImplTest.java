package com.revshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revshop.dao.ReviewDAO;
import com.revshop.master.ReviewMaster;
import com.revshop.service.impl.ReviewServiceImpl;

public class ReviewServiceImplTest {

    @Mock
    private ReviewDAO reviewRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetReviewsByProductId_Success() {
        int productId = 1;
        List<ReviewMaster> reviews = new ArrayList<>();
        reviews.add(new ReviewMaster());
        reviews.add(new ReviewMaster());

        when(reviewRepository.findReviewsByProductId(productId)).thenReturn(reviews);

        // Call the method
        List<ReviewMaster> result = reviewService.getReviewsByProductId(productId);

        // Assert that the returned list matches the mocked data
        assertEquals(2, result.size());
        verify(reviewRepository, times(1)).findReviewsByProductId(productId);
    }

    @Test
    void testGetReviewsByProductId_EmptyList() {
        int productId = 1;
        List<ReviewMaster> reviews = new ArrayList<>();

        when(reviewRepository.findReviewsByProductId(productId)).thenReturn(reviews);

        // Call the method
        List<ReviewMaster> result = reviewService.getReviewsByProductId(productId);

        // Assert that the returned list is empty
        assertTrue(result.isEmpty());
        verify(reviewRepository, times(1)).findReviewsByProductId(productId);
    }

    @Test
    void testSaveReview_Success() {
        ReviewMaster review = new ReviewMaster();
        review.setReviewId(1);
        review.setReviewContent("Great product!");

        // Call the method
        reviewService.saveReview(review);

        // Verify that the save method was called in the repository
        verify(reviewRepository, times(1)).save(review);
    }
}
