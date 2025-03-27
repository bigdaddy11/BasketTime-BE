package com.prod.main.baskettime.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prod.main.baskettime.batch.PushNotificationScheduler;

@RestController
@RequestMapping("/api/push")
public class PushTriggerController {
    private final PushNotificationScheduler pushScheduler;

    public PushTriggerController(PushNotificationScheduler pushScheduler) {
        this.pushScheduler = pushScheduler;
    }

    @PostMapping("/run")
    public ResponseEntity<String> triggerPushManually(@RequestParam String key) {
        if (!"h98016995!".equals(key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
        }
        pushScheduler.sendNewCommentNotifications();
        pushScheduler.sendNewMessageNotifications();
        return ResponseEntity.ok("✅ 푸시 트리거 완료");
    }
}
