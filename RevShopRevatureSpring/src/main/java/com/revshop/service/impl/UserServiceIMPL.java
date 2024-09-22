package com.revshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.revshop.dao.LoginDAO;
import com.revshop.dao.UserDAO;
import com.revshop.master.LoginMaster;
import com.revshop.master.UserMaster;
import com.revshop.service.UserService;

import jakarta.transaction.Transactional;

@Service
public class UserServiceIMPL implements UserService {

    @Autowired
    private UserDAO userDAO; // Let Spring inject the UserDAO implementation
    
    @Autowired
    private LoginDAO loginDAO;

    @Override
    @Transactional
    public UserMaster saveUser(UserMaster user) {
        try {
            UserMaster savedUser = userDAO.saveUser(user);
            return savedUser;
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Email already exists. Please use a different email.", e);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while saving the user: " + e.getMessage(), e);
        }
    }

	@Override
	@Transactional
	public boolean updateDetails(UserMaster user,String role) {
		
		LoginMaster Luser = loginDAO.findByEmail(user.getEmail());
        if (Luser != null && Luser.isFirstLogin()) {
        	Luser.setRole("ROLE_"+role);
        	Luser.setFirstLogin(false);
            loginDAO.insert(Luser);
        }
        
        user.setUserId(Luser.getUserId().getUserId());
        userDAO.saveUser(user);
        
		return true;
	}

	@Override
    public UserMaster getUserById(int userId) {
        return userDAO.getUserById(userId); 
    }
}
