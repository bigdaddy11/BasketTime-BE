package com.prod.main.baskettime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prod.main.baskettime.dto.PaperPlanWithNickName;
import com.prod.main.baskettime.entity.PaperPlan;

public interface PaperPlanRepository extends JpaRepository<PaperPlan, Long> {
    List<PaperPlan> findByRUserId(Long receiverId); // 특정 사용자가 받은 메시지 조회
    List<PaperPlan> findBySUserId(Long senderId); // 특정 사용자가 보낸 메시지 조회

    @Query(value = """
        SELECT 
            p.id AS id,
            p.content AS content,
            u.nick_name AS nickName,
            CASE 
                WHEN AGE(NOW(), p.created_at) < INTERVAL '1 minute' THEN '방금 전'
                WHEN AGE(NOW(), p.created_at) < INTERVAL '1 hour' THEN CONCAT(EXTRACT(MINUTE FROM AGE(NOW(), p.created_at))::INT, '분 전')
                WHEN AGE(NOW(), p.created_at) < INTERVAL '1 day' THEN CONCAT(EXTRACT(HOUR FROM AGE(NOW(), p.created_at))::INT, '시간 전')
                WHEN AGE(NOW(), p.created_at) < INTERVAL '2 day' THEN '1일 전'
                WHEN AGE(NOW(), p.created_at) < INTERVAL '3 day' THEN '2일 전'
                ELSE TO_CHAR(p.created_at, 'YYYY-MM-DD HH24:MI:SS')
            END AS timeAgo,
            is_read AS isRead
        FROM paper_plan p
        JOIN users u ON p.s_user_id = u.id
        WHERE p.r_user_id = :receiverId
        ORDER BY p.id DESC
        """, nativeQuery = true)
    List<Object[]> findReceivedMessagesWithNickName(@Param("receiverId") Long receiverId);

    @Query(value = """
        SELECT 
            p.id AS id,
            p.content AS content,
            u.nick_name AS nickName,
            CASE 
                WHEN AGE(NOW(), p.created_at) < INTERVAL '1 minute' THEN '방금 전'
                WHEN AGE(NOW(), p.created_at) < INTERVAL '1 hour' THEN CONCAT(EXTRACT(MINUTE FROM AGE(NOW(), p.created_at))::INT, '분 전')
                WHEN AGE(NOW(), p.created_at) < INTERVAL '1 day' THEN CONCAT(EXTRACT(HOUR FROM AGE(NOW(), p.created_at))::INT, '시간 전')
                WHEN AGE(NOW(), p.created_at) < INTERVAL '2 day' THEN '1일 전'
                WHEN AGE(NOW(), p.created_at) < INTERVAL '3 day' THEN '2일 전'
                ELSE TO_CHAR(p.created_at, 'YYYY-MM-DD HH24:MI:SS') -- 포맷된 날짜 출력
            END AS timeAgo,
            is_read AS isRead
        FROM paper_plan p
        JOIN users u ON p.r_user_id = u.id
        WHERE p.s_user_id = :senderId
        ORDER BY p.id DESC
        """, nativeQuery = true)
    List<Object[]> findSentMessagesWithNickName(@Param("senderId") Long senderId);
}
