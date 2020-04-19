package com.gmail.yauhenizhukovich.app.service.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomUtil {

    private static final int PASSWORD_LENGTH = 8;
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final Random random = new Random();

    public static String generateUserPassword() {
        String allowedChars = CHAR_LOWER + CHAR_UPPER + NUMBER;
        String shuffledAllowedChars = getShuffledAllowedChars(allowedChars);
        int allowedCharsLength = allowedChars.length();
        StringBuilder stringBuilder = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            appendRandomChar(allowedCharsLength, shuffledAllowedChars, stringBuilder);
        }
        return stringBuilder.toString();
    }

    private static void appendRandomChar(int allowedCharsLength, String shuffledAllowedChars, StringBuilder stringBuilder) {
        int randomCharPosition = random.nextInt(allowedCharsLength);
        char randomChar = shuffledAllowedChars.charAt(randomCharPosition);
        stringBuilder.append(randomChar);
    }

    private static String getShuffledAllowedChars(String allowedSymbols) {
        List<String> allowedSymbolsList = Arrays.asList(allowedSymbols.split(""));
        Collections.shuffle(allowedSymbolsList);
        return String.join("", allowedSymbolsList);
    }

}
