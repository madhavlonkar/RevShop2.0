package com.revshop.service;

import java.util.List;

import com.revshop.master.FavoriteMaster;

public interface FavoriteService {
    // Add a product to the wishlist if it doesn't already exist
    void addProductToFavorites(int userId, int productId);

    // Get the list of favorite products for a specific user by user ID
    List<FavoriteMaster> getFavoritesByUserId(int userId);

    // Remove a product from the user's wishlist
    void removeProductFromFavorites(int userId, int productId);

    // Move a product from the wishlist to the cart (removes from wishlist and adds to cart)
    void moveProductToCart(int userId, int productId);

    // Check if the product is already in the user's wishlist
    boolean isProductInWishlist(int userId, int productId);
}
