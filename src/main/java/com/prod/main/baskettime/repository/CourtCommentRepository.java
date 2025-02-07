package com.prod.main.baskettime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prod.main.baskettime.entity.CourtComment;

public interface CourtCommentRepository extends JpaRepository<CourtComment, Long>{
    List<CourtComment> findByCourtId(String courtId); // 농구장 ID로 댓글 조회
}
