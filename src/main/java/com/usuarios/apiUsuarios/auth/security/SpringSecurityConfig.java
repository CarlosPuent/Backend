package com.usuarios.apiUsuarios.auth.security;

import com.usuarios.apiUsuarios.auth.filter.JwtAuthenticationFilter;
import com.usuarios.apiUsuarios.auth.filter.JwtValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.Arrays;

@Configuration
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Rutas de usuarios
                        .requestMatchers(HttpMethod.GET, "/users", "/users/page/{page}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/{id}").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/users").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/{id}").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/users/{id}").hasAnyRole("ADMIN")

                        // Rutas de departamentos
                        .requestMatchers(HttpMethod.GET, "/departments", "/departments/page/{page}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/departments/{id}").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/departments").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/departments/{id}").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/departments/{id}").hasAnyRole("ADMIN")

                        // Rutas de posiciones
                        .requestMatchers(HttpMethod.GET, "/positions", "/positions/page/{page}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/positions/{id}").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/positions").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/positions/{id}").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/positions/{id}").hasAnyRole("ADMIN")

                        // Rutas de Swagger
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilter(new JwtAuthenticationFilter(authenticationConfiguration.getAuthenticationManager()))
                .addFilter(new JwtValidationFilter(authenticationConfiguration.getAuthenticationManager()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:9090"));
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
