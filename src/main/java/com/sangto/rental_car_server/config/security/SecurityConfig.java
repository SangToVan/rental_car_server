package com.sangto.rental_car_server.config.security;

import com.sangto.rental_car_server.constant.Endpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private static final String CATCH_ALL_WILDCARDS = "/**";
    public static final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/test" + CATCH_ALL_WILDCARDS,
            "/api-docs" + CATCH_ALL_WILDCARDS,
            "/swagger-ui" + CATCH_ALL_WILDCARDS,
            "/v3/api-docs" + CATCH_ALL_WILDCARDS,
            Endpoint.V1.Home.BASE + CATCH_ALL_WILDCARDS,
            Endpoint.V1.Payment.VNPAY_RETURN,
            Endpoint.V1.Admin.BASE + CATCH_ALL_WILDCARDS,
            Endpoint.V1.Auth.BASE + CATCH_ALL_WILDCARDS,
            Endpoint.V1.Feedback.BASE + CATCH_ALL_WILDCARDS,
            Endpoint.V1.Car.BASE,
            Endpoint.V1.Car.DETAILS,
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest()
                        .authenticated());
        httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(List.of("*")); // ✅ Cho phép mọi domain nếu dùng allowCredentials
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
