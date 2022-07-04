package com.feredback.feredback_backend.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feredback.feredback_backend.util.JsonResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-13 00:34
 **/

@Component
public class Custom403Handler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        JsonResult result = JsonResult.error();
        result.code(4010);
        result.message("You don't have the authority to access this");

        ObjectMapper mapper = new ObjectMapper();
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        mapper.writeValue(httpServletResponse.getWriter(), result);


    }
}
