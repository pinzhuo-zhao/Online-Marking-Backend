package com.feredback.feredback_backend.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feredback.feredback_backend.util.JsonResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-12 01:25
 **/
@Component
public class LogoutHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Authentication authentication) throws IOException, ServletException {
        JsonResult result = JsonResult.ok();
        ObjectMapper mapper = new ObjectMapper();
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.setStatus(HttpStatus.OK.value());
        mapper.writeValue(httpServletResponse.getWriter(), result);
    }
}
