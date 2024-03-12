package com.shortlink.webapp.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
//@AllArgsConstructor
public class LinkReadDto {
    private Long id;
    private String originalLink;
    private String shortLink;


    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
}
