package com.shortlink.webapp.util;

import jakarta.validation.constraints.Min;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Setter
@Component
@Validated
@ConfigurationProperties(prefix = "util")
public class LinkUtil {
    @Min(1)
    private int countOfRandomUriCharacters;
    public String encryptKeySHA256(String key) {
        byte[] hashBytes;
        try {
            hashBytes = MessageDigest.getInstance("SHA-256")
                    .digest(key.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte b : hashBytes) {
                hash.append(String.format("%02x", b));
            }
            return hash.toString();

        } catch (NoSuchAlgorithmException ex) {
//            TODO
//            throw new NoSuchAlgorithmException();
            return key;
        }

    }

    public boolean isCorrectKey(String maybeCorrectKey, String encryptedKey) {
        String hashMaybeCorrectKey = encryptKeySHA256(maybeCorrectKey);
        return Objects.equals(hashMaybeCorrectKey, encryptedKey);

    }

    public String generateRandomString() {
        return RandomStringUtils.random(countOfRandomUriCharacters, true, true);
    }
}


