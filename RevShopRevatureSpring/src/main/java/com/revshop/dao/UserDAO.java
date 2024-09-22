package com.revshop.dao;

import com.revshop.master.UserMaster;

public interface UserDAO {
    UserMaster saveUser(UserMaster user);
    UserMaster getUserById(int userId); 
}
