package com.feredback.feredback_backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feredback.feredback_backend.util.JsonResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-12 01:11
 **/

@Component
public class FailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        JsonResult result = JsonResult.error();
        if (e instanceof BadCredentialsException) {
            //wrong password
            result.code(4002);
            result.message("Wrong Username or Password");
        } else if (e instanceof DisabledException) {
            result.code(4003);
            result.message("Account Disabled");
        }  else if (e instanceof InternalAuthenticationServiceException) {
            result.code(4002);
            result.message("Wrong Username or Password");
        }

        ObjectMapper mapper = new ObjectMapper();
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.setStatus(HttpStatus.OK.value());
        mapper.writeValue(httpServletResponse.getWriter(), result);

        }
}
