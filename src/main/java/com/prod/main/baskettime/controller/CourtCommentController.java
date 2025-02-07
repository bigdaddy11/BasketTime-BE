package com.prod.main.baskettime.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prod.main.baskettime.entity.CourtComment;
import com.prod.main.baskettime.service.CommentService;
import com.prod.main.baskettime.service.CourtCommentService;

@RestController
@RequestMapping("/api/courts/comments")
public class CourtCommentController {
    private final CourtCommentService courtCommentService;

    public CourtCommentController(CourtCommentService courtCommentService) {
        this.courtCommentService = courtCommentService;
    }

    // 농구장 ID로 댓글 조회
    @GetMapping("/{courtId}")
    public ResponseEntity<List<CourtComment>> getCommentsByCourtId(@PathVariable String courtId) {
        List<CourtComment> comments = courtCommentService.getCommentsByCourtId(courtId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 추가
    @PostMapping
    public ResponseEntity<CourtComment> addComment(@RequestBody CourtComment comment) {
        CourtComment savedComment = courtCommentService.addComment(comment);
        return ResponseEntity.ok(savedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        courtCommentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<CourtComment> updateComment(
            @PathVariable Long commentId,
            @RequestBody CourtComment comment) {
                CourtComment updatedComment = courtCommentService.updateComment(commentId, comment.getContent());
        return ResponseEntity.ok(updatedComment);
    }
}
