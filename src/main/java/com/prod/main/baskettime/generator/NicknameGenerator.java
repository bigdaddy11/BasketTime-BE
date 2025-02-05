package com.prod.main.baskettime.generator;

import java.util.Random;

public class NicknameGenerator {

    private static final String[] ADJECTIVES = {
        "행복한", "빠른", "느긋한", "용감한", "똑똑한", "귀여운", "멋진", "재밌는", "강한", "부드런"
    };

    private static final String[] NOUNS = {
        "호랑이", "토끼", "사자", "코끼리", "고양이", "강아지", "독수리", "곰", "여우", "팬더"
    };

    public static String generateRandomNickname() {
        Random random = new Random();
        String adjective = ADJECTIVES[random.nextInt(ADJECTIVES.length)].replaceAll("\\s+", "").trim(); // 띄어쓰기 제거
        String noun = NOUNS[random.nextInt(NOUNS.length)].replaceAll("\\s+", "").trim(); // 띄어쓰기 제거
        int number = random.nextInt(100); // 0부터 99까지의 랜덤 숫자

        String nickname = adjective + noun + number;
        return nickname;
    }
}

