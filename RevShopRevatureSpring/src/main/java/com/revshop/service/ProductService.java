package com.revshop.service;

import java.util.List;

import com.revshop.master.ProductMaster;

public interface ProductService {

	void saveProduct(ProductMaster product);

	List<ProductMaster> getProductsBySellerId(int userId);

	ProductMaster getProductById(int productId);

	boolean updateProduct(ProductMaster product);

	void deleteProductById(int productId);

	List<ProductMaster> getAllProducts();

	List<ProductMaster> getProductsByCategory(String category);

	List<ProductMaster> searchProductsQuery(String searchQuery);

	List<ProductMaster> getLowStockProductsBySellerId(int sellerId);

	List<ProductMaster> searchProductsBySellerAndQuery(int userId, String searchQuery);

	List<ProductMaster> getProductsBySellerAndCategory(int userId, String category);

}
