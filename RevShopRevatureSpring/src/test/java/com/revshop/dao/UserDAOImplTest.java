package com.revshop.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revshop.dao.impl.UserDAOImpl;
import com.revshop.master.UserMaster;

import jakarta.persistence.EntityManager;

public class UserDAOImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Session session;

    @InjectMocks
    private UserDAOImpl userDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(entityManager.unwrap(Session.class)).thenReturn(session);
    }

    @Test
    void testSaveUser_Success() {
        UserMaster user = new UserMaster();
        user.setUserId(1);
        user.setEmail("test@example.com");

        when(session.merge(user)).thenReturn(user);

        // Call the saveUser method
        UserMaster result = userDAO.saveUser(user);

        // Verify that the session.merge method was called and check the returned result
        assertEquals(user, result);
        verify(session, times(1)).merge(user);
    }

    @Test
    void testGetUserById_Success() {
        int userId = 1;
        UserMaster user = new UserMaster();
        user.setUserId(userId);
        user.setEmail("test@example.com");

        when(session.get(UserMaster.class, userId)).thenReturn(user);

        // Call the getUserById method
        UserMaster result = userDAO.getUserById(userId);

        // Verify that the session.get method was called and check the returned result
        assertEquals(user, result);
        verify(session, times(1)).get(UserMaster.class, userId);
    }
}
