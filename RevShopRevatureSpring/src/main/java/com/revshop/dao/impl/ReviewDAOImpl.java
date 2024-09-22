package com.revshop.dao.impl;

import com.revshop.dao.ReviewDAO;
import com.revshop.master.ReviewMaster;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewDAOImpl implements ReviewDAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<ReviewMaster> findReviewsByProductId(int productId) {
        String query = "FROM ReviewMaster WHERE product.productId = :productId";
        return entityManager.createQuery(query, ReviewMaster.class)
                .setParameter("productId", productId)
                .getResultList();
    }

    @Override
    @Transactional
    public void save(ReviewMaster review) {
        if (review.getReviewId() == 0) {
            entityManager.persist(review); // If it's a new review
        } else {
            entityManager.merge(review); // If it's an existing review (for updating)
        }
    }
}
