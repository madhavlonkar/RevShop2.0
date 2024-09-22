package com.revshop.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
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

import com.revshop.dao.impl.ReviewDAOImpl;
import com.revshop.master.ReviewMaster;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class ReviewDAOImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<ReviewMaster> reviewQuery;

    @InjectMocks
    private ReviewDAOImpl reviewDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindReviewsByProductId_Success() {
        int productId = 1;
        List<ReviewMaster> reviewList = new ArrayList<>();
        reviewList.add(new ReviewMaster());

        String query = "FROM ReviewMaster WHERE product.productId = :productId";
        when(entityManager.createQuery(query, ReviewMaster.class)).thenReturn(reviewQuery);
        when(reviewQuery.setParameter("productId", productId)).thenReturn(reviewQuery);
        when(reviewQuery.getResultList()).thenReturn(reviewList);

        List<ReviewMaster> result = reviewDAO.findReviewsByProductId(productId);
        assertEquals(reviewList, result);
        verify(entityManager, times(1)).createQuery(query, ReviewMaster.class);
        verify(reviewQuery, times(1)).setParameter("productId", productId);
        verify(reviewQuery, times(1)).getResultList();
    }

    @Test
    void testSave_NewReview_Success() {
        ReviewMaster review = new ReviewMaster();
        review.setReviewId(0);  // Indicating a new review

        reviewDAO.save(review);

        verify(entityManager, times(1)).persist(review);
        verify(entityManager, never()).merge(review);
    }

    @Test
    void testSave_ExistingReview_Success() {
        ReviewMaster review = new ReviewMaster();
        review.setReviewId(1);  // Indicating an existing review

        reviewDAO.save(review);

        verify(entityManager, times(1)).merge(review);
        verify(entityManager, never()).persist(review);
    }
}
