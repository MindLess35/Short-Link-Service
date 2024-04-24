package com.shortlink.webapp.security;

import com.shortlink.webapp.domain.entity.user.User;
import com.shortlink.webapp.domain.enums.Role;
import com.shortlink.webapp.repository.jpa.link.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityExpression {
    private final LinkRepository linkRepository;

    public boolean isCorrectUserAccess(Long requestUserId) {
        User authorizedUser = getAuthorizedUser();

        return isAdmin(authorizedUser)
               || authorizedUser.getId().equals(requestUserId)
               && isVerified(authorizedUser);
    }

    @Transactional(readOnly = true)
    public boolean isUserHasAccessToLink(Long requestLinkId) {
        User authorizedUser = getAuthorizedUser();

        return isAdmin(authorizedUser)
//               || isVerified(authorizedUser)
//               &&
               ||
               linkRepository.existsByIdAndUser(requestLinkId, authorizedUser);

    }

    private boolean isVerified(User authorizedUser) {
        return authorizedUser.getVerified();
    }

    private boolean isAdmin(User authorizedUser) {
        return authorizedUser.getRole().equals(Role.ADMIN);
    }

    private User getAuthorizedUser() {
//        return (User) SecurityContextHolder
//                .getContext()
//                .getAuthentication()
//                .getPrincipal();
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
//                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(User.class::cast)
                .orElseThrow(() -> new RuntimeException("Access denied!"));
    }
}
