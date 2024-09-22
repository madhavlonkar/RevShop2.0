package com.revshop.dao;

import java.util.List;

import com.revshop.master.LoginMaster;


public interface LoginDAO {
	
	boolean insert(LoginMaster user);
	List<LoginMaster> reterieveAll();
	 void delete(int userId);
	LoginMaster findByUsername(String username);
	LoginMaster findByEmail(String email);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);

}
