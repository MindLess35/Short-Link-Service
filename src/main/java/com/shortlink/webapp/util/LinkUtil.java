package com.shortlink.webapp.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Objects;

@UtilityClass
public class LinkUtil {
    public final String APPLICATION_URL = ServletUriComponentsBuilder
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
//    public static void main(String[] args) {
//        Set<String> apachi = new HashSet<>();
//        Set<String> uuid = new HashSet<>();
//        int uuidMatches = 0;
//        int apachiMatches = 0;
//
//        for (int i = 0; i < 1_000_000_0; i++) {
//            String randomApachi = RandomStringUtils.random(8, true, true);
//            String randomUuid = UUID.randomUUID().toString().substring(0, 8);
//
//            if (apachi.contains(randomApachi))
//                apachiMatches++;
//            else apachi.add(randomApachi);
//
//            if (uuid.contains(randomUuid))
//                uuidMatches++;
//            else uuid.add(randomUuid);
//
//        }
//
//        System.out.println("random apachi " + apachiMatches);
//        System.out.println("random uuid " + uuidMatches);
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


