package com.levkorolkov.test_application.security;

import com.levkorolkov.test_application.entity.enums.Role;
import com.levkorolkov.test_application.services.users.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final JWTEntryPoint jwtEntryPoint;

    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(JWTEntryPoint jwtEntryPoint, CustomUserDetailsService userDetailsService) {
        this.jwtEntryPoint = jwtEntryPoint;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.exceptionHandling().authenticationEntryPoint(jwtEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests()
                .requestMatchers(SecurityConstants.SIGN_UP_URLS).permitAll()
                .requestMatchers("/api/application/create").hasRole(String.valueOf(Role.USER))
                .requestMatchers("/api/application/all/**").hasRole(String.valueOf(Role.OPERATOR))
                .requestMatchers("/api/application/accept/{appId}").hasRole(String.valueOf(Role.OPERATOR))
                .requestMatchers("/api/application/reject/{appId}").hasRole(String.valueOf(Role.OPERATOR))
                .requestMatchers("/api/user/").hasAnyRole()
                .requestMatchers("/api/user/**").hasRole(String.valueOf(Role.ADMIN))
                .anyRequest().authenticated();

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }
}
