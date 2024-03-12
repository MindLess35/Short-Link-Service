package com.shortlink.webapp.security;

import com.shortlink.webapp.entity.User;
import com.shortlink.webapp.entity.enums.Role;
import com.shortlink.webapp.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityExpression {
    private final LinkRepository linkRepository;

    public boolean isCorrectUserAccess(Long requestUserId) {
        User authorizedUser = getAuthorizedUser();

        return isAdmin(authorizedUser)
               || authorizedUser.getId().equals(requestUserId);
    }

    public boolean isUserHasAccessToLink(Long requestLinkId) {
        User authorizedUser = getAuthorizedUser();

        return isAdmin(authorizedUser)
               || linkRepository.existsByIdAndUser(requestLinkId, authorizedUser);

    }

    private boolean isAdmin(User authorizedUser) {
        return authorizedUser.getRole().equals(Role.ADMIN);
    }

    private User getAuthorizedUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
