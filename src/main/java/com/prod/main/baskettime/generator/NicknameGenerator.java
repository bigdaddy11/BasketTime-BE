package com.prod.main.baskettime.generator;

import java.util.Random;

public class NicknameGenerator {

    private static final String[] ADJECTIVES = {
        "행복한", "빠른", "느긋한", "용감한", "똑똑한", "귀여운", "멋진", "재미있는", "강한", "부드러운"
    };

    private static final String[] NOUNS = {
        "호랑이", "토끼", "사자", "코끼리", "고양이", "강아지", "독수리", "곰", "여우", "팬더"
    };

    public static String generateRandomNickname() {
        Random random = new Random();
        String adjective = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
        String noun = NOUNS[random.nextInt(NOUNS.length)];
        int number = random.nextInt(1000); // 0부터 999까지의 랜덤 숫자

        return adjective + " " + noun + number;
    }
}

