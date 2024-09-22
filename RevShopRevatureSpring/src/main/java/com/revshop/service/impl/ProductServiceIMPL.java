package com.revshop.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revshop.dao.ProductDAO;
import com.revshop.master.ProductMaster;
import com.revshop.service.ProductService;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceIMPL implements ProductService {

    @Autowired
    private ProductDAO productDAO;

    @Override
    @Transactional
    public void saveProduct(ProductMaster product) {
        productDAO.save(product);
    }

    @Override
    @Transactional
    public List<ProductMaster> getProductsBySellerId(int sellerId) {
        return productDAO.findProductsBySellerId(sellerId);  // Delegate call to the DAO
    }

    @Override
    @Transactional
    public ProductMaster getProductById(int productId) {
        return productDAO.findById(productId);  // Call DAO to fetch product by ID
    }

    @Override
    @Transactional
    public boolean updateProduct(ProductMaster product) {
        return productDAO.update(product);  // Call DAO to update product
    }

    @Override
    @Transactional
    public void deleteProductById(int productId) {
        productDAO.deleteById(productId);  // Call DAO to delete product by ID
    }
    
    @Override
    @Transactional
    public List<ProductMaster> getAllProducts() {
        return productDAO.findAll();
    }

    @Override
    public List<ProductMaster> getProductsByCategory(String category) {
        return productDAO.findByCategory(category);
    }

    @Override
    public List<ProductMaster> searchProductsQuery(String searchQuery) {
        return productDAO.searchProducts(searchQuery);
    }
    
    @Override
    @Transactional
    public List<ProductMaster> getLowStockProductsBySellerId(int sellerId) {
        return productDAO.findLowStockProductsBySellerId(sellerId);
    }

    public List<ProductMaster> getProductsBySellerAndCategory(int sellerId, String category) {
        // Query to get products based on sellerId and category
        return productDAO.findProductsBySellerAndCategory(sellerId, category);
    }

    public List<ProductMaster> searchProductsBySellerAndQuery(int sellerId, String query) {
        // Query to get products based on sellerId and a search query
        return productDAO.searchProductsBySellerAndQuery(sellerId, query);
    }

}
