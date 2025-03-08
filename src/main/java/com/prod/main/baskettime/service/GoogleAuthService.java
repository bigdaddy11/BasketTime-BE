package com.prod.main.baskettime.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleAuthService {

    private static final String SERVICE_ACCOUNT_FILE = "src/main/resources/service-account.json"; // 서비스 계정 JSON 경로
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String FCM_URL = "https://fcm.googleapis.com/v1/projects/dependable-glow-439512-m5/messages:send";
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Google OAuth2 JWT를 생성하고 액세스 토큰 요청
     */
    public String getAccessToken() {
        try {
            // 🔹 1. 서비스 계정 JSON 파일 로드
            GoogleCredentials credentials = ServiceAccountCredentials
                    .fromStream(new FileInputStream(SERVICE_ACCOUNT_FILE))
                    .createScoped(Collections.singletonList("https://www.googleapis.com/auth/cloud-platform"));

            // 🔹 2. 액세스 토큰 요청
            credentials.refreshIfExpired();
            return credentials.getAccessToken().getTokenValue();

        } catch (IOException e) {
            throw new RuntimeException("❌ Google OAuth2 액세스 토큰 발급 실패", e);
        }
    }

    /**
     * ✅ FCM 푸시 메시지 전송
     */
    public String sendPushNotification(String targetToken, String title, String body) {
        try {
            String accessToken = getAccessToken(); // 🔹 OAuth2 액세스 토큰 가져오기

            // 🔹 1. 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 🔹 2. FCM 메시지 JSON 데이터 생성 (Map 사용)
            Map<String, Object> notification = new HashMap<>();
            notification.put("title", title);
            notification.put("body", body);

            Map<String, Object> message = new HashMap<>();
            message.put("token", targetToken);
            message.put("notification", notification);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("message", message);

            // 🔹 3. JSON 변환 (Jackson ObjectMapper 활용)
            String requestJson = objectMapper.writeValueAsString(requestBody);

            // 🔹 4. HTTP 요청 보내기
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
            ResponseEntity<String> response = restTemplate.exchange(FCM_URL, HttpMethod.POST, entity, String.class);

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("❌ FCM 푸시 알림 전송 실패", e);
        }
    }
}

