package com.revshop.dao.impl;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.revshop.dao.UserDAO;
import com.revshop.master.UserMaster;

import jakarta.persistence.EntityManager;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public UserMaster saveUser(UserMaster user) {
        Session session = entityManager.unwrap(Session.class);
        return session.merge(user); // Save or update user
    }

    @Override
    public UserMaster getUserById(int userId) {
        Session session = entityManager.unwrap(Session.class);
        return session.get(UserMaster.class, userId); // Fetch user by ID
    }
}
