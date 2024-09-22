package com.revshop.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.revshop.dao.LoginDAO;
import com.revshop.master.LoginMaster;
import com.revshop.master.UserMaster;

import jakarta.persistence.EntityManager;

@Repository
public class LoginDAOIMPL implements LoginDAO{

	@Autowired
	private EntityManager entityManager;
	
	
	
	@Override
	public boolean insert(LoginMaster user) {
		Session session = entityManager.unwrap(Session.class);
		return session.merge(user) != null;
	}
	
	@Override
    public List<LoginMaster> reterieveAll() {
        Session session = entityManager.unwrap(Session.class);
        Query<LoginMaster> query = session.createQuery("from LoginMaster", LoginMaster.class);
        return query.getResultList();
    }
	
	@Override
    public void delete(int userId) {
        Session session = entityManager.unwrap(Session.class);
        UserMaster user = session.get(UserMaster.class, userId);
        if (user != null) {
            session.delete(user);
        }
    }
	
	@Override
	public LoginMaster findByUsername(String username) {
		Session session = entityManager.unwrap(Session.class);
		Query<LoginMaster> query = session.createQuery("from LoginMaster where username = :username", LoginMaster.class);
		query.setParameter("username", username);
		return query.getSingleResult();
	}

	@Override
	public LoginMaster findByEmail(String email) {
	    Session session = entityManager.unwrap(Session.class);
	    Query<LoginMaster> query = session.createQuery("from LoginMaster where email = :email", LoginMaster.class);
	    query.setParameter("email", email);

	    List<LoginMaster> results = query.getResultList();
	    if (results.isEmpty()) {
	        return null; // No user found with the given email
	    } else {
	        return results.get(0); // Return the first result
	    }
	}
	
	@Override
	public boolean existsByUsername(String username) {
	    Session session = entityManager.unwrap(Session.class);
	    Query<Long> query = session.createQuery("select count(l) from LoginMaster l where l.userName = :username", Long.class);
	    query.setParameter("username", username);
	    return query.getSingleResult() > 0;
	}

	@Override
	public boolean existsByEmail(String email) {
	    Session session = entityManager.unwrap(Session.class);
	    Query<Long> query = session.createQuery("select count(l) from LoginMaster l where l.email = :email", Long.class);
	    query.setParameter("email", email);
	    return query.getSingleResult() > 0;
	}



}
