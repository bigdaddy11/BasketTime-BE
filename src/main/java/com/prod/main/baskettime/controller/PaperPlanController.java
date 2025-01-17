package com.prod.main.baskettime.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<PaperPlan>> getReceivedMessages(@RequestParam Long receiverId) {
        List<PaperPlan> messages = paperPlanService.getReceivedMessages(receiverId);
        return ResponseEntity.ok(messages);
    }

    // 보낸 쪽지 목록 조회
    @GetMapping("/sent")
    public ResponseEntity<List<PaperPlan>> getSentMessages(@RequestParam Long senderId) {
        List<PaperPlan> messages = paperPlanService.getSentMessages(senderId);
        return ResponseEntity.ok(messages);
    }
}
