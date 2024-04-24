package com.shortlink.webapp.domain.property;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "link")
public class LinkProperty {

    @Min(1)
    @NotNull
    private Short countOfRandomUrlCharacters;

    public Short getCount() {
        return countOfRandomUrlCharacters;
    }
}

