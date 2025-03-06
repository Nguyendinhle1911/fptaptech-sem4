package com.fptaptech.mvc2jsplibary.util;

import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {

    /**
     * Mã hóa mật khẩu bằng BCrypt
     */
    public static String hashPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password must not be null or empty");
        }
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Kiểm tra mật khẩu nhập vào có khớp với mật khẩu đã mã hóa không
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    /**
     * Tạo một mật khẩu ngẫu nhiên (dùng làm mật khẩu tạm hoặc token)
     */
    public static String generateRandomPassword(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes).substring(0, length);
    }
}