package com.feredback.feredback_backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feredback.feredback_backend.util.JsonResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-15 17:48
 **/
public class LogoutFailureHandler implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        JsonResult result = null;

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            result = JsonResult.error();
            result.code(4011);
            result.message("You are not logged in");
            ObjectMapper mapper = new ObjectMapper();
            httpServletResponse.setContentType("text/json;charset=utf-8");
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            try {
                mapper.writeValue(httpServletResponse.getWriter(), result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
