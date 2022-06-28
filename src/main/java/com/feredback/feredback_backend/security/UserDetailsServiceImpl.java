package com.feredback.feredback_backend.security;

import com.feredback.feredback_backend.security.SecurityUser;
import com.feredback.feredback_backend.entity.User;
import com.feredback.feredback_backend.service.IUserService;
import com.feredback.feredback_backend.service.ex.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-11 14:45
 **/
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUserService userService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userFound = userService.findUserByEmail(email);
        if (null == userFound) {
            //meaning we can't find the user with this username in the database
            throw new UserNotFoundException("Wrong Email or Password");
        }
        SecurityUser securityUser = new SecurityUser(userFound);
        return securityUser;




    }
}
