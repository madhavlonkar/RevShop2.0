package com.revshop.service;

import java.util.List;
import com.revshop.master.LoginMaster;

public interface LoginService {
    boolean registerUser(LoginMaster user);
    List<LoginMaster> getAllUser();
    void deleteUser(int userId);  // New method to delete a user by ID
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	LoginMaster getLoggedInUserDetails();
	boolean isFirstLogin(String email);
	LoginMaster findByEmail(String email);
}
