package com.revshop.dao;

import java.time.LocalDateTime;

import com.revshop.master.PasswordResetTokenMaster;

public interface PasswordResetTokenDAO {
    void saveToken(PasswordResetTokenMaster token);

    PasswordResetTokenMaster findByToken(String token);

    void deleteToken(PasswordResetTokenMaster token);

	void deleteExpiredTokens(LocalDateTime now);
}
