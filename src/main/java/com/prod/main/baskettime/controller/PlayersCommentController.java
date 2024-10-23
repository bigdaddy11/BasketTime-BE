package com.prod.main.baskettime.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.prod.main.baskettime.entity.PlayerComment;
import com.prod.main.baskettime.repository.PlayerCommentRepository;
import com.prod.main.baskettime.service.PlayerCommentService;

@RestController
@RequestMapping("/api/comments")
public class PlayersCommentController {

    @Autowired
    private PlayerCommentRepository playerCommentRepository;

    @Autowired
    private PlayerCommentService playerCommentService;

    // 특정 선수의 댓글 목록을 반환하는 API
    @GetMapping("/{playerId}/{type}")
    public ResponseEntity<List<PlayerComment>> getCommentsByPlayerIdAndType(
        @PathVariable("playerId") Long playerId,
        @PathVariable("type") String type
    ) {
        List<PlayerComment> comments = playerCommentService.getCommentsByPlayerIdAndType(playerId, type);
        return ResponseEntity.ok(comments);
    }

    // 댓글 저장
    @PostMapping
    public ResponseEntity<PlayerComment> createComment(@RequestBody PlayerComment comment) {
        PlayerComment savedComment = playerCommentRepository.save(comment);
        return ResponseEntity.ok(savedComment);
    }
}
