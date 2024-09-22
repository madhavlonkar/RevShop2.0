package com.revshop.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revshop.dao.impl.PasswordResetTokenDAOIMPL;
import com.revshop.master.PasswordResetTokenMaster;

import jakarta.persistence.EntityManager;

class PasswordResetTokenDAOIMPLTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Session session;

    @Mock
    private Query<PasswordResetTokenMaster> tokenQuery; // Mock the correct type of Query

    @InjectMocks
    private PasswordResetTokenDAOIMPL passwordResetTokenDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(entityManager.unwrap(Session.class)).thenReturn(session);
    }

    @Test
    void testSaveToken_Success() {
        PasswordResetTokenMaster token = new PasswordResetTokenMaster();
        token.setToken("abc123");

        // Call the saveToken method
        passwordResetTokenDAO.saveToken(token);

        // Verify that the session.saveOrUpdate method was called
        verify(session, times(1)).saveOrUpdate(token);
    }

    @Test
    void testFindByToken_Success() {
        String tokenValue = "abc123";
        PasswordResetTokenMaster token = new PasswordResetTokenMaster();
        token.setToken(tokenValue);

        // Mock the Query object and its behavior
        when(session.createQuery("from PasswordResetTokenMaster where token = :token", PasswordResetTokenMaster.class))
                .thenReturn(tokenQuery);
        when(tokenQuery.setParameter("token", tokenValue)).thenReturn(tokenQuery);
        when(tokenQuery.uniqueResult()).thenReturn(token);

        // Call the findByToken method
        PasswordResetTokenMaster result = passwordResetTokenDAO.findByToken(tokenValue);

        // Verify the result and method interactions
        assertEquals(token, result);
        verify(session, times(1)).createQuery("from PasswordResetTokenMaster where token = :token", PasswordResetTokenMaster.class);
        verify(tokenQuery, times(1)).setParameter("token", tokenValue);
        verify(tokenQuery, times(1)).uniqueResult();
    }

    @Test
    void testDeleteToken_Success() {
        PasswordResetTokenMaster token = new PasswordResetTokenMaster();
        token.setToken("abc123");

        // Call the deleteToken method
        passwordResetTokenDAO.deleteToken(token);

        // Verify that the session.delete method was called
        verify(session, times(1)).delete(token);
    }

    
}
