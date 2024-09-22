package com.revshop.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.revshop.dao.ProductDAO;
import com.revshop.master.ProductMaster;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ProductDAOImpl implements ProductDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(ProductMaster product) {
        entityManager.persist(product);
    }

    @Override
    public List<ProductMaster> findProductsBySellerId(int sellerId) {
        String query = "FROM ProductMaster WHERE seller.userId = :sellerId";
        return entityManager.createQuery(query, ProductMaster.class)
                            .setParameter("sellerId", sellerId)
                            .getResultList();
    }

    @Override
    public ProductMaster findById(int productId) {
        return entityManager.find(ProductMaster.class, productId);
    }

    @Override
    public boolean update(ProductMaster product) {
        entityManager.merge(product);
        return true;
    }

    @Override
    public void deleteById(int productId) {
        ProductMaster product = entityManager.find(ProductMaster.class, productId);
        if (product != null) {
            entityManager.remove(product);
        }
    }

    @Override
    public List<ProductMaster> findAll() {
        return entityManager.createQuery("FROM ProductMaster", ProductMaster.class).getResultList();
    }

    @Override
    public void updateProduct(ProductMaster product) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(product);
    }

    @Override
    public List<ProductMaster> findByCategory(String category) {
        String query = "FROM ProductMaster WHERE productCategory = :category";
        return entityManager.createQuery(query, ProductMaster.class)
                            .setParameter("category", category)
                            .getResultList();
    }

    @Override
    public List<ProductMaster> searchProducts(String searchQuery) {
        String query = "FROM ProductMaster WHERE "
                     + "LOWER(productName) LIKE :searchQuery OR "
                     + "LOWER(productTags) LIKE :searchQuery OR "
                     + "LOWER(productDescription) LIKE :searchQuery OR "
                     + "LOWER(productBrand) LIKE :searchQuery";

        return entityManager.createQuery(query, ProductMaster.class)
                            .setParameter("searchQuery", "%" + searchQuery.toLowerCase() + "%")
                            .getResultList();
    }
    
    @Override
    public List<ProductMaster> findLowStockProductsBySellerId(int sellerId) {
        String query = "FROM ProductMaster WHERE seller.userId = :sellerId AND productStock < threshold";
        return entityManager.createQuery(query, ProductMaster.class)
                            .setParameter("sellerId", sellerId)
                            .getResultList();
    }

    // New method: Find products by seller and category
    @Override
    public List<ProductMaster> findProductsBySellerAndCategory(int sellerId, String category) {
        String query = "FROM ProductMaster WHERE seller.userId = :sellerId AND productCategory = :category";
        return entityManager.createQuery(query, ProductMaster.class)
                            .setParameter("sellerId", sellerId)
                            .setParameter("category", category)
                            .getResultList();
    }

    // New method: Search products by seller and query
    @Override
    public List<ProductMaster> searchProductsBySellerAndQuery(int sellerId, String searchQuery) {
        String query = "FROM ProductMaster WHERE seller.userId = :sellerId AND ("
                     + "LOWER(productName) LIKE :searchQuery OR "
                     + "LOWER(productTags) LIKE :searchQuery OR "
                     + "LOWER(productDescription) LIKE :searchQuery OR "
                     + "LOWER(productBrand) LIKE :searchQuery)";
        return entityManager.createQuery(query, ProductMaster.class)
                            .setParameter("sellerId", sellerId)
                            .setParameter("searchQuery", "%" + searchQuery.toLowerCase() + "%")
                            .getResultList();
    }
}
