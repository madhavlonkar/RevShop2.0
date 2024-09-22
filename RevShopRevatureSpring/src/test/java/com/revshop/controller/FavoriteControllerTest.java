package com.revshop.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import com.revshop.master.LoginMaster;
import com.revshop.master.ProductMaster;
import com.revshop.master.UserMaster;
import com.revshop.service.FavoriteService;
import com.revshop.service.LoginService;
import com.revshop.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class FavoriteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FavoriteService favoriteService;

    @Mock
    private LoginService loginService;

    @Mock
    private ProductService productService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private FavoriteController favoriteController;

    @Mock
    private Model model;

    @Mock
    private HttpServletRequest request;

    private LoginMaster mockLoggedInUser;
    private UserMaster mockUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(favoriteController).build();

        mockUser = new UserMaster();
        mockUser.setUserId(1);

        mockLoggedInUser = new LoginMaster();
        mockLoggedInUser.setUserId(mockUser);
    }

    @Test
    public void testAddProductToFavorites_UserNotLoggedIn() throws Exception {
        when(loginService.getLoggedInUserDetails()).thenReturn(null);

        String view = favoriteController.addProductToFavorites(1, model);

        assertEquals("error-page", view);
        verify(model).addAttribute("errorMessage", "User is not logged in.");
    }

    @Test
    public void testAddProductToFavorites_ProductNotFound() throws Exception {
        when(loginService.getLoggedInUserDetails()).thenReturn(mockLoggedInUser);
        when(productService.getProductById(1)).thenReturn(null);

        String view = favoriteController.addProductToFavorites(1, model);

        assertEquals("error-page", view);
        verify(model).addAttribute("errorMessage", "Product not found.");
    }

    @Test
    public void testAddProductToFavorites_AlreadyInWishlist() throws Exception {
        ProductMaster mockProduct = new ProductMaster();
        when(loginService.getLoggedInUserDetails()).thenReturn(mockLoggedInUser);
        when(productService.getProductById(1)).thenReturn(mockProduct);
        when(favoriteService.isProductInWishlist(1, 1)).thenReturn(true);

        String view = favoriteController.addProductToFavorites(1, model);

        assertEquals("redirect:/favorites/list", view);
        verify(model).addAttribute("message", "Product already in wishlist.");
    }

    @Test
    public void testAddProductToFavorites_Success() throws Exception {
        ProductMaster mockProduct = new ProductMaster();
        when(loginService.getLoggedInUserDetails()).thenReturn(mockLoggedInUser);
        when(productService.getProductById(1)).thenReturn(mockProduct);
        when(favoriteService.isProductInWishlist(1, 1)).thenReturn(false);

        String view = favoriteController.addProductToFavorites(1, model);

        assertEquals("redirect:/favorites/list", view);
        verify(favoriteService).addProductToFavorites(1, 1);
        verify(model).addAttribute("message", "Product added to wishlist.");
    }

    @Test
    public void testGetFavoritesByUser_UserNotLoggedIn() throws Exception {
        when(loginService.getLoggedInUserDetails()).thenReturn(null);

        String view = favoriteController.getFavoritesByUser(request, model);

        assertEquals("error-page", view);
        verify(model).addAttribute("errorMessage", "User is not logged in.");
    }

    @Test
    public void testGetFavoritesByUser_Success() throws Exception {
        when(loginService.getLoggedInUserDetails()).thenReturn(mockLoggedInUser);
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class)))
            .thenReturn(new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK));

        String view = favoriteController.getFavoritesByUser(request, model);

        assertEquals("favorites-view", view);
        verify(model).addAttribute("message", "No favorite products found.");
    }

    @Test
    public void testRemoveProductFromFavorites_UserNotLoggedIn() throws Exception {
        when(loginService.getLoggedInUserDetails()).thenReturn(null);

        String view = favoriteController.removeProductFromFavorites(1, model);

        assertEquals("error-page", view);
        verify(model).addAttribute("errorMessage", "User is not logged in.");
    }

    @Test
    public void testRemoveProductFromFavorites_Success() throws Exception {
        when(loginService.getLoggedInUserDetails()).thenReturn(mockLoggedInUser);

        String view = favoriteController.removeProductFromFavorites(1, model);

        assertEquals("redirect:/favorites/list", view);
        verify(favoriteService).removeProductFromFavorites(1, 1);
    }

    @Test
    public void testMoveProductToCart_UserNotLoggedIn() throws Exception {
        when(loginService.getLoggedInUserDetails()).thenReturn(null);

        String view = favoriteController.moveProductToCart(1, model);

        assertEquals("error-page", view);
        verify(model).addAttribute("errorMessage", "User is not logged in.");
    }

    @Test
    public void testMoveProductToCart_ProductNotInWishlist() throws Exception {
        when(loginService.getLoggedInUserDetails()).thenReturn(mockLoggedInUser);
        when(favoriteService.isProductInWishlist(1, 1)).thenReturn(false);

        String view = favoriteController.moveProductToCart(1, model);

        assertEquals("error-page", view);
        verify(model).addAttribute("errorMessage", "Product not in wishlist.");
    }

    @Test
    public void testMoveProductToCart_Success() throws Exception {
        when(loginService.getLoggedInUserDetails()).thenReturn(mockLoggedInUser);
        when(favoriteService.isProductInWishlist(1, 1)).thenReturn(true);

        String view = favoriteController.moveProductToCart(1, model);

        assertEquals("redirect:/cart/view", view);
        verify(favoriteService).moveProductToCart(1, 1);
    }

    @Test
    public void testIsProductInWishlist_UserNotLoggedIn() throws Exception {
        when(loginService.getLoggedInUserDetails()).thenReturn(null);

        ResponseEntity<Boolean> response = favoriteController.isProductInWishlist(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
    }

    @Test
    public void testIsProductInWishlist_Success() throws Exception {
        when(loginService.getLoggedInUserDetails()).thenReturn(mockLoggedInUser);
        when(favoriteService.isProductInWishlist(1, 1)).thenReturn(true);

        ResponseEntity<Boolean> response = favoriteController.isProductInWishlist(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }
}
