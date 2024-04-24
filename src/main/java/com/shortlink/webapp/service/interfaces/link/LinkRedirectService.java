package com.shortlink.webapp.service.interfaces.link;

import com.shortlink.webapp.dto.link.response.RedirectLinkDto;
import org.springframework.http.ResponseEntity;

public interface LinkRedirectService {

    RedirectLinkDto getOriginalLink(String shortLinkName);

    RedirectLinkDto checkLinkKey(String shortLinkName, String key);
    ResponseEntity<?> buildResponseEntity(String originalLink);
}
