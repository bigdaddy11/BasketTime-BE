package com.prod.main.baskettime.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prod.main.baskettime.service.CommentService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 수 조회
    @GetMapping("/count")
    public ResponseEntity<Long> getCommentCount(@PathVariable("postId") Long postId) {
        long count = commentService.getCommentCount(postId);
        return ResponseEntity.ok(count);
    }
}
