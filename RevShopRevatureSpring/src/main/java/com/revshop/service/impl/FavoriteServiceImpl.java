package com.revshop.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revshop.master.CartMaster;
import com.revshop.master.FavoriteMaster;
import com.revshop.master.ProductMaster;
import com.revshop.master.UserMaster;
import com.revshop.repo.FavoriteRepository;
import com.revshop.service.CartService;
import com.revshop.service.FavoriteService;

import jakarta.transaction.Transactional;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private static final Logger logger = LoggerFactory.getLogger(FavoriteServiceImpl.class);

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private CartService cartService;

    @Override
    public void addProductToFavorites(int userId, int productId) {
        logger.info("Adding product {} to favorites for user {}", productId, userId);

        // Check if the product is already in the user's wishlist
        if (!favoriteRepository.existsByUserIdAndProductId(userId, productId)) {
            try {
                // Create UserMaster and ProductMaster instances
                UserMaster user = new UserMaster();
                user.setUserId(userId);
                ProductMaster product = new ProductMaster();
                product.setProductId(productId);

                // Create and save the FavoriteMaster instance
                FavoriteMaster favorite = new FavoriteMaster();
                favorite.setUser(user);
                favorite.setProduct(product);
                favorite.setCreatedDate(LocalDateTime.now());

                favoriteRepository.save(favorite); // Save the favorite
                logger.info("Product {} successfully added to favorites for user {}", productId, userId);
            } catch (Exception e) {
                logger.error("Error adding product {} to favorites for user {}", productId, userId, e);
                // Consider throwing a custom exception or returning an error message
            }
        } else {
            logger.info("Product {} is already in favorites for user {}", productId, userId);
        }
    }

    @Override
    public List<FavoriteMaster> getFavoritesByUserId(int userId) {
        logger.info("Fetching favorites for user {}", userId);
        return favoriteRepository.findFavoritesByUserId(userId); // Use the repository method
    }

    @Override
    @Transactional
    public void removeProductFromFavorites(int userId, int productId) {
        logger.info("Removing product {} from favorites for user {}", productId, userId);
        try {
            // Use the repository method to remove the favorite
            favoriteRepository.deleteFavoriteByUserIdAndProductId(userId, productId);
            logger.info("Product {} successfully removed from favorites for user {}", productId, userId);
        } catch (Exception e) {
            logger.error("Error removing product {} from favorites for user {}", productId, userId, e);
            // Handle error or throw exception
        }
    }

    @Override
    @Transactional
    public void moveProductToCart(int userId, int productId) {
        logger.info("Moving product {} to cart for user {}", productId, userId);
        try {
            // First, remove the product from the favorites
            removeProductFromFavorites(userId, productId);

            // Prepare user and product entities
            UserMaster user = new UserMaster();
            user.setUserId(userId);
            ProductMaster product = new ProductMaster();
            product.setProductId(productId);

            // Check if the product already exists in the user's cart
            CartMaster existingCartItem = cartService.getCartItemForUserAndProduct(user, product);

            if (existingCartItem != null) {
                // If the product is already in the cart, update its quantity
                cartService.updateProductQuantity(user, product, 1);
                logger.info("Updated quantity for product {} in the cart for user {}", productId, userId);
            } else {
                // If the product is not in the cart, add it
                cartService.addProductToCart(user, product, 1);
                logger.info("Added product {} to cart for user {}", productId, userId);
            }
        } catch (Exception e) {
            logger.error("Error moving product {} to cart for user {}", productId, userId, e);
            // Consider handling rollback scenarios more explicitly
        }
    }

    @Override
    public boolean isProductInWishlist(int userId, int productId) {
        logger.info("Checking if product {} is in the wishlist for user {}", productId, userId);
        // Use the repository method to check if the product is in the wishlist
        return favoriteRepository.existsByUserIdAndProductId(userId, productId);
    }
}
