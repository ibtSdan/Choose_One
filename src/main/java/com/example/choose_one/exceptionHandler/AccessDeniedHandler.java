package com.example.choose_one.exceptionHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component

public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);  // 403 Forbidden
        response.setContentType("application/json");

        // JSON 형식으로 권한 부족 메시지 작성
        String jsonResponse = "{\"error\": \"\"Access Denied.\"}";

        // 클라이언트에게 JSON 형식으로 응답 본문 작성
        response.getWriter().write(jsonResponse);
    }
}
