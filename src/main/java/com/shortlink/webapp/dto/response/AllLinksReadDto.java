package com.shortlink.webapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllLinksReadDto {

    private Long id;

    private String originalLink;

    private String shortLink;

//    @DateTimeFormat(pattern = "yyyy-MM-dd HH24:mm:ss")
    private String dateOfCreation;

//    @DateTimeFormat(pattern = "yyyy-MM-dd HH24:mm:ss")
    private String dateOfLastUses;

    private Long countOfUses;

    private String timeToLive;
}
