package com.revshop.dao;

import com.revshop.master.ReviewMaster;
import java.util.List;

public interface ReviewDAO {
    List<ReviewMaster> findReviewsByProductId(int productId);
    void save(ReviewMaster review); // For saving the review
}
