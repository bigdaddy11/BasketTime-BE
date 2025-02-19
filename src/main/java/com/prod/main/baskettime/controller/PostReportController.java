package com.prod.main.baskettime.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prod.main.baskettime.service.PostReportService;

@RestController
@RequestMapping("/api/reports")
public class PostReportController {
    private final PostReportService postReportService;

    @Autowired
    public PostReportController(PostReportService postReportService) {
        this.postReportService = postReportService;
    }

    @PostMapping
    public ResponseEntity<?> reportContent(@RequestBody Map<String, Object> payload) {
        Long userId = ((Number) payload.get("userId")).longValue();
        String type = (String) payload.get("type"); // "P" (게시글) / "PC" (댓글)
        Long relationId = ((Number) payload.get("relationId")).longValue();

        try {
            postReportService.reportContent(userId, type, relationId);
            return ResponseEntity.ok(Collections.singletonMap("message", "게시글 신고에 성공하였습니다."));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT) // 409 Conflict 상태 코드 반환
                    .body(Collections.singletonMap("message", e.getMessage()));
        }
    }
}
