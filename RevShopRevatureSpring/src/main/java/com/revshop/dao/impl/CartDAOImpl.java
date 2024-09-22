package com.revshop.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.revshop.dao.CartDAO;
import com.revshop.master.CartMaster;
import com.revshop.master.ProductMaster;
import com.revshop.master.UserMaster;

import jakarta.persistence.EntityManager;

@Repository
public class CartDAOImpl implements CartDAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public CartMaster findByUserAndProduct(UserMaster user, ProductMaster product) {
        String hql = "FROM CartMaster WHERE user = :user AND product = :product";
        List<CartMaster> results = entityManager.createQuery(hql, CartMaster.class)
            .setParameter("user", user)
            .setParameter("product", product)
            .getResultList();

        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<CartMaster> findByUser(UserMaster user) {
        String hql = "FROM CartMaster WHERE user = :user";
        return entityManager.createQuery(hql, CartMaster.class)
            .setParameter("user", user)
            .getResultList();
    }

    @Override
    public CartMaster findById(int cartId) {
        return entityManager.find(CartMaster.class, cartId);
    }

    @Override
    public void delete(CartMaster cartItem) {
        entityManager.remove(cartItem);
    }

    @Override
    public CartMaster saveOrUpdateCart(CartMaster cartMaster) {
        return entityManager.merge(cartMaster);
    }
    
    @Override
    public void clearCartByUser(UserMaster user) {
        Session session = entityManager.unwrap(Session.class);
        String hql = "DELETE FROM CartMaster WHERE user = :user";
        session.createQuery(hql)
               .setParameter("user", user)
               .executeUpdate();
    }
}
