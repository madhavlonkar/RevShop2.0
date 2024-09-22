package com.revshop.service;

import com.revshop.master.LoginMaster;
import com.revshop.master.PasswordResetTokenMaster;

public interface PasswordResetService {
    void createPasswordResetToken(LoginMaster loginMaster, String token);

    PasswordResetTokenMaster getToken(String token);

    void deleteToken(PasswordResetTokenMaster token);

	void resetPassword(String token, String newPassword);

	LoginMaster getByResetToken(String token);

	void sendPasswordResetToken(String email);
}
