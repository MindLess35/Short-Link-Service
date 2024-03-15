package com.shortlink.webapp.config;

import com.shortlink.webapp.WebappApplication;
import com.shortlink.webapp.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.envers.repository.config.EnableEnversRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
@EnableEnversRepositories(basePackageClasses = WebappApplication.class)
public class AuditConfig {

//    public static void main(String[] args) {
//        String f = null;
//        Optional.ofNullable(f)
//                .map(r -> r == null ? null : "ff")
//                .map(r -> r.length())
//                .ifPresent(System.out::println);
//
//        String ff = "null";
//        Optional.of(ff)
//                .map(r -> null)
//                .map(r -> r.hashCode())
//                .ifPresent(System.out::println);
//
//        String zz = "null";
//        Optional.ofNullable(zz)
//                .map(r -> r == null ? "null" : null)
//                .map(r -> r.length())
//                .ifPresent(System.out::println);
//
//        String k = "null";
//        Optional.of(k)
//                .map(r -> r == null ? "null" : null)
//                .map(r -> r.length())
//                .ifPresent(System.out::println);
//
//        String x = "null";
//        Optional.ofNullable(x)
//                .filter(s -> s.length() > 100)
//                .map(Object::hashCode)
//                .ifPresent(System.out::println);
//
//        String y = "null";
//        Optional.of(y)
//                .filter(s -> s.length() > 100)
//                .map(r -> r.length())
//                .ifPresent(System.out::println);
//
//        String v = null;    // npe !!
//        Optional.ofNullable(v.length())
////                .filter(s -> s.length() > 100)
//                .map(Object::hashCode)
//                .ifPresent(System.out::println);
//
////        String n = "null";
////        Optional.of(gag -> gag == null ? "d" : null)
////                .filter(s -> s.length() > 100)
////                .map(r -> r.length())
////                .ifPresent(System.out::println);
//
//    }
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

//    @Bean
//    public AuditorAware<Long> auditorAware() {
//        return () -> {
//            Authentication authentication = SecurityContextHolder
//                    .getContext()
//                    .getAuthentication();
//            if (authentication == null ||
//                !authentication.isAuthenticated() ||
//                authentication instanceof AnonymousAuthenticationToken
//            ) {
//                return Optional.empty();
//            }
//
//            User user = (User) authentication.getPrincipal();
//            return Optional.of(user.getId());
//        };
//
//    }

    @Bean
    public AuditorAware<Long> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .map(User.class::cast)
                .map(User::getId);
    }
//    @Override
//    public Optional<User> getCurrentAuditor() {

//        return Optional.ofNullable(SecurityContextHolder.getContext())
//                .map(SecurityContext::getAuthentication)
//                .filter(Authentication::isAuthenticated)
//                .map(Authentication::getPrincipal)
//                .map(User.class::cast);
//    }
}

