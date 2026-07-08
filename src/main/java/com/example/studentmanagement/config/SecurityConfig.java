package com.example.studentmanagement.config;

import com.example.studentmanagement.security.CustomUserDetailsService;
import com.example.studentmanagement.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, DaoAuthenticationProvider authenticationProvider) throws Exception{
http
        // REST API does not need CSRF
        .csrf(csrf -> csrf.disable())

        // JWT is stateless
        .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).
        authorizeHttpRequests(auth-> auth
                .requestMatchers(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/auth/**"
                ).permitAll()


//                .requestMatchers(HttpMethod.POST,"/students/**")
//                .hasRole("ADMIN")
//
//                .requestMatchers(HttpMethod.DELETE,"/students/**")
//                .hasRole("ADMIN")
//
//                .requestMatchers(HttpMethod.PUT,"/students/**")
//                .hasAnyRole("ADMIN","TEACHER")
//
//
//                .requestMatchers(HttpMethod.PATCH,"/students/**")
//                .hasAnyRole("ADMIN","TEACHER")
//
//
//                .requestMatchers(HttpMethod.GET,"/students/**")
//                .hasAnyRole("ADMIN","TEACHER","STUDENT")


                .anyRequest().authenticated())

        .authenticationProvider(authenticationProvider)


        // Add JWT filter before username/password filter
        .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        //.httpBasic(Customizer.withDefaults());
return http.build();
    }


    // it stores password as encoded
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider authProvider =
                new DaoAuthenticationProvider(userDetailsService);

        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }
}
