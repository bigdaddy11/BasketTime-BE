package com.prod.main.baskettime.service;

import org.springframework.stereotype.Service;

import com.prod.main.baskettime.entity.CourtComment;
import com.prod.main.baskettime.repository.CourtCommentRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourtCommentService {
    private final CourtCommentRepository CourtCommentRepository;

    public CourtCommentService(CourtCommentRepository CourtCommentRepository) {
        this.CourtCommentRepository = CourtCommentRepository;
    }

    // 농구장 ID로 댓글 조회
    public List<CourtComment> getCommentsByCourtId(String courtId) {
        return CourtCommentRepository.findByCourtId(courtId);
    }

    // 댓글 추가
    public CourtComment addComment(CourtComment comment) {
        return CourtCommentRepository.save(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long id) {
        CourtCommentRepository.deleteById(id);
    }

    // 댓글 수정
    public CourtComment updateComment(Long commentId, String newContent) {
        CourtComment comment = CourtCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다: " + commentId));
        comment.setContent(newContent);
        comment.setUpdatedAt(LocalDateTime.now()); // 수정 시간 갱신
        return CourtCommentRepository.save(comment);
    }
}
