package com.prod.main.baskettime.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
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
        (SELECT COUNT(*) 
            FROM post_comment pc 
            WHERE pc.relation_id = a.id AND pc.type = 'P') AS comment_count, -- 댓글 수
        COALESCE(like_count, 0) AS like_count, -- 좋아요 수
        (SELECT COUNT(DISTINCT v.id) 
            FROM views v 
            WHERE v.relation_id = a.id AND v.type = 'P') AS view_count, -- 조회수
        (SELECT COUNT(*) > 0
            FROM likes l
            WHERE l.relation_id = a.id AND l.user_id = :userId AND l.type = 'P') AS is_liked, -- 좋아요 여부
        (SELECT pi.image_paths 
            FROM post_images pi 
            WHERE pi.post_id = a.id 
            ORDER BY pi.id ASC 
            LIMIT 1) AS image_main_path,
        c.image
        FROM posts a
        LEFT JOIN users b ON a.user_id = b.id
        LEFT JOIN category c on a.category_id = c.id
        LEFT JOIN (
            SELECT l.relation_id, COUNT(DISTINCT l.id) AS like_count
            FROM likes l 
            WHERE l.type = 'P' 
            GROUP BY l.relation_id
        ) likes_count ON a.id = likes_count.relation_id
        WHERE (:categoryId IS NULL OR a.category_id = :categoryId)
        ORDER BY 
            CASE WHEN :sortOrder = 'popular' THEN COALESCE(likes_count.like_count, 0) END DESC,
            CASE WHEN :sortOrder = 'popular' THEN a.id END DESC,
            CASE WHEN :sortOrder = 'latest' THEN a.id END DESC
        OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY
        """, nativeQuery = true)
    List<Object[]> findPostsWithNickName(
        @Param("categoryId") Long categoryId,
        @Param("userId") Long userId,
        @Param("offset") Long offset,
        @Param("limit") int limit,
        @Param("sortOrder") String sortOrder);

    @Query(value = """
        SELECT COUNT(*)
        FROM posts a
        WHERE (:categoryId IS NULL OR a.category_id = :categoryId)
    """, nativeQuery = true)
    long countPostsWithNickName(@Param("categoryId") Long categoryId);

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
        c.image,
        COALESCE(json_agg(d.image_paths) FILTER (WHERE d.image_paths IS NOT NULL), '[]'::json) AS image_main_path
        FROM posts a
        LEFT JOIN users b ON a.user_id = b.id
        LEFT JOIN category c on a.category_id = c.id
        LEFT JOIN post_images d on a.id = d.post_id
        WHERE (:id IS NULL OR a.id = :id)
        GROUP BY a.id, b.nick_name, c.name, c.image
        ORDER BY a.id DESC
    """, nativeQuery = true)
    List<Object[]> findPostsWithId(@Param("id") Long id);
}
