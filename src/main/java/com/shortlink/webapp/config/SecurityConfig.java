package com.shortlink.webapp.config;

import com.shortlink.webapp.property.MailProperty;
import com.shortlink.webapp.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.util.UUID;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    @Profile("dev & !(prod | test)")
    public SecurityFilterChain securityFilterChainDev(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable);

        httpSecurity.authorizeHttpRequests(request -> request
                .anyRequest().permitAll());

        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.logout(logout -> logout
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) ->
                        SecurityContextHolder.clearContext())
        );

        return httpSecurity.build();
    }

    @Bean
    @Primary
    @Profile("(prod | test) & !dev")
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable);

        httpSecurity.authorizeHttpRequests(request -> request
                .requestMatchers("/api/v1/users/**").authenticated()
                .anyRequest().permitAll());
////                        .requestMatchers(WHITE_LIST_URL)
////                        .permitAll()
//                .requestMatchers("/api/v1/manager/**").hasAnyRole(ADMIN.name(), MANAGER.name())
//                .requestMatchers(GET, "/api/v1/manager/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name())
//                .requestMatchers(POST, "/api/v1/manager/**").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name())
//                .requestMatchers(PUT, "/api/v1/manager/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
//                .requestMatchers(DELETE, "/api/v1/manager/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name()));
//
//        httpSecurity.authorizeHttpRequests(request -> request
//                .requestMatchers("/api/v1/admin/**").hasRole(ADMIN.name())
//                .requestMatchers(GET, "/api/v1/admin/**").hasAuthority(ADMIN_READ.name())
//                .requestMatchers(POST, "/api/v1/admin/**").hasAuthority(ADMIN_CREATE.name())
//                .requestMatchers(PUT, "/api/v1/admin/**").hasAuthority(ADMIN_UPDATE.name())
//                .requestMatchers(DELETE, "/api/v1/admin/**").hasAuthority(ADMIN_DELETE.name())
//
//                .requestMatchers(POST, "api/v1/links").permitAll()
//                .requestMatchers("api/v1/links/**", "api/v1/users/**").hasAnyRole(USER.name(), ADMIN.name())
//                .anyRequest().permitAll());
////                .anyRequest().authenticated());

        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.logout(logout -> logout
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) ->
                        SecurityContextHolder.clearContext())
        );

        return httpSecurity.build();
    }

}
