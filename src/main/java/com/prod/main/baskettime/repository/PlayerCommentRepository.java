package com.prod.main.baskettime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Sort;
import com.prod.main.baskettime.entity.PlayerComment;

public interface PlayerCommentRepository extends JpaRepository<PlayerComment, Long> {
    // 선수 ID를 기반으로 댓글 목록을 가져오는 메서드
    List<PlayerComment> findByPlayerIdAndType(Long playerId, String type, Sort sort);

    @Query(value = """
        SELECT a.id, a.comment_text, a.player_id, a.type, a.user_id, b.nick_name,
            CASE 
                WHEN AGE(NOW(), a.created_at) < INTERVAL '1 minute' THEN '방금 전'
                WHEN AGE(NOW(), a.created_at) < INTERVAL '1 hour' THEN CONCAT(EXTRACT(MINUTE FROM AGE(NOW(), a.created_at))::INT, '분 전')
                WHEN AGE(NOW(), a.created_at) < INTERVAL '1 day' THEN CONCAT(EXTRACT(HOUR FROM AGE(NOW(), a.created_at))::INT, '시간 전')
                WHEN AGE(NOW(), a.created_at) < INTERVAL '2 day' THEN '1일 전'
                WHEN AGE(NOW(), a.created_at) < INTERVAL '3 day' THEN '2일 전'
                ELSE TO_CHAR(a.created_at, '오래 전') -- 그 외에는 원본 날짜 출력
            END AS time_ago
        FROM player_comment a
        LEFT JOIN users b on a.user_id = b.id
        WHERE a.player_id = :id
        AND a.type = :type
        ORDER BY a.id ASC
    """, nativeQuery = true)
    List<Object[]> findPlayerWithIdAndType(@Param("id") Long id, @Param("type") String type);
}
