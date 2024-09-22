package com.revshop.dao.impl;

import java.time.LocalDateTime;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.revshop.dao.PasswordResetTokenDAO;
import com.revshop.master.PasswordResetTokenMaster;

import jakarta.persistence.EntityManager;

@Repository
public class PasswordResetTokenDAOIMPL implements PasswordResetTokenDAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void saveToken(PasswordResetTokenMaster token) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(token);
    }

    @Override
    public PasswordResetTokenMaster findByToken(String token) {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("from PasswordResetTokenMaster where token = :token", PasswordResetTokenMaster.class)
                      .setParameter("token", token)
                      .uniqueResult();
    }

    @Override
    public void deleteToken(PasswordResetTokenMaster token) {
        Session session = entityManager.unwrap(Session.class);
        session.delete(token);
    }
    
    @Override
    public void deleteExpiredTokens(LocalDateTime now) {
        Session session = entityManager.unwrap(Session.class);
        session.createQuery("delete from PasswordResetTokenMaster where expirationTime < :now")
               .setParameter("now", now)
               .executeUpdate();
    }
}
