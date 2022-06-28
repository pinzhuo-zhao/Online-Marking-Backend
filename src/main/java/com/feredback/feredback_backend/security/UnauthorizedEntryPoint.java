package com.feredback.feredback_backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feredback.feredback_backend.util.JsonResult;
import com.feredback.feredback_backend.util.SecurityContextUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-12 00:33
 **/

@Component
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        JsonResult result = JsonResult.error();
        result.code(4010);
        System.out.println();
        result.message("Please sign in first");
        ObjectMapper mapper = new ObjectMapper();
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        mapper.writeValue(httpServletResponse.getWriter(), result);
    }
}
