package com.prod.main.baskettime.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @GetMapping("/{relationId}")
    public ResponseEntity<List<PostComment>> getCommentsByPostId(@PathVariable("relationId") Long relationId,  @RequestParam(name = "type", required = false) String type) {
        List<PostComment> comments = postCommentService.getPostCommentsByPostId(relationId, type);
        return ResponseEntity.ok(comments);
    }

    // 댓글 작성
    @PostMapping("/{relationId}")
    public ResponseEntity<PostComment> addComment(
            @PathVariable("relationId") Long relationId,
            @RequestBody PostComment commentText
    ) {
        commentText.setRelationId(relationId); // URL의 postId 설정
        PostComment savedComment = postCommentService.addComment(commentText);
        return ResponseEntity.status(201).body(savedComment);
    }

    // 대댓글 작성
    @PostMapping
    public ResponseEntity<PostComment> createComment(@RequestBody PostComment comment) {
        PostComment savedComment = postCommentService.saveComment(comment);
        return ResponseEntity.ok(savedComment);
    }

    // 특정 댓글의 대댓글만 가져오기
    @GetMapping("/{commentId}/replies")
    public ResponseEntity<Map<String, Object>> getReplies(@PathVariable Long commentId) {
        PostComment parentComment = postCommentService.getCommentById(commentId);
        List<PostComment> replies = postCommentService.getRepliesByCommentId(commentId);

        Map<String, Object> response = new HashMap<>();
        response.put("parentComment", parentComment);
        response.put("replies", replies);

        return ResponseEntity.ok(response);
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
