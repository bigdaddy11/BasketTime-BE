package com.prod.main.baskettime.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prod.main.baskettime.service.GoogleAuthService;

@RestController
@RequestMapping("/api/google-auth")
public class GoogleAuthController {
    private final GoogleAuthService googleAuthService;

    public GoogleAuthController(GoogleAuthService googleAuthService) {
        this.googleAuthService = googleAuthService;
    }

    /**
     * ✅ FCM 액세스 토큰 요청 API
     */
    @GetMapping("/token")
    public String getGoogleAccessToken() {
        return googleAuthService.getAccessToken();
    }

    /**
     * ✅ 푸시 메시지 전송 API
     */
    @PostMapping("/send")
    public String sendNotification(@RequestBody Map<String, String> payload) {
        String targetToken = payload.get("token");
        String title = payload.get("title");
        String body = payload.get("body");

        return googleAuthService.sendPushNotification(targetToken, title, body);
    }
}
