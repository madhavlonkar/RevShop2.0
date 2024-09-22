package com.revshop.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revshop.dao.CartDAO;
import com.revshop.dto.CartItemDTO;
import com.revshop.master.CartMaster;
import com.revshop.master.ProductMaster;
import com.revshop.master.UserMaster;
import com.revshop.service.impl.CartServiceImpl;

public class CartServiceImplTest {

    @Mock
    private CartDAO cartDAO;

    @InjectMocks
    private CartServiceImpl cartService;

    private UserMaster user;
    private ProductMaster product;
    private CartMaster cartMaster;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks

        user = new UserMaster();
        user.setUserId(1);

        product = new ProductMaster();
        product.setProductId(1);
        product.setProductName("Test Product");
        product.setProductStock(10);
        product.setProductPrice(100.0);

        cartMaster = new CartMaster();
        cartMaster.setCartId(1);
        cartMaster.setUser(user);
        cartMaster.setProduct(product);
        cartMaster.setQuantity(2);
    }

    @Test
    public void testAddProductToCart_NewProduct() {
        // Mocking
        when(cartDAO.findByUserAndProduct(user, product)).thenReturn(null);
        when(cartDAO.saveOrUpdateCart(any(CartMaster.class))).thenReturn(cartMaster);

        // Call the method
        CartMaster result = cartService.addProductToCart(user, product, 2);

        // Verify and assertions
        verify(cartDAO, times(1)).saveOrUpdateCart(any(CartMaster.class));
        assertNotNull(result);
        assertEquals(2, result.getQuantity());
        assertEquals(product, result.getProduct());
        assertEquals(user, result.getUser());
    }

    @Test
    public void testAddProductToCart_ExistingProduct() {
        // Mocking
        when(cartDAO.findByUserAndProduct(user, product)).thenReturn(cartMaster);
        when(cartDAO.saveOrUpdateCart(any(CartMaster.class))).thenReturn(cartMaster);

        // Call the method
        CartMaster result = cartService.addProductToCart(user, product, 3);

        // Verify and assertions
        verify(cartDAO, times(1)).saveOrUpdateCart(any(CartMaster.class));
        assertNotNull(result);
        assertEquals(5, result.getQuantity()); // Existing 2 + new 3 = 5
    }

    @Test
    public void testGetCartItemsForUser() {
        // Mocking
        List<CartMaster> cartList = Arrays.asList(cartMaster);
        when(cartDAO.findByUser(user)).thenReturn(cartList);

        // Call the method
        List<CartMaster> result = cartService.getCartItemsForUser(user);

        // Verify and assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(cartMaster, result.get(0));
    }

    @Test
    public void testRemoveCartItem_ExistingItem() {
        // Mocking
        when(cartDAO.findById(1)).thenReturn(cartMaster);

        // Call the method
        boolean result = cartService.removeCartItem(1);

        // Verify and assertions
        verify(cartDAO, times(1)).delete(cartMaster);
        assertTrue(result);
    }

    @Test
    public void testRemoveCartItem_NonExistingItem() {
        // Mocking
        when(cartDAO.findById(1)).thenReturn(null);

        // Call the method
        boolean result = cartService.removeCartItem(1);

        // Verify and assertions
        assertFalse(result);
        verify(cartDAO, never()).delete(any(CartMaster.class));
    }

    @Test
    public void testUpdateProductQuantity_IncreaseQuantity() {
        // Mocking
        when(cartDAO.findByUserAndProduct(user, product)).thenReturn(cartMaster);
        when(cartDAO.saveOrUpdateCart(cartMaster)).thenReturn(cartMaster);

        // Call the method
        CartMaster result = cartService.updateProductQuantity(user, product, 3);

        // Verify and assertions
        assertNotNull(result);
        assertEquals(5, result.getQuantity()); // Existing 2 + 3
        verify(cartDAO, times(1)).saveOrUpdateCart(cartMaster);
    }

    @Test
    public void testUpdateProductQuantity_RemoveItemWhenQuantityZero() {
        // Mocking
        when(cartDAO.findByUserAndProduct(user, product)).thenReturn(cartMaster);

        // Call the method
        CartMaster result = cartService.updateProductQuantity(user, product, -2);

        // Verify and assertions
        assertNull(result);
        verify(cartDAO, times(1)).delete(cartMaster); // Item should be removed
    }

    @Test
    public void testUpdateProductQuantity_ExceedsStock() {
        // Mocking
        when(cartDAO.findByUserAndProduct(user, product)).thenReturn(cartMaster);

        // Call the method
        CartMaster result = cartService.updateProductQuantity(user, product, 10);

        // Verify and assertions
        assertNull(result); // No update should be made
        verify(cartDAO, never()).saveOrUpdateCart(any(CartMaster.class));
    }

    @Test
    public void testConvertToDTO() {
        List<CartMaster> cartItems = Arrays.asList(cartMaster);

        // Call the method
        List<CartItemDTO> dtoList = cartService.convertToDTO(cartItems);

        // Assertions
        assertNotNull(dtoList);
        assertEquals(1, dtoList.size());
        CartItemDTO dto = dtoList.get(0);
        assertEquals(cartMaster.getCartId(), dto.getCartId());
        assertEquals(cartMaster.getProduct().getProductId(), dto.getProductId());
        assertEquals(cartMaster.getQuantity(), dto.getQuantity());
    }
}
