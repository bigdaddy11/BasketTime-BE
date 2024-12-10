package com.prod.main.baskettime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prod.main.baskettime.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategoryIdOrderByIdAsc(Long categoryId); // 카테고리로 조회
   
    List<Post> findAllByOrderByIdAsc();  // 전체 게시글을 id 기준으로 오름차순 정렬

    @Query(value = """
        SELECT a.id, a.category_id, a.content, a.created_at, a.title, a.updated_at, a.user_id, b.nick_name, c.name as category_name,
        CASE 
            WHEN AGE(NOW(), a.created_at) < INTERVAL '1 minute' THEN '방금 전'
            WHEN AGE(NOW(), a.created_at) < INTERVAL '1 hour' THEN CONCAT(EXTRACT(MINUTE FROM AGE(NOW(), a.created_at))::INT, '분 전')
            WHEN AGE(NOW(), a.created_at) < INTERVAL '1 day' THEN CONCAT(EXTRACT(HOUR FROM AGE(NOW(), a.created_at))::INT, '시간 전')
            WHEN AGE(NOW(), a.created_at) < INTERVAL '2 day' THEN '1일 전'
            WHEN AGE(NOW(), a.created_at) < INTERVAL '3 day' THEN '2일 전'
            ELSE TO_CHAR(a.created_at, '오래 전') -- 그 외에는 원본 날짜 출력
        END AS time_ago,
        COUNT(pc.id) AS comment_count,
        COUNT(DISTINCT l.id) AS like_count,    -- 좋아요 수
        COUNT(DISTINCT v.id) AS view_count,     -- 조회수
        (SELECT COUNT(*) > 0 
            FROM likes 
            WHERE relation_id = a.id AND user_id = :userId) AS is_liked -- 좋아요 여부
        FROM posts a
        LEFT JOIN users b ON a.user_id = b.id
        LEFT JOIN category c on a.category_id = c.id
        LEFT JOIN post_comment pc ON a.id = pc.post_id -- 댓글 테이블과 조인
        LEFT JOIN likes l ON a.id = l.relation_id AND l.type = 'P'    -- 좋아요 테이블과 조인
        LEFT JOIN views v ON a.id = v.relation_id AND v.type = 'P'    -- 조회수 테이블과 조인
        WHERE (:categoryId IS NULL OR a.category_id = :categoryId)
        GROUP BY a.id, a.category_id, a.content, a.created_at, a.title, a.updated_at, a.user_id, b.nick_name, c.name
        ORDER BY a.id DESC
    """, nativeQuery = true)
    List<Object[]> findPostsWithNickName(@Param("categoryId") Long categoryId, @Param("userId") Long userId);

    @Query(value = """
        SELECT a.id, a.category_id, a.content, a.created_at, a.title, a.updated_at, a.user_id, b.nick_name, c.name as category_name,
        CASE 
            WHEN AGE(NOW(), a.created_at) < INTERVAL '1 minute' THEN '방금 전'
            WHEN AGE(NOW(), a.created_at) < INTERVAL '1 hour' THEN CONCAT(EXTRACT(MINUTE FROM AGE(NOW(), a.created_at))::INT, '분 전')
            WHEN AGE(NOW(), a.created_at) < INTERVAL '1 day' THEN CONCAT(EXTRACT(HOUR FROM AGE(NOW(), a.created_at))::INT, '시간 전')
            WHEN AGE(NOW(), a.created_at) < INTERVAL '2 day' THEN '1일 전'
            WHEN AGE(NOW(), a.created_at) < INTERVAL '3 day' THEN '2일 전'
            ELSE TO_CHAR(a.created_at, '오래 전') -- 그 외에는 원본 날짜 출력
        END AS time_ago,
        c.image
        FROM posts a
        LEFT JOIN users b ON a.user_id = b.id
        LEFT JOIN category c on a.category_id = c.id
        WHERE (:id IS NULL OR a.id = :id)
        ORDER BY a.id DESC
    """, nativeQuery = true)
    List<Object[]> findPostsWithId(@Param("id") Long id);
}
