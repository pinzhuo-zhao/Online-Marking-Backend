package com.feredback.feredback_backend.security.filter;

import com.feredback.feredback_backend.entity.User;
import com.feredback.feredback_backend.security.SecurityUser;
import com.feredback.feredback_backend.service.IUserService;
import com.feredback.feredback_backend.service.ex.UserNotFoundException;
import com.feredback.feredback_backend.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @program: Online-Marking-Backend
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-07-04 01:42
 **/
@Component
public class TokenLoginFilter extends OncePerRequestFilter {
    @Autowired
    IUserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!JwtUtils.checkToken(request)) {
            //release the request if the JWT token does not exist
            filterChain.doFilter(request,response);
        } else {
            String email = JwtUtils.getMemberEmailByJwtToken(request);
            User userFound = userService.findUserByEmail(email);
            if (Objects.isNull(userFound)) {
                //meaning we can't find the user with this username in the database
                throw new UserNotFoundException("Wrong Email or Password");
            }
            SecurityUser securityUser = new SecurityUser(userFound);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(securityUser, null, null);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            filterChain.doFilter(request, response);
        }

    }
}
