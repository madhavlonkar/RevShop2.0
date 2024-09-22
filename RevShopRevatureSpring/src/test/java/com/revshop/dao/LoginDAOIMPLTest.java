package com.revshop.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.revshop.dao.impl.LoginDAOIMPL;
import com.revshop.master.LoginMaster;
import com.revshop.master.UserMaster;

import jakarta.persistence.EntityManager;

public class LoginDAOIMPLTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Session session;

    @Mock
    private Query<LoginMaster> loginQuery;

    @Mock
    private Query<Long> countQuery;

    @InjectMocks
    private LoginDAOIMPL loginDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(entityManager.unwrap(Session.class)).thenReturn(session);
    }

    @Test
    void testInsert_Success() {
        LoginMaster user = new LoginMaster();
        when(session.merge(user)).thenReturn(user);

        boolean result = loginDAO.insert(user);
        assertTrue(result);
        verify(session, times(1)).merge(user);
    }

    @Test
    void testRetrieveAll_Success() {
        List<LoginMaster> loginList = new ArrayList<>();
        loginList.add(new LoginMaster());

        when(session.createQuery("from LoginMaster", LoginMaster.class)).thenReturn(loginQuery);
        when(loginQuery.getResultList()).thenReturn(loginList);

        List<LoginMaster> result = loginDAO.reterieveAll();
        assertEquals(loginList, result);
        verify(session, times(1)).createQuery("from LoginMaster", LoginMaster.class);
        verify(loginQuery, times(1)).getResultList();
    }

    @Test
    void testDelete_Success() {
        int userId = 1;
        UserMaster user = new UserMaster();
        when(session.get(UserMaster.class, userId)).thenReturn(user);

        loginDAO.delete(userId);
        verify(session, times(1)).get(UserMaster.class, userId);
        verify(session, times(1)).delete(user);
    }

    @Test
    void testFindByUsername_Success() {
        String username = "testUser";
        LoginMaster user = new LoginMaster();
        user.setUserName(username);

        when(session.createQuery("from LoginMaster where username = :username", LoginMaster.class)).thenReturn(loginQuery);
        when(loginQuery.setParameter("username", username)).thenReturn(loginQuery);
        when(loginQuery.getSingleResult()).thenReturn(user);

        LoginMaster result = loginDAO.findByUsername(username);
        assertEquals(user, result);
        verify(session, times(1)).createQuery("from LoginMaster where username = :username", LoginMaster.class);
        verify(loginQuery, times(1)).setParameter("username", username);
        verify(loginQuery, times(1)).getSingleResult();
    }

    @Test
    void testFindByEmail_Success() {
        String email = "test@example.com";
        List<LoginMaster> users = new ArrayList<>();
        LoginMaster user = new LoginMaster();
        user.setEmail(email);
        users.add(user);

        when(session.createQuery("from LoginMaster where email = :email", LoginMaster.class)).thenReturn(loginQuery);
        when(loginQuery.setParameter("email", email)).thenReturn(loginQuery);
        when(loginQuery.getResultList()).thenReturn(users);

        LoginMaster result = loginDAO.findByEmail(email);
        assertEquals(user, result);
        verify(session, times(1)).createQuery("from LoginMaster where email = :email", LoginMaster.class);
        verify(loginQuery, times(1)).setParameter("email", email);
        verify(loginQuery, times(1)).getResultList();
    }

    @Test
    void testFindByEmail_NoResult() {
        String email = "notfound@example.com";
        when(session.createQuery("from LoginMaster where email = :email", LoginMaster.class)).thenReturn(loginQuery);
        when(loginQuery.setParameter("email", email)).thenReturn(loginQuery);
        when(loginQuery.getResultList()).thenReturn(new ArrayList<>());

        LoginMaster result = loginDAO.findByEmail(email);
        assertNull(result);
        verify(session, times(1)).createQuery("from LoginMaster where email = :email", LoginMaster.class);
        verify(loginQuery, times(1)).setParameter("email", email);
        verify(loginQuery, times(1)).getResultList();
    }

    @Test
    void testExistsByUsername_True() {
        String username = "testUser";
        when(session.createQuery("select count(l) from LoginMaster l where l.userName = :username", Long.class)).thenReturn(countQuery);
        when(countQuery.setParameter("username", username)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(1L);

        boolean result = loginDAO.existsByUsername(username);
        assertTrue(result);
        verify(session, times(1)).createQuery("select count(l) from LoginMaster l where l.userName = :username", Long.class);
        verify(countQuery, times(1)).setParameter("username", username);
        verify(countQuery, times(1)).getSingleResult();
    }

    @Test
    void testExistsByUsername_False() {
        String username = "testUser";
        when(session.createQuery("select count(l) from LoginMaster l where l.userName = :username", Long.class)).thenReturn(countQuery);
        when(countQuery.setParameter("username", username)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(0L);

        boolean result = loginDAO.existsByUsername(username);
        assertFalse(result);
        verify(session, times(1)).createQuery("select count(l) from LoginMaster l where l.userName = :username", Long.class);
        verify(countQuery, times(1)).setParameter("username", username);
        verify(countQuery, times(1)).getSingleResult();
    }

    @Test
    void testExistsByEmail_True() {
        String email = "test@example.com";
        when(session.createQuery("select count(l) from LoginMaster l where l.email = :email", Long.class)).thenReturn(countQuery);
        when(countQuery.setParameter("email", email)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(1L);

        boolean result = loginDAO.existsByEmail(email);
        assertTrue(result);
        verify(session, times(1)).createQuery("select count(l) from LoginMaster l where l.email = :email", Long.class);
        verify(countQuery, times(1)).setParameter("email", email);
        verify(countQuery, times(1)).getSingleResult();
    }

    @Test
    void testExistsByEmail_False() {
        String email = "notfound@example.com";
        when(session.createQuery("select count(l) from LoginMaster l where l.email = :email", Long.class)).thenReturn(countQuery);
        when(countQuery.setParameter("email", email)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(0L);

        boolean result = loginDAO.existsByEmail(email);
        assertFalse(result);
        verify(session, times(1)).createQuery("select count(l) from LoginMaster l where l.email = :email", Long.class);
        verify(countQuery, times(1)).setParameter("email", email);
        verify(countQuery, times(1)).getSingleResult();
    }
}
