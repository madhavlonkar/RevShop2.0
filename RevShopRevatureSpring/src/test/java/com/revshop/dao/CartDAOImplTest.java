package com.revshop.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revshop.dao.impl.CartDAOImpl;
import com.revshop.master.CartMaster;
import com.revshop.master.ProductMaster;
import com.revshop.master.UserMaster;

import jakarta.persistence.EntityManager;

public class CartDAOImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Session session;

    @Mock
    private Query<CartMaster> cartQuery;

    @InjectMocks
    private CartDAOImpl cartDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(entityManager.unwrap(Session.class)).thenReturn(session);
    }

    @Test
    void testFindByUserAndProduct_Success() {
        UserMaster user = new UserMaster();
        ProductMaster product = new ProductMaster();
        CartMaster cartItem = new CartMaster();
        List<CartMaster> cartList = new ArrayList<>();
        cartList.add(cartItem);

        String hql = "FROM CartMaster WHERE user = :user AND product = :product";
        when(entityManager.createQuery(hql, CartMaster.class)).thenReturn(cartQuery);
        when(cartQuery.setParameter("user", user)).thenReturn(cartQuery);
        when(cartQuery.setParameter("product", product)).thenReturn(cartQuery);
        when(cartQuery.getResultList()).thenReturn(cartList);

        CartMaster result = cartDAO.findByUserAndProduct(user, product);
        assertEquals(cartItem, result);
        verify(entityManager, times(1)).createQuery(hql, CartMaster.class);
        verify(cartQuery, times(1)).setParameter("user", user);
        verify(cartQuery, times(1)).setParameter("product", product);
        verify(cartQuery, times(1)).getResultList();
    }

    @Test
    void testFindByUser_Success() {
        UserMaster user = new UserMaster();
        List<CartMaster> cartList = new ArrayList<>();
        cartList.add(new CartMaster());

        String hql = "FROM CartMaster WHERE user = :user";
        when(entityManager.createQuery(hql, CartMaster.class)).thenReturn(cartQuery);
        when(cartQuery.setParameter("user", user)).thenReturn(cartQuery);
        when(cartQuery.getResultList()).thenReturn(cartList);

        List<CartMaster> result = cartDAO.findByUser(user);
        assertEquals(cartList, result);
        verify(entityManager, times(1)).createQuery(hql, CartMaster.class);
        verify(cartQuery, times(1)).setParameter("user", user);
        verify(cartQuery, times(1)).getResultList();
    }

    @Test
    void testFindById_Success() {
        int cartId = 1;
        CartMaster cartItem = new CartMaster();
        when(entityManager.find(CartMaster.class, cartId)).thenReturn(cartItem);

        CartMaster result = cartDAO.findById(cartId);
        assertEquals(cartItem, result);
        verify(entityManager, times(1)).find(CartMaster.class, cartId);
    }

    @Test
    void testDelete_Success() {
        CartMaster cartItem = new CartMaster();
        doNothing().when(entityManager).remove(cartItem);

        cartDAO.delete(cartItem);
        verify(entityManager, times(1)).remove(cartItem);
    }

    @Test
    void testSaveOrUpdateCart_Success() {
        CartMaster cartItem = new CartMaster();
        when(entityManager.merge(cartItem)).thenReturn(cartItem);

        CartMaster result = cartDAO.saveOrUpdateCart(cartItem);
        assertEquals(cartItem, result);
        verify(entityManager, times(1)).merge(cartItem);
    }

    
}
