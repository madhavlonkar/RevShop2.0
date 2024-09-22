package com.revshop.service;

import com.revshop.master.ReviewMaster;
import java.util.List;

public interface ReviewService {
    List<ReviewMaster> getReviewsByProductId(int productId);
    void saveReview(ReviewMaster review);
}
