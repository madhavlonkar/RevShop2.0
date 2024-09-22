package com.revshop.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.revshop.master.FavoriteMaster;

import jakarta.transaction.Transactional;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteMaster, Integer> {

    // Custom query to get the list of favorites for a given user
    @Query("SELECT f FROM FavoriteMaster f WHERE f.user.userId = :userId")
    List<FavoriteMaster> findFavoritesByUserId(@Param("userId") int userId);

    // Check if a product is already in the user's wishlist
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM FavoriteMaster f WHERE f.user.userId = :userId AND f.product.productId = :productId")
    boolean existsByUserIdAndProductId(@Param("userId") int userId, @Param("productId") int productId);

    // Method to delete a specific product from the user's wishlist
    @Modifying  // This tells Spring Data JPA that this is a modifying query
    @Transactional
    @Query("DELETE FROM FavoriteMaster f WHERE f.user.userId = :userId AND f.product.productId = :productId")
    void deleteFavoriteByUserIdAndProductId(@Param("userId") int userId, @Param("productId") int productId);
}
