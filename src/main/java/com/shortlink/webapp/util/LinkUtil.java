package com.shortlink.webapp.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Objects;

@UtilityClass
public class LinkUtil {
    public final String URI = ServletUriComponentsBuilder
                                             .fromCurrentContextPath()
                                             .build()
                                             .toUriString() + "/";


//    public String encryptKeySHA256(String key) {
//        byte[] hashBytes;
//        try {
//            hashBytes = MessageDigest.getInstance("SHA-256")
//                    .digest(key.getBytes(StandardCharsets.UTF_8));
//            StringBuilder hash = new StringBuilder();
//            for (byte b : hashBytes) {
//                hash.append(String.format("%02x", b));
//            }
//            return hash.toString();
//
//        } catch (NoSuchAlgorithmException ex) {
////            TODO
////            throw new NoSuchAlgorithmException();
//            return key;
//        }
//
//    }

//    public boolean isCorrectKey(String maybeCorrectKey, String encryptedKey) {
//        String hashMaybeCorrectKey = encryptKeySHA256(maybeCorrectKey);
//        return Objects.equals(hashMaybeCorrectKey, encryptedKey);
//
//    }

    public String generateRandomString(Short count) {
        return RandomStringUtils.random(count, true, true);
    }

    public boolean isCorrectKey(String maybeKey, String correctKey) {
        return Objects.equals(maybeKey, correctKey);
    }

//    public static String getCurrentDomain() {
//        return DOMAIN;
//    }
}


