package com.revshop.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.revshop.master.LoginMaster;
import com.revshop.master.ProductMaster;
import com.revshop.master.UserMaster;
import com.revshop.service.LoginService;
import com.revshop.service.ProductService;
import com.revshop.utility.EmailService;

public class HomeControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private LoginService loginService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private HomeController homeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
    }

    @Test
    void testShowHomePage_WithCategory() throws Exception {
        List<ProductMaster> products = new ArrayList<>();
        products.add(new ProductMaster());

        when(productService.getProductsByCategory("Electronics")).thenReturn(products);
        LoginMaster loggedInUser = new LoginMaster();
        when(loginService.getLoggedInUserDetails()).thenReturn(loggedInUser);

        mockMvc.perform(get("/home").param("category", "Electronics"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("selectedCategory"))
                .andExpect(view().name("index"));

        verify(productService, times(1)).getProductsByCategory("Electronics");
        verify(loginService, times(1)).getLoggedInUserDetails();
    }

    @Test
    void testShowHomePage_WithSearchQuery() throws Exception {
        List<ProductMaster> products = new ArrayList<>();
        products.add(new ProductMaster());

        when(productService.searchProductsQuery("Laptop")).thenReturn(products);
        LoginMaster loggedInUser = new LoginMaster();
        when(loginService.getLoggedInUserDetails()).thenReturn(loggedInUser);

        mockMvc.perform(get("/home").param("s", "Laptop"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("products"))
                .andExpect(view().name("index"));

        verify(productService, times(1)).searchProductsQuery("Laptop");
        verify(loginService, times(1)).getLoggedInUserDetails();
    }

    @Test
    void testShowSellerDashboard_WithCategory() throws Exception {
        List<ProductMaster> products = new ArrayList<>();
        products.add(new ProductMaster());

        LoginMaster loggedInUser = new LoginMaster();
        UserMaster user = new UserMaster();
        user.setUserId(1);
        loggedInUser.setUserId(user);

        when(loginService.getLoggedInUserDetails()).thenReturn(loggedInUser);
        when(productService.getProductsBySellerAndCategory(1, "Electronics")).thenReturn(products);
        when(productService.getLowStockProductsBySellerId(1)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/dashboard").param("category", "Electronics"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("lowStockProducts"))
                .andExpect(view().name("Seller/sellerDashboard"));

        verify(productService, times(1)).getProductsBySellerAndCategory(1, "Electronics");
        verify(productService, times(1)).getLowStockProductsBySellerId(1);
        verify(emailService, never()).sendLowStockAlert(anyString(), anyList());
    }

    @Test
    void testShowSellerDashboard_WithSearchQuery() throws Exception {
        List<ProductMaster> products = new ArrayList<>();
        products.add(new ProductMaster());

        LoginMaster loggedInUser = new LoginMaster();
        UserMaster user = new UserMaster();
        user.setUserId(1);
        loggedInUser.setUserId(user);

        when(loginService.getLoggedInUserDetails()).thenReturn(loggedInUser);
        when(productService.searchProductsBySellerAndQuery(1, "Laptop")).thenReturn(products);
        when(productService.getLowStockProductsBySellerId(1)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/dashboard").param("s", "Laptop"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("products"))
                .andExpect(view().name("Seller/sellerDashboard"));

        verify(productService, times(1)).searchProductsBySellerAndQuery(1, "Laptop");
        verify(productService, times(1)).getLowStockProductsBySellerId(1);
        verify(emailService, never()).sendLowStockAlert(anyString(), anyList());
    }

    @Test
    void testShowSellerDashboard_WithLowStockAlert() throws Exception {
        List<ProductMaster> products = new ArrayList<>();
        products.add(new ProductMaster());

        LoginMaster loggedInUser = new LoginMaster();
        UserMaster user = new UserMaster();
        user.setUserId(1);
        loggedInUser.setUserId(user);
        user.setEmail("seller@example.com");

        when(loginService.getLoggedInUserDetails()).thenReturn(loggedInUser);
        when(productService.getProductsBySellerAndCategory(1, "Electronics")).thenReturn(products);

        List<ProductMaster> lowStockProducts = new ArrayList<>();
        lowStockProducts.add(new ProductMaster());
        when(productService.getLowStockProductsBySellerId(1)).thenReturn(lowStockProducts);

        mockMvc.perform(get("/dashboard").param("category", "Electronics"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("lowStockProducts"))
                .andExpect(view().name("Seller/sellerDashboard"));

        verify(productService, times(1)).getProductsBySellerAndCategory(1, "Electronics");
        verify(productService, times(1)).getLowStockProductsBySellerId(1);
        verify(emailService, times(1)).sendLowStockAlert(eq("seller@example.com"), eq(lowStockProducts));
    }

    @Test
    void testShowSellerDashboard_UserNotLoggedIn() throws Exception {
        when(loginService.getLoggedInUserDetails()).thenReturn(null);

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("errorMessage", "User is not logged in."))
                .andExpect(view().name("error-page"));

        verify(loginService, times(1)).getLoggedInUserDetails();
        verify(productService, never()).getProductsBySellerAndCategory(anyInt(), anyString());
    }
}
