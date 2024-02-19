package com.shortlink.webapp.config;

import com.shortlink.webapp.WebappApplication;
import com.shortlink.webapp.entity.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.envers.repository.config.EnableEnversRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
@EnableEnversRepositories(basePackageClasses = WebappApplication.class)
public class AuditConfig implements AuditorAware<Long> {

//    @Bean
//    public AuditorAware<Long> auditorAware() {
//        return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
//                .map(authentication -> (UserDetails) authentication.getPrincipal())
//                .map(UserDetails::getUsername);
//        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
//        Object principal = authentication1.getPrincipal();
//        User user = (User) principal;
//        String username = user.getUsername();

//        return () -> Optional.ofNullable(username);

//    }

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if (authentication == null ||
            !authentication.isAuthenticated() ||
            authentication instanceof AnonymousAuthenticationToken
        ) {
            return Optional.empty();
        }

        User user = (User) authentication.getPrincipal();
        return Optional.of(user.getId());

    }
//    @Override
//    public Optional<User> getCurrentAuditor() {
//
//        return Optional.ofNullable(SecurityContextHolder.getContext())
//                .map(SecurityContext::getAuthentication)
//                .filter(Authentication::isAuthenticated)
//                .map(Authentication::getPrincipal)
//                .map(User.class::cast);
//    }
}

