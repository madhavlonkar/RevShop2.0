package com.revshop.dao;

import java.util.List;

import com.revshop.master.ProductMaster;

public interface ProductDAO {
    void save(ProductMaster product);

	List<ProductMaster> findProductsBySellerId(int sellerId);

	ProductMaster findById(int productId);

	boolean update(ProductMaster product);

	void deleteById(int productId);

	List<ProductMaster> findAll();
	
	 void updateProduct(ProductMaster product);

	List<ProductMaster> findByCategory(String category);

	List<ProductMaster> searchProducts(String searchQuery);

	List<ProductMaster> findLowStockProductsBySellerId(int sellerId);

	List<ProductMaster> findProductsBySellerAndCategory(int sellerId, String category);

	List<ProductMaster> searchProductsBySellerAndQuery(int sellerId, String searchQuery);
}
