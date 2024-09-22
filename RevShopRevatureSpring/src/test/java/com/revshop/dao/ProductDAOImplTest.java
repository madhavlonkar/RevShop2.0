package com.revshop.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revshop.dao.impl.ProductDAOImpl;
import com.revshop.master.ProductMaster;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class ProductDAOImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Session session;

    @InjectMocks
    private ProductDAOImpl productDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveProduct() {
        ProductMaster product = new ProductMaster();
        productDAO.save(product);
        verify(entityManager, times(1)).persist(product);
    }

    @Test
    void testFindProductsBySellerId() {
        int sellerId = 1;
        List<ProductMaster> products = new ArrayList<>();
        TypedQuery<ProductMaster> query = mock(TypedQuery.class);

        when(entityManager.createQuery("FROM ProductMaster WHERE seller.userId = :sellerId", ProductMaster.class))
                .thenReturn(query);
        when(query.setParameter("sellerId", sellerId)).thenReturn(query);
        when(query.getResultList()).thenReturn(products);

        List<ProductMaster> result = productDAO.findProductsBySellerId(sellerId);
        assertEquals(products, result);
        verify(entityManager, times(1)).createQuery("FROM ProductMaster WHERE seller.userId = :sellerId", ProductMaster.class);
        verify(query, times(1)).setParameter("sellerId", sellerId);
        verify(query, times(1)).getResultList();
    }

    @Test
    void testFindById() {
        int productId = 1;
        ProductMaster product = new ProductMaster();

        when(entityManager.find(ProductMaster.class, productId)).thenReturn(product);

        ProductMaster result = productDAO.findById(productId);
        assertEquals(product, result);
        verify(entityManager, times(1)).find(ProductMaster.class, productId);
    }

    @Test
    void testUpdateProduct() {
        ProductMaster product = new ProductMaster();
        boolean result = productDAO.update(product);
        assertTrue(result);
        verify(entityManager, times(1)).merge(product);
    }

    @Test
    void testDeleteProductById() {
        int productId = 1;
        ProductMaster product = new ProductMaster();
        
        when(entityManager.find(ProductMaster.class, productId)).thenReturn(product);
        
        productDAO.deleteById(productId);
        verify(entityManager, times(1)).remove(product);
    }

    @Test
    void testFindAllProducts() {
        List<ProductMaster> products = new ArrayList<>();
        TypedQuery<ProductMaster> query = mock(TypedQuery.class);

        when(entityManager.createQuery("FROM ProductMaster", ProductMaster.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(products);

        List<ProductMaster> result = productDAO.findAll();
        assertEquals(products, result);
        verify(entityManager, times(1)).createQuery("FROM ProductMaster", ProductMaster.class);
        verify(query, times(1)).getResultList();
    }

    @Test
    void testUpdateProductWithSession() {
        ProductMaster product = new ProductMaster();
        when(entityManager.unwrap(Session.class)).thenReturn(session);

        productDAO.updateProduct(product);
        verify(session, times(1)).saveOrUpdate(product);
    }

    @Test
    void testFindProductsByCategory() {
        String category = "Electronics";
        List<ProductMaster> products = new ArrayList<>();
        TypedQuery<ProductMaster> query = mock(TypedQuery.class);

        when(entityManager.createQuery("FROM ProductMaster WHERE productCategory = :category", ProductMaster.class))
                .thenReturn(query);
        when(query.setParameter("category", category)).thenReturn(query);
        when(query.getResultList()).thenReturn(products);

        List<ProductMaster> result = productDAO.findByCategory(category);
        assertEquals(products, result);
        verify(entityManager, times(1)).createQuery("FROM ProductMaster WHERE productCategory = :category", ProductMaster.class);
        verify(query, times(1)).setParameter("category", category);
        verify(query, times(1)).getResultList();
    }

    @Test
    void testSearchProducts() {
        String searchQuery = "phone";
        List<ProductMaster> products = new ArrayList<>();
        TypedQuery<ProductMaster> query = mock(TypedQuery.class);

        String hqlQuery = "FROM ProductMaster WHERE "
                        + "LOWER(productName) LIKE :searchQuery OR "
                        + "LOWER(productTags) LIKE :searchQuery OR "
                        + "LOWER(productDescription) LIKE :searchQuery OR "
                        + "LOWER(productBrand) LIKE :searchQuery";

        when(entityManager.createQuery(hqlQuery, ProductMaster.class)).thenReturn(query);
        when(query.setParameter("searchQuery", "%" + searchQuery.toLowerCase() + "%")).thenReturn(query);
        when(query.getResultList()).thenReturn(products);

        List<ProductMaster> result = productDAO.searchProducts(searchQuery);
        assertEquals(products, result);
        verify(entityManager, times(1)).createQuery(hqlQuery, ProductMaster.class);
        verify(query, times(1)).setParameter("searchQuery", "%" + searchQuery.toLowerCase() + "%");
        verify(query, times(1)).getResultList();
    }

    @Test
    void testFindLowStockProductsBySellerId() {
        int sellerId = 1;
        List<ProductMaster> products = new ArrayList<>();
        TypedQuery<ProductMaster> query = mock(TypedQuery.class);

        String hqlQuery = "FROM ProductMaster WHERE seller.userId = :sellerId AND productStock < threshold";

        when(entityManager.createQuery(hqlQuery, ProductMaster.class)).thenReturn(query);
        when(query.setParameter("sellerId", sellerId)).thenReturn(query);
        when(query.getResultList()).thenReturn(products);

        List<ProductMaster> result = productDAO.findLowStockProductsBySellerId(sellerId);
        assertEquals(products, result);
        verify(entityManager, times(1)).createQuery(hqlQuery, ProductMaster.class);
        verify(query, times(1)).setParameter("sellerId", sellerId);
        verify(query, times(1)).getResultList();
    }

    @Test
    void testFindProductsBySellerAndCategory() {
        int sellerId = 1;
        String category = "Electronics";
        List<ProductMaster> products = new ArrayList<>();
        TypedQuery<ProductMaster> query = mock(TypedQuery.class);

        String hqlQuery = "FROM ProductMaster WHERE seller.userId = :sellerId AND productCategory = :category";

        when(entityManager.createQuery(hqlQuery, ProductMaster.class)).thenReturn(query);
        when(query.setParameter("sellerId", sellerId)).thenReturn(query);
        when(query.setParameter("category", category)).thenReturn(query);
        when(query.getResultList()).thenReturn(products);

        List<ProductMaster> result = productDAO.findProductsBySellerAndCategory(sellerId, category);
        assertEquals(products, result);
        verify(entityManager, times(1)).createQuery(hqlQuery, ProductMaster.class);
        verify(query, times(1)).setParameter("sellerId", sellerId);
        verify(query, times(1)).setParameter("category", category);
        verify(query, times(1)).getResultList();
    }

    @Test
    void testSearchProductsBySellerAndQuery() {
        int sellerId = 1;
        String searchQuery = "phone";
        List<ProductMaster> products = new ArrayList<>();
        TypedQuery<ProductMaster> query = mock(TypedQuery.class);

        String hqlQuery = "FROM ProductMaster WHERE seller.userId = :sellerId AND ("
                        + "LOWER(productName) LIKE :searchQuery OR "
                        + "LOWER(productTags) LIKE :searchQuery OR "
                        + "LOWER(productDescription) LIKE :searchQuery OR "
                        + "LOWER(productBrand) LIKE :searchQuery)";

        when(entityManager.createQuery(hqlQuery, ProductMaster.class)).thenReturn(query);
        when(query.setParameter("sellerId", sellerId)).thenReturn(query);
        when(query.setParameter("searchQuery", "%" + searchQuery.toLowerCase() + "%")).thenReturn(query);
        when(query.getResultList()).thenReturn(products);

        List<ProductMaster> result = productDAO.searchProductsBySellerAndQuery(sellerId, searchQuery);
        assertEquals(products, result);
        verify(entityManager, times(1)).createQuery(hqlQuery, ProductMaster.class);
        verify(query, times(1)).setParameter("sellerId", sellerId);
        verify(query, times(1)).setParameter("searchQuery", "%" + searchQuery.toLowerCase() + "%");
        verify(query, times(1)).getResultList();
    }
}
