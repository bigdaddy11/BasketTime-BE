package com.prod.main.baskettime.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleAuthService {

    @Value("${push.mode}")
    private String pushMode;  // 현재 환경 (expo or fcm)

    @Value("${push.expo.url}")
    private String expoUrl;

    @Value("${push.fcm.url}")
    private String fcmUrl;

    private static final String SERVICE_ACCOUNT_FILE = "src/main/resources/service-account.json"; // 서비스 계정 JSON 경로
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String FCM_URL = "https://fcm.googleapis.com/v1/projects/dependable-glow-439512-m5/messages:send";

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * ✅ Google OAuth2 JWT를 생성하고 액세스 토큰 요청
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
     * ✅ 환경에 따라 Expo 또는 FCM으로 푸시 전송
     */
    public String sendPushNotification(String targetToken, String title, String body) {
        return pushMode.equalsIgnoreCase("expo") 
            ? sendExpoNotification(targetToken, title, body) 
            : sendFCMNotification(targetToken, title, body);
    }

    /**
     * ✅ Expo Push Notification 전송 (개발 환경)
     */
    private String sendExpoNotification(String targetToken, String title, String body) {
        if (!targetToken.startsWith("ExponentPushToken")) {
            return "❌ Invalid Expo Push Token";
        }

        Map<String, Object> payload = new HashMap<>();
        String decodedPushToken = URLDecoder.decode(targetToken, StandardCharsets.UTF_8);
        payload.put("to", decodedPushToken);
        payload.put("title", title);
        payload.put("body", body);
        payload.put("sound", "default");
        payload.put("data", Map.of("extraData", "테스트 데이터"));

        return sendHttpRequest(expoUrl, payload, null);
    }

    /**
     * ✅ Firebase Cloud Messaging (FCM) 전송 (운영 환경)
     */
    private String sendFCMNotification(String targetToken, String title, String body) {
        try {
            String accessToken = getAccessToken(); // 🔹 OAuth2 액세스 토큰 가져오기
            String decodedPushToken = URLDecoder.decode(targetToken, StandardCharsets.UTF_8);
            // 🔹 1. 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 🔹 2. FCM 메시지 JSON 데이터 생성 (Map 사용)
            Map<String, Object> notification = new HashMap<>();
            notification.put("title", title);
            notification.put("body", body);
           
            Map<String, Object> message = new HashMap<>();
            message.put("token", decodedPushToken);
            message.put("notification", notification);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("message", message);

            // 🔹 3. JSON 변환 (Jackson ObjectMapper 활용)
            String requestJson = objectMapper.writeValueAsString(requestBody);

            // 🔹 4. HTTP 요청 보내기
            return sendHttpRequest(FCM_URL, requestJson, accessToken);
        } catch (Exception e) {
            throw new RuntimeException("❌ FCM 푸시 알림 전송 실패", e);
        }
    }

    /**
     * ✅ HTTP 요청 처리 (FCM & Expo 공통)
     */
    private String sendHttpRequest(String url, Object payload, String serverKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (serverKey != null) {
            headers.setBearerAuth(serverKey);
        }

        HttpEntity<Object> entity = new HttpEntity<>(payload, headers);
        ResponseEntity<String> response = new RestTemplate().exchange(url, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }
}
