package com.example.choose_one.interceptor;

/*
import com.example.choose_one.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;


security 에서 jwt 검증 하므로 주석 처리


@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {
    private final TokenService tokenService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var accessToken = request.getHeader("authorization");
        var userId = tokenService.validationToken(accessToken);
        var requestContext = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
        requestContext.setAttribute("userId",userId, RequestAttributes.SCOPE_REQUEST);
        return true;
    }
}
*/
