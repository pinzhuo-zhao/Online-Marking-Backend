package com.feredback.feredback_backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feredback.feredback_backend.entity.User;
import com.feredback.feredback_backend.entity.vo.UserVo;
import com.feredback.feredback_backend.util.JsonResult;
import com.feredback.feredback_backend.util.SecurityContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-12 01:07
 **/

@Component
public class SuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        User loggedInUser = SecurityContextUtil.getCurrentUser();
    /*     if nothing goes wrong, return the user found to frontend, but only a part of the fields'
         value will be returned as a UserVO, for security reasons.*/
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(loggedInUser,userVo);
        JsonResult result = JsonResult.ok();
        result.data("user",userVo);
        ObjectMapper mapper = new ObjectMapper();
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.setStatus(HttpStatus.OK.value());
        mapper.writeValue(httpServletResponse.getWriter(), result);

    }
}
