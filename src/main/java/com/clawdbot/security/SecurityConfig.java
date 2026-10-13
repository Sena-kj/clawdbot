package com.clawdbot.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final RateLimitFilter rateLimitFilter;
    private final ApiKeyAuthFilter apiKeyAuthFilter;
    private final List<String> corsAllowedOrigins;
    private final List<String> corsAllowedMethods;

    public SecurityConfig(
            JwtAuthFilter jwtAuthFilter,
            RateLimitFilter rateLimitFilter,
            ApiKeyAuthFilter apiKeyAuthFilter,
            @Value("${clawdbot.api.cors.allowed-origins:*}") String corsAllowedOrigins,
            @Value("${clawdbot.api.cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}") String corsAllowedMethods
    ) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.rateLimitFilter = rateLimitFilter;
        this.apiKeyAuthFilter = apiKeyAuthFilter;
        this.corsAllowedOrigins = split(corsAllowedOrigins);
        this.corsAllowedMethods = split(corsAllowedMethods);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(rateLimitFilter, JwtAuthFilter.class)
                .addFilterBefore(apiKeyAuthFilter, JwtAuthFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration apiCors = new CorsConfiguration();
        apiCors.setAllowedOrigins(corsAllowedOrigins);
        apiCors.setAllowedMethods(corsAllowedMethods);
        apiCors.setAllowedHeaders(List.of("*"));
        apiCors.setAllowCredentials(false);

        CorsConfiguration openCors = new CorsConfiguration();
        openCors.setAllowedOrigins(List.of("*"));
        openCors.setAllowedMethods(List.of("GET", "OPTIONS"));
        openCors.setAllowedHeaders(List.of("*"));
        openCors.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", apiCors);
        source.registerCorsConfiguration("/**", openCors);
        return source;
    }

    private List<String> split(String value) {
        if (value == null || value.isBlank()) {
            return List.of("*");
        }
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(item -> !item.isBlank())
                .toList();
    }
}

