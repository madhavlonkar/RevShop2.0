package com.revshop.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revshop.dao.CartDAO;
import com.revshop.dao.OrderDAO;
import com.revshop.dao.ProductDAO;
import com.revshop.master.CartMaster;
import com.revshop.master.OrderMaster;
import com.revshop.master.ProductMaster;
import com.revshop.master.UserMaster;
import com.revshop.repo.TransactionDAO;
import com.revshop.service.impl.CheckoutServiceImpl;
import com.revshop.utility.EmailService;

public class CheckoutServiceImplTest {

    @Mock
    private OrderDAO orderDAO;

    @Mock
    private TransactionDAO transactionDAO;

    @Mock
    private CartDAO cartDAO;

    @Mock
    private ProductDAO productDAO;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private CheckoutServiceImpl checkoutService;

    private UserMaster user;
    private ProductMaster product;
    private CartMaster cartItem;
    private OrderMaster orderMaster;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        user = new UserMaster();
        user.setUserId(1);
        user.setEmail("test@example.com");

        // Create a seller for the product
        UserMaster seller = new UserMaster();
        seller.setUserId(2);
        seller.setEmail("seller@example.com");

        product = new ProductMaster();
        product.setProductId(1);
        product.setProductName("Test Product");
        product.setProductPrice(100.0);
        product.setProductStock(10);
        product.setProductDiscount(10);  // 10% discount
        product.setSeller(seller);  // Associate seller with product

        cartItem = new CartMaster();
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);  // 2 items in the cart

        orderMaster = new OrderMaster();
        orderMaster.setOrderId(1);
        orderMaster.setUser(user);
        orderMaster.setTotalAmount(180.0);  // Total after discount
    }

    @Test
    public void testPlaceOrder_Successful() {
        // Mocking the cart items for the user
        when(cartDAO.findByUser(user)).thenReturn(Arrays.asList(cartItem));

        // Mock saving order and returning saved order
        when(orderDAO.saveOrder(any(OrderMaster.class))).thenReturn(orderMaster);

        // Call placeOrder method
        boolean result = checkoutService.placeOrder(user, "payment123", "123 Test St", "Test City", "Test State", "12345");

        // Verify interactions
        verify(cartDAO, times(1)).findByUser(user);
        verify(orderDAO, times(1)).saveOrder(any(OrderMaster.class));
        verify(orderDAO, times(1)).saveOrderDetails(any());
        verify(productDAO, times(1)).updateProduct(product);
        verify(transactionDAO, times(1)).save(any());
        verify(cartDAO, times(1)).clearCartByUser(user);
        verify(emailService, times(1)).sendEmailOnOrderPlaced(eq(user.getEmail()), any(), eq(180.0));
        verify(emailService, times(1)).sendEmailOnOrderReceived(eq(product.getSeller().getEmail()), eq(product), eq(2), eq(user));

        // Assert that the order was placed successfully
        assertTrue(result);
    }

    @Test
    public void testPlaceOrder_EmptyCart() {
        // Mocking an empty cart
        when(cartDAO.findByUser(user)).thenReturn(null);

        // Call placeOrder method
        boolean result = checkoutService.placeOrder(user, "payment123", "123 Test St", "Test City", "Test State", "12345");

        // Verify interactions
        verify(cartDAO, times(1)).findByUser(user);
        verify(orderDAO, never()).saveOrder(any());
        verify(transactionDAO, never()).save(any());

        // Assert that the order was not placed
        assertFalse(result);
    }

    @Test
    public void testPlaceOrder_ProductStockUpdate() {
        // Mocking the cart items for the user
        when(cartDAO.findByUser(user)).thenReturn(Arrays.asList(cartItem));
        when(orderDAO.saveOrder(any(OrderMaster.class))).thenReturn(orderMaster);

        // Call placeOrder method
        checkoutService.placeOrder(user, "payment123", "123 Test St", "Test City", "Test State", "12345");

        // Verify product stock update
        verify(productDAO, times(1)).updateProduct(product);
        assertEquals(8, product.getProductStock());  // 10 stock - 2 quantity = 8 stock
    }

    @Test
    public void testPlaceOrder_EmailNotifications() {
        // Mocking the cart items for the user
        when(cartDAO.findByUser(user)).thenReturn(Arrays.asList(cartItem));
        when(orderDAO.saveOrder(any(OrderMaster.class))).thenReturn(orderMaster);

        // Call placeOrder method
        checkoutService.placeOrder(user, "payment123", "123 Test St", "Test City", "Test State", "12345");

        // Verify email notifications
        verify(emailService, times(1)).sendEmailOnOrderPlaced(eq(user.getEmail()), eq(orderMaster), anyDouble());
        verify(emailService, times(1)).sendEmailOnOrderReceived(eq(product.getSeller().getEmail()), eq(product), eq(cartItem.getQuantity()), eq(user));
    }
}
