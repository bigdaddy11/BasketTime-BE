package com.prod.main.baskettime.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prod.main.baskettime.entity.Post;
import com.prod.main.baskettime.entity.PostComment;
import com.prod.main.baskettime.service.PostCommentService;

@RestController
@RequestMapping("/api/posts/comments")
public class PostCommentController {
    private final PostCommentService postCommentService;

    public PostCommentController(PostCommentService postCommentService) {
        this.postCommentService = postCommentService;
    }

    // 특정 게시글의 댓글 목록 조회
    @GetMapping("/{postId}")
    public ResponseEntity<List<PostComment>> getCommentsByPostId(@PathVariable("postId") Long postId) {
        List<PostComment> comments = postCommentService.getPostCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 작성
    @PostMapping("/{postId}")
    public ResponseEntity<PostComment> addComment(
            @PathVariable("postId") Long postId,
            @RequestBody PostComment commentText
    ) {
        commentText.setPostId(postId); // URL의 postId 설정
        PostComment savedComment = postCommentService.addComment(commentText);
        return ResponseEntity.status(201).body(savedComment);
    }

    // 댓글 수정
    @PutMapping("/{id}")
    public ResponseEntity<PostComment> updateComment(@PathVariable("id") Long id, @RequestBody PostComment postComment) {
        return ResponseEntity.ok(postCommentService.updateComment(id, postComment));
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") Long id) {
        postCommentService.deleteCommentById(id); // 서비스 호출
        return ResponseEntity.noContent().build();
    }
}
