package com.feredback.feredback_backend.util;

import com.feredback.feredback_backend.entity.User;
import com.feredback.feredback_backend.security.SecurityUser;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @program: FE-Redback
 * @description:For getting logged-in user's information
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-15 20:07
 **/
public class SecurityContextUtil {

    public static User getCurrentUser() {
        User currentUser = ((SecurityUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getLoggedInUser();
        return currentUser;
    }
}
