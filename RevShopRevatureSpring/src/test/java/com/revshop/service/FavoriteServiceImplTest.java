package com.revshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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

import com.revshop.master.CartMaster;
import com.revshop.master.FavoriteMaster;
import com.revshop.master.ProductMaster;
import com.revshop.master.UserMaster;
import com.revshop.repo.FavoriteRepository;
import com.revshop.service.impl.FavoriteServiceImpl;

public class FavoriteServiceImplTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private CartService cartService;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProductToFavorites_Success() {
        int userId = 1;
        int productId = 100;
        
        // Mock repository response
        when(favoriteRepository.existsByUserIdAndProductId(userId, productId)).thenReturn(false);

        // Call the method under test
        favoriteService.addProductToFavorites(userId, productId);

        // Verify that save was called
        verify(favoriteRepository, times(1)).save(any(FavoriteMaster.class));
    }

    @Test
    void testAddProductToFavorites_AlreadyInFavorites() {
        int userId = 1;
        int productId = 100;
        
        // Mock repository response
        when(favoriteRepository.existsByUserIdAndProductId(userId, productId)).thenReturn(true);

        // Call the method under test
        favoriteService.addProductToFavorites(userId, productId);

        // Verify that save was NOT called
        verify(favoriteRepository, never()).save(any(FavoriteMaster.class));
    }

    @Test
    void testGetFavoritesByUserId_Success() {
        int userId = 1;

        // Prepare mock data
        List<FavoriteMaster> favorites = new ArrayList<>();
        FavoriteMaster favorite = new FavoriteMaster();
        favorites.add(favorite);

        // Mock repository response
        when(favoriteRepository.findFavoritesByUserId(userId)).thenReturn(favorites);

        // Call the method under test
        List<FavoriteMaster> result = favoriteService.getFavoritesByUserId(userId);

        // Verify the result
        assertEquals(1, result.size());
    }

    @Test
    void testRemoveProductFromFavorites_Success() {
        int userId = 1;
        int productId = 100;

        // Call the method under test
        favoriteService.removeProductFromFavorites(userId, productId);

        // Verify that delete was called
        verify(favoriteRepository, times(1)).deleteFavoriteByUserIdAndProductId(userId, productId);
    }

    @Test
    void testMoveProductToCart_ProductNotInCart() {
        int userId = 1;
        int productId = 100;
        UserMaster user = new UserMaster();
        user.setUserId(userId);
        ProductMaster product = new ProductMaster();
        product.setProductId(productId);

        // Mock the cartService response
        when(cartService.getCartItemForUserAndProduct(user, product)).thenReturn(null);

        // Call the method under test
        favoriteService.moveProductToCart(userId, productId);

        // Verify that the product was added to cart
        verify(cartService, times(1)).addProductToCart(user, product, 1);
        // Verify that the product was removed from favorites
        verify(favoriteRepository, times(1)).deleteFavoriteByUserIdAndProductId(userId, productId);
    }

    @Test
    void testMoveProductToCart_ProductAlreadyInCart() {
        int userId = 1;
        int productId = 100;
        UserMaster user = new UserMaster();
        user.setUserId(userId);
        ProductMaster product = new ProductMaster();
        product.setProductId(productId);

        // Mock the cartService response to return an existing item
        CartMaster existingCartItem = new CartMaster();
        when(cartService.getCartItemForUserAndProduct(user, product)).thenReturn(existingCartItem);

        // Call the method under test
        favoriteService.moveProductToCart(userId, productId);

        // Verify that the product quantity was updated
        verify(cartService, times(1)).updateProductQuantity(user, product, 1);
        // Verify that the product was removed from favorites
        verify(favoriteRepository, times(1)).deleteFavoriteByUserIdAndProductId(userId, productId);
    }

    @Test
    void testIsProductInWishlist_True() {
        int userId = 1;
        int productId = 100;

        // Mock repository response
        when(favoriteRepository.existsByUserIdAndProductId(userId, productId)).thenReturn(true);

        // Call the method under test
        boolean result = favoriteService.isProductInWishlist(userId, productId);

        // Verify the result
        assertTrue(result);
    }

    @Test
    void testIsProductInWishlist_False() {
        int userId = 1;
        int productId = 100;

        // Mock repository response
        when(favoriteRepository.existsByUserIdAndProductId(userId, productId)).thenReturn(false);

        // Call the method under test
        boolean result = favoriteService.isProductInWishlist(userId, productId);

        // Verify the result
        assertFalse(result);
    }
}
