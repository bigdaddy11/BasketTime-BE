package com.prod.main.baskettime.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prod.main.baskettime.dto.PaperPlanWithNickName;
import com.prod.main.baskettime.entity.PaperPlan;
import com.prod.main.baskettime.service.PaperPlanService;

@RestController
@RequestMapping("/api/paper-plan")
public class PaperPlanController {
    private final PaperPlanService paperPlanService;

    public PaperPlanController(PaperPlanService paperPlanService) {
        this.paperPlanService = paperPlanService;
    }

    // 쪽지 보내기
    @PostMapping
    public ResponseEntity<PaperPlan> sendMessage(
        @RequestBody PaperPlan paperPlan
    ) {
        PaperPlan message = paperPlanService.saveMessage(paperPlan);
        return ResponseEntity.ok(message);
    }

    // 받은 쪽지 목록 조회
    @GetMapping("/received")
    public ResponseEntity<List<PaperPlanWithNickName>> getReceivedMessages(@RequestParam(name = "rUserId", required = false) Long receiverId) {
        List<PaperPlanWithNickName> messages = paperPlanService.getReceivedMessages(receiverId);
        return ResponseEntity.ok(messages);
    }

    // 보낸 쪽지 목록 조회
    @GetMapping("/sent")
    public ResponseEntity<List<PaperPlanWithNickName>> getSentMessages(@RequestParam(name = "sUserId", required = false) Long senderId) {
        List<PaperPlanWithNickName> messages = paperPlanService.getSentMessages(senderId);
        return ResponseEntity.ok(messages);
    }

    // 쪽지 읽음 처리 
    @PutMapping("/{messageId}/read")
    public ResponseEntity<Void> markMessageAsRead(@PathVariable("messageId") Long messageId) {
        paperPlanService.markMessageAsRead(messageId);
        return ResponseEntity.noContent().build();
    }
}
