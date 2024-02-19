package com.shortlink.webapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterLink {

    private String originalLink;
    private String shortLink;
    private Boolean userExists;
}
