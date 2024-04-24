package com.shortlink.webapp.dto.link.response;

import com.shortlink.webapp.dto.projection.link.ClickLinkStatProjection;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClickLinkStatDto implements ClickLinkStatProjection {

    @NotBlank
    private String dateOfUses;

    @Min(0)
    private Long countOfUses;

}