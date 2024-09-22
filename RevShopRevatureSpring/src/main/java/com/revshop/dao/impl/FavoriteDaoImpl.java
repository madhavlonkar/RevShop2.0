//package com.revshop.dao.impl;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import com.revshop.dao.FavoriteDao;
//import com.revshop.master.CartMaster;
//import com.revshop.master.FavoriteMaster;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.Query;
//import jakarta.transaction.Transactional;
//
//@Repository
//public class FavoriteDaoImpl implements FavoriteDao {
//
//    @Autowired
//    private EntityManager entityManager;
//
//    @Override
//    @Transactional
//    public void addFavoriteIfNotExists(FavoriteMaster favorite) {
//        if (!isProductInWishlist(favorite.getUser().getUserId(), favorite.getProduct().getProductId())) {
//            entityManager.persist(favorite);
//        }
//    }
//
//    @Override
//    public List<FavoriteMaster> getFavoritesByUserId(int userId) {
//        String hql = "FROM FavoriteMaster WHERE user.userId = :userId";
//        return entityManager.createQuery(hql, FavoriteMaster.class)
//                .setParameter("userId", userId)
//                .getResultList();
//    }
//
//    @Override
//    public void removeFavorite(int userId, int productId) {
//        String hql = "FROM FavoriteMaster WHERE user.userId = :userId AND product.productId = :productId";
//        Query query = entityManager.createQuery(hql);
//        query.setParameter("userId", userId);
//        query.setParameter("productId", productId);
//        
//        // Fetch the FavoriteMaster entity first
//        FavoriteMaster favorite = (FavoriteMaster) query.getSingleResult();
//        
//        // Remove the entity using entityManager.remove()
//        entityManager.remove(favorite);
//    }
//
//    
//    @Override
//    public boolean isProductInWishlist(int userId, int productId) {
//        String hql = "SELECT COUNT(f) FROM FavoriteMaster f WHERE f.user.userId = :userId AND f.product.productId = :productId";
//        Long count = entityManager.createQuery(hql, Long.class)
//                .setParameter("userId", userId)
//                .setParameter("productId", productId)
//                .getSingleResult();
//        return count > 0;
//    }
//}
