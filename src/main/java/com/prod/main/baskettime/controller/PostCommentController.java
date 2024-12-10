package com.prod.main.baskettime.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prod.main.baskettime.entity.PostComment;
import com.prod.main.baskettime.service.PostCommentService;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class PostCommentController {
    private final PostCommentService postCommentService;

    public PostCommentController(PostCommentService postCommentService) {
        this.postCommentService = postCommentService;
    }

    // 특정 게시글의 댓글 목록 조회
    @GetMapping
    public ResponseEntity<List<PostComment>> getCommentsByPostId(@PathVariable("postId") Long postId) {
        List<PostComment> comments = postCommentService.getPostCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 작성
    @PostMapping
    public ResponseEntity<PostComment> addComment(
            @PathVariable("postId") Long postId,
            @RequestBody PostComment commentText
    ) {
        commentText.setPostId(postId); // URL의 postId 설정
        PostComment savedComment = postCommentService.addComment(commentText);
        return ResponseEntity.status(201).body(savedComment);
    }
}
