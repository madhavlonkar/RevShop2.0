package com.revshop.service;

import com.revshop.master.UserMaster;

public interface UserService {
    UserMaster saveUser(UserMaster user);
	boolean updateDetails(UserMaster user, String role);
	UserMaster getUserById(int sellerId);
}
