package com.revshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/login", "/register","/oauth2LoginSuccess", "/oauth2/**", "/home", "/WEB-INF/**", "/resources/**", "/static/**", "/css/**", "/js/**", "/img/home/**", "/libs/**").permitAll() // Allow access to static resources without authentication
                .requestMatchers(HttpMethod.GET, "/product/**").permitAll()
                .requestMatchers("/forgot-password", "/reset-password/**").permitAll()
                .requestMatchers("/dashboard/**").hasAuthority("ROLE_seller") // Check for 'seller' role
                .requestMatchers("/products/add").hasAuthority("ROLE_seller")
                .requestMatchers("/products/edit/**").hasAuthority("ROLE_seller")// Check for 'buyer' role
                .requestMatchers("/products/update").hasAuthority("ROLE_seller")
                .requestMatchers("/products/delete/**").hasAuthority("ROLE_seller")
                .anyRequest().authenticated() // All other requests require authentication
            )
            .formLogin((form) -> form
                .loginPage("/login") // Custom login page
                .usernameParameter("email") // Use "email" instead of "username"
                .passwordParameter("password") // Ensure the password field matches the input
                .defaultSuccessUrl("/loginSuccess", true) // Redirect to custom loginSuccess handler
                .failureUrl("/login?error=true")  // Redirect on failure
                .permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/oauth2LoginSuccess", true)
                .failureUrl("/login?error=true")
            )
            .logout((logout) -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity (you can enable it later if needed)
            .exceptionHandling(handling -> handling
                    .accessDeniedPage("/unauthorizedAccess") // Redirect to custom page
                ); // Redirect to an error page if access is denied

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder())
            .and()
            .build();
    }
}
