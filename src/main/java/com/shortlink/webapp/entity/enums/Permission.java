package com.shortlink.webapp.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Permission {//implements GrantedAuthority {

    ADMIN_READ      ,//("admin:read"),
    ADMIN_UPDATE    ,//("admin:update"),
    ADMIN_CREATE    ,//("admin:create"),
    ADMIN_DELETE    ,//("admin:delete"),
    MANAGER_READ    ,//("management:read"),
    MANAGER_UPDATE  ,//("management:update"),
    MANAGER_CREATE  ,//("management:create"),
    MANAGER_DELETE  ;//("management:delete");

//    @Getter
//    private final String permission;

//    @Override
//    public String getAuthority() {
//        return name();
//    }
}