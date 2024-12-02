// SecurityUtil.java
package com.cafereview.util;

import org.mindrot.jbcrypt.BCrypt;
import jakarta.servlet.http.HttpSession;

public class SecurityUtil {
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public static boolean isAuthenticated(HttpSession session) {
        return session != null && session.getAttribute("user") != null;
    }
}
