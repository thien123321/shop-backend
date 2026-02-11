package com.minhthien.web.shop.config.security;

import com.minhthien.web.shop.entity.Auth.User;
import com.minhthien.web.shop.service.security.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Component
public class Filter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Autowired
    TokenService tokenService;

    // Danh sách các API không cần check Token
    private final List<String> PUBLIC_API = List.of(
            // auth
            "POST:/api/auth/register",
            "POST:/api/auth/login",
            "POST:/api/auth/reset-password",
            "POST:/api/auth/forgot-password",
            "POST:/api/auth/logout",


            // Swagger FULL
            "GET:/swagger-ui/**",
            "GET:/swagger-ui.html",
            "GET:/v3/api-docs",
            "GET:/v3/api-docs/**",
            "GET:/v3/api-docs.yaml",
            "GET:/v3/api-docs/swagger-config",
            "GET:/swagger-resources/**",
            "GET:/webjars/**",
            "GET:/ws-chat/**",
            "GET:/ws-chat",
            "GET:/ws/**",
            "GET:/ws",

            "POST:/api/payos/webhook",
            "GET:/payment-success.html",
            "GET:/payment-cancel.html"
    );

    public boolean isPublicAPI(String uri, String method) {
        AntPathMatcher matcher = new AntPathMatcher();
        return PUBLIC_API.stream().anyMatch(pattern -> {
            String[] parts = pattern.split(":", 2);
            if (parts.length != 2) return false;
            String allowedMethod = parts[0];
            String allowedUri = parts[1];
            return method.equalsIgnoreCase(allowedMethod) && matcher.match(allowedUri, uri);
        });
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            // ❗ KHÔNG CHECK PUBLIC Ở ĐÂY
            // Spring Security tự gọi shouldNotFilter()

            String token = getToken(request);

            // Không có token → cho đi tiếp (SecurityConfig quyết định)
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            User account = tokenService.extractAccount(token);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            account.getUsername(),
                            null,
                            List.of(
                                    new SimpleGrantedAuthority(
                                            "ROLE_" + account.getRole().name()
                                    )
                            )
                    );

            auth.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
        }
    }





    public String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7).trim();
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String uri = request.getRequestURI();
        if (uri.startsWith("/api/payos/webhook")) {
            return true;
        }


        String method = request.getMethod();

        System.out.println("URI = " + uri);
        System.out.println("METHOD = " + method);

        return isPublicAPI(uri, method);

//        return uri.startsWith("/swagger-ui")
//                || uri.startsWith("/v3/api-docs")
//                || uri.startsWith("/swagger-resources")
//                || uri.startsWith("/webjars");
    }

}