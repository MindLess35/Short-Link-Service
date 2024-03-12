package com.shortlink.webapp.dto.response;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllLinksReadDto {

    private Long id;

    private String originalLink;

    private String shortLink;

    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH24:mm:ss")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH24:mm:ss")
    private String dateOfCreation;
//@JsonValue
    // YYYY-MM-DD HH24:MI:SS
    @DateTimeFormat(pattern = "yyyy-MM-dd HH24:mm:ss")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH24:mm:ss")
    private String dateOfLastUses;

    private Long countOfUses;
}
