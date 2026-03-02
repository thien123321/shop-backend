package com.minhthien.web.shop.config.security;

import com.minhthien.web.shop.service.auth.AuthService;
import com.minhthien.web.shop.service.security.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailService userDetailsService;

    @Autowired
    Filter filter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(List.of("*")); // ⭐ FIX CHÍNH
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthService authservice) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers(
                                        "/api/auth/register",
                                        "/api/auth/login",
                                        "/api/auth/forgot-password",
                                        "/api/auth/reset-password",
                                        "/api/auth/logout",

                                        "/payment-success.html", // Thêm dòng này để PayOS trả về không bị lỗi 403
                                        "/payment-cancel.html",  // Thêm dòng này

                                        "/api/payos/webhook",







                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/v3/api-docs.yaml",
                                        "/api/upload/files/**",
                                        "/ws/**"
                                ).permitAll()
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers(
                                        //pay
                                        "/api/payments/**",
                                        "/api/cart/**",
                                        "/api/refunds/request",
                                        "/api/refunds/{id}/complete"

                                ).authenticated()
                                .requestMatchers(
                                        //product
                                        "/api/product/insert",
                                        "/api/product/update/{id}",
                                        "/api/product/delete/{id}",

                                        //cate
                                        "/api/categories/**",
                                        //quan ly user
                                        "/api/admin/**",
                                        //dashboard
                                        "/api/dashboard/admin/summary",
                                        //giam gia
                                        "/api/product-discount/**",
                                        //report

                                        "/api/admin/product-reports/**"

                                        ).hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers("/api/staff/**",
                                        "/api/dashboard/staff/**").hasAnyRole("USER","ADMIN", "STAFF")
                                .requestMatchers("/api/user/**",
                                        "/api/product-report",
                                        "/api/product-review",
                                        "/api/product/findproduct").hasAnyRole("USER","ADMIN", "STAFF")
                                .anyRequest().authenticated()

                )
                .userDetailsService(userDetailsService)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
