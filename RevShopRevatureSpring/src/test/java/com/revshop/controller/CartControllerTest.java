package com.revshop.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.revshop.dto.CartItemDTO;
import com.revshop.master.CartMaster;
import com.revshop.master.LoginMaster;
import com.revshop.master.ProductMaster;
import com.revshop.master.UserMaster;
import com.revshop.service.CartService;
import com.revshop.service.LoginService;
import com.revshop.service.ProductService;

public class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private ProductService productService;

    @Mock
    private LoginService loginService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CartController cartController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    void testAddToCart_Success() throws Exception {
        LoginMaster loggedInUser = new LoginMaster();
        UserMaster user = new UserMaster();
        loggedInUser.setUserId(user);
        ProductMaster product = new ProductMaster();
        product.setProductId(1);
        product.setProductStock(10);

        when(loginService.getLoggedInUserDetails()).thenReturn(loggedInUser);
        when(productService.getProductById(1)).thenReturn(product);
        when(cartService.addProductToCart(user, product, 1)).thenReturn(new CartMaster());

        mockMvc.perform(post("/cart/add")
                .param("productId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/view"));

        verify(cartService, times(1)).addProductToCart(user, product, 1);
    }

    @Test
    void testAddToCart_ProductNotFound() throws Exception {
        when(productService.getProductById(1)).thenReturn(null);

        mockMvc.perform(post("/cart/add")
                .param("productId", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("errorMessage", "Product not found."))
                .andExpect(view().name("error-page"));

        verify(cartService, never()).addProductToCart(any(), any(), anyInt());
    }

    @Test
    void testAddToCart_OutOfStock() throws Exception {
        ProductMaster product = new ProductMaster();
        product.setProductStock(0);
        when(productService.getProductById(1)).thenReturn(product);

        mockMvc.perform(post("/cart/add")
                .param("productId", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("errorMessage", "Product is out of stock."))
                .andExpect(view().name("error-page"));

        verify(cartService, never()).addProductToCart(any(), any(), anyInt());
    }

   

    @Test
    void testViewCart_UserNotLoggedIn() throws Exception {
        when(loginService.getLoggedInUserDetails()).thenReturn(null);

        mockMvc.perform(get("/cart/view"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("errorMessage", "User not logged in."))
                .andExpect(view().name("error-page"));

        verify(restTemplate, never()).exchange(anyString(), eq(HttpMethod.GET), any(), any(ParameterizedTypeReference.class));
    }

    @Test
    void testRemoveFromCart_Success() throws Exception {
        mockMvc.perform(post("/cart/remove")
                .param("cartId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/view"));

        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.DELETE), any(), eq(Void.class));
    }

    @Test
    void testRemoveFromCart_Error() throws Exception {
        doThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "Not Found", HttpHeaders.EMPTY, null, null))
                .when(restTemplate).exchange(anyString(), eq(HttpMethod.DELETE), any(), eq(Void.class));

        mockMvc.perform(post("/cart/remove")
                .param("cartId", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("errorMessage", "Error removing item: 404 NOT_FOUND"))
                .andExpect(view().name("error-page"));
    }

    @Test
    void testUpdateCartQuantity_Increase() throws Exception {
        LoginMaster loggedInUser = new LoginMaster();
        UserMaster user = new UserMaster();
        loggedInUser.setUserId(user);
        ProductMaster product = new ProductMaster();
        product.setProductId(1);

        when(loginService.getLoggedInUserDetails()).thenReturn(loggedInUser);
        when(productService.getProductById(1)).thenReturn(product);
        when(cartService.updateProductQuantity(user, product, 1)).thenReturn(new CartMaster());

        mockMvc.perform(post("/cart/update")
                .param("productId", "1")
                .param("action", "increase"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/view"));

        verify(cartService, times(1)).updateProductQuantity(user, product, 1);
    }

    @Test
    void testUpdateCartQuantity_Decrease() throws Exception {
        LoginMaster loggedInUser = new LoginMaster();
        UserMaster user = new UserMaster();
        loggedInUser.setUserId(user);
        ProductMaster product = new ProductMaster();
        product.setProductId(1);

        when(loginService.getLoggedInUserDetails()).thenReturn(loggedInUser);
        when(productService.getProductById(1)).thenReturn(product);
        when(cartService.updateProductQuantity(user, product, -1)).thenReturn(null);

        mockMvc.perform(post("/cart/update")
                .param("productId", "1")
                .param("action", "decrease"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/view"));

        verify(cartService, times(1)).updateProductQuantity(user, product, -1);
    }
}
