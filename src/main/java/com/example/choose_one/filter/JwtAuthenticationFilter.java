package com.example.choose_one.filter;

import com.example.choose_one.common.error.ErrorCode;
import com.example.choose_one.common.error.TokenErrorCode;
import com.example.choose_one.common.exception.ApiException;
import com.example.choose_one.service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("필터");

        // 인증이 필요 없는 경로
        String[] excludePaths = {"/swagger-ui.index.html",
                "/swagger-ui",
                "/v3/api-docs",
                "/actuator",
                "/actuator/**",
                "/user/signup",
                "/user/login",
                "/token/reissue"};

        String requestURI = request.getRequestURI();

        boolean isExcludedPath = Arrays.stream(excludePaths)
                .anyMatch(requestURI::startsWith);

        if (isExcludedPath) {
            // 인증이 필요 없는 경로면 필터 통과
            filterChain.doFilter(request, response);
            return;
        }


        String token = request.getHeader("authorization");
        try{
            var map = tokenService.validationToken(token);
            var userId = Long.parseLong(map.get("userId").toString());
            var authorities = (List<String>) map.get("authorities");
            var granted = authorities.stream()
                    .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId,null,granted);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (ApiException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write(""+e.getErrorCodeIfs());
            return;  // 응답 후 필터 체인 진행을 멈춤
        }
        filterChain.doFilter(request,response);
    }
}
