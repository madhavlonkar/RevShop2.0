package com.revshop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revshop.dao.ReviewDAO;
import com.revshop.master.ReviewMaster;
import com.revshop.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewDAO reviewRepository;

    @Override
    public List<ReviewMaster> getReviewsByProductId(int productId) {
        return reviewRepository.findReviewsByProductId(productId);
    }

    @Override
    public void saveReview(ReviewMaster review) {
        reviewRepository.save(review);
    }
}
