package com.feredback.feredback_backend.service.impl;

import com.feredback.feredback_backend.entity.User;
import com.feredback.feredback_backend.security.SecurityUser;
import com.feredback.feredback_backend.service.ILoginService;
import com.feredback.feredback_backend.service.ex.UserNotFoundException;
import com.feredback.feredback_backend.util.JwtUtils;
import com.feredback.feredback_backend.util.SecurityContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @program: Online-Marking-Backend
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-07-04 23:46
 **/
@Service
public class LoginServiceImpl implements ILoginService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Override
    public String login(User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            throw  new UserNotFoundException("Wrong Username Or Password");
        }
        //Generating JWT
        String emailUsername = ((SecurityUser) authenticate.getPrincipal()).getUsername();
        String id = String.valueOf(((SecurityUser) authenticate.getPrincipal()).getLoggedInUser().getId());
        return JwtUtils.getJwtToken(id, emailUsername);

    }

    @Override
    public void logout() {
        User currentUser = SecurityContextUtil.getCurrentUser();
    }
}
