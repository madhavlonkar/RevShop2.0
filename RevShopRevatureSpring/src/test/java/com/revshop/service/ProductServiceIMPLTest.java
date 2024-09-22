package com.revshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.revshop.dao.ProductDAO;
import com.revshop.master.ProductMaster;
import com.revshop.service.impl.ProductServiceIMPL;

public class ProductServiceIMPLTest {

    @Mock
    private ProductDAO productDAO;

    @InjectMocks
    private ProductServiceIMPL productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveProduct_Success() {
        ProductMaster product = new ProductMaster();
        product.setProductId(1);
        product.setProductName("Test Product");

        // Call the method
        productService.saveProduct(product);

        // Verify that the save method was called in the DAO
        verify(productDAO, times(1)).save(product);
    }

    @Test
    void testGetProductsBySellerId_Success() {
        int sellerId = 1;
        List<ProductMaster> products = List.of(new ProductMaster(), new ProductMaster());

        when(productDAO.findProductsBySellerId(sellerId)).thenReturn(products);

        // Call the method
        List<ProductMaster> result = productService.getProductsBySellerId(sellerId);

        // Assert that the returned list matches the mocked data
        assertEquals(2, result.size());
        verify(productDAO, times(1)).findProductsBySellerId(sellerId);
    }

    @Test
    void testGetProductById_Success() {
        int productId = 1;
        ProductMaster product = new ProductMaster();
        product.setProductId(productId);

        when(productDAO.findById(productId)).thenReturn(product);

        // Call the method
        ProductMaster result = productService.getProductById(productId);

        // Assert that the returned product matches the mocked data
        assertEquals(productId, result.getProductId());
        verify(productDAO, times(1)).findById(productId);
    }

    @Test
    void testUpdateProduct_Success() {
        ProductMaster product = new ProductMaster();
        product.setProductId(1);
        product.setProductName("Updated Product");

        when(productDAO.update(product)).thenReturn(true);

        // Call the method
        boolean result = productService.updateProduct(product);

        // Assert the update result
        assertTrue(result);
        verify(productDAO, times(1)).update(product);
    }

    @Test
    void testDeleteProductById_Success() {
        int productId = 1;

        // Call the method
        productService.deleteProductById(productId);

        // Verify that the delete method was called in the DAO
        verify(productDAO, times(1)).deleteById(productId);
    }

    @Test
    void testGetAllProducts_Success() {
        List<ProductMaster> products = new ArrayList<>();
        products.add(new ProductMaster());
        products.add(new ProductMaster());

        when(productDAO.findAll()).thenReturn(products);

        // Call the method
        List<ProductMaster> result = productService.getAllProducts();

        // Assert that the returned list matches the mocked data
        assertEquals(2, result.size());
        verify(productDAO, times(1)).findAll();
    }

    @Test
    void testGetProductsByCategory_Success() {
        String category = "Electronics";
        List<ProductMaster> products = List.of(new ProductMaster(), new ProductMaster());

        when(productDAO.findByCategory(category)).thenReturn(products);

        // Call the method
        List<ProductMaster> result = productService.getProductsByCategory(category);

        // Assert that the returned list matches the mocked data
        assertEquals(2, result.size());
        verify(productDAO, times(1)).findByCategory(category);
    }

    @Test
    void testSearchProductsQuery_Success() {
        String query = "Test";
        List<ProductMaster> products = List.of(new ProductMaster(), new ProductMaster());

        when(productDAO.searchProducts(query)).thenReturn(products);

        // Call the method
        List<ProductMaster> result = productService.searchProductsQuery(query);

        // Assert that the returned list matches the mocked data
        assertEquals(2, result.size());
        verify(productDAO, times(1)).searchProducts(query);
    }

    @Test
    void testGetLowStockProductsBySellerId_Success() {
        int sellerId = 1;
        List<ProductMaster> products = List.of(new ProductMaster(), new ProductMaster());

        when(productDAO.findLowStockProductsBySellerId(sellerId)).thenReturn(products);

        // Call the method
        List<ProductMaster> result = productService.getLowStockProductsBySellerId(sellerId);

        // Assert that the returned list matches the mocked data
        assertEquals(2, result.size());
        verify(productDAO, times(1)).findLowStockProductsBySellerId(sellerId);
    }

    @Test
    void testGetProductsBySellerAndCategory_Success() {
        int sellerId = 1;
        String category = "Electronics";
        List<ProductMaster> products = List.of(new ProductMaster(), new ProductMaster());

        when(productDAO.findProductsBySellerAndCategory(sellerId, category)).thenReturn(products);

        // Call the method
        List<ProductMaster> result = productService.getProductsBySellerAndCategory(sellerId, category);

        // Assert that the returned list matches the mocked data
        assertEquals(2, result.size());
        verify(productDAO, times(1)).findProductsBySellerAndCategory(sellerId, category);
    }

    @Test
    void testSearchProductsBySellerAndQuery_Success() {
        int sellerId = 1;
        String query = "Test";
        List<ProductMaster> products = List.of(new ProductMaster(), new ProductMaster());

        when(productDAO.searchProductsBySellerAndQuery(sellerId, query)).thenReturn(products);

        // Call the method
        List<ProductMaster> result = productService.searchProductsBySellerAndQuery(sellerId, query);

        // Assert that the returned list matches the mocked data
        assertEquals(2, result.size());
        verify(productDAO, times(1)).searchProductsBySellerAndQuery(sellerId, query);
    }
}
