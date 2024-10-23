package com.prod.main.baskettime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;
import com.prod.main.baskettime.entity.PlayerComment;

public interface PlayerCommentRepository extends JpaRepository<PlayerComment, Long> {
    // 선수 ID를 기반으로 댓글 목록을 가져오는 메서드
    List<PlayerComment> findByPlayerIdAndType(Long playerId, String type, Sort sort);
}
