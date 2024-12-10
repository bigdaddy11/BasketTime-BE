package com.prod.main.baskettime.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prod.main.baskettime.entity.PostComment;
import com.prod.main.baskettime.service.InteractionService;

@RestController
@RequestMapping("/api/interactions")
public class InteractionController {
    private final InteractionService interactionService;

    public InteractionController(InteractionService interactionService) {
        this.interactionService = interactionService;
    }
   
    // 좋아요 추가
    @PostMapping("/likes")
    public ResponseEntity<Void> addLike(@RequestBody Map<String, Object> payload) {
        Long relationId = Long.valueOf(payload.get("relationId").toString());
        Long userId = Long.valueOf(payload.get("userId").toString());
        String type = payload.get("type").toString();

        interactionService.addLike(relationId, userId, type);
        return ResponseEntity.ok().build();
    }

    // 좋아요 취소
    @DeleteMapping("/likes")
    public ResponseEntity<Void> removeLike(@RequestBody Map<String, Object> payload) {
        Long relationId = Long.valueOf(payload.get("relationId").toString());
        Long userId = Long.valueOf(payload.get("userId").toString());
        String type = payload.get("type").toString();

        interactionService.removeLike(relationId, userId, type);
        return ResponseEntity.ok().build();
    }

    // 조회수 추가
    @PostMapping("/views")
    public ResponseEntity<Void> addView(@RequestBody Map<String, Object> payload) {
        Long relationId = ((Number) payload.get("relationId")).longValue();
        Long userId = ((Number) payload.get("userId")).longValue();
        String type = (String) payload.get("type");
        interactionService.addView(relationId, userId, type);
        return ResponseEntity.ok().build();
    }

    // 조회수 조회
    @GetMapping("/views/count")
    public ResponseEntity<Long> getViewCount(@PathVariable("relationId") Long relationId) {
        return ResponseEntity.ok(interactionService.getViewCount(relationId));
    }
}
