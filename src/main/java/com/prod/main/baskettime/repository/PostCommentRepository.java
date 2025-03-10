package com.prod.main.baskettime.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.prod.main.baskettime.entity.PostComment;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findByRelationIdOrderByCreatedAtAsc(Long postId); // 특정 게시글의 댓글을 시간순 정렬

    @Query(value = """
        SELECT a.id,
            a.comment_text,
            b.nick_name,
            CASE 
                WHEN AGE(NOW(), a.created_at) < INTERVAL '1 minute' THEN '방금 전'
                WHEN AGE(NOW(), a.created_at) < INTERVAL '1 hour' THEN CONCAT(EXTRACT(MINUTE FROM AGE(NOW(), a.created_at))::INT, '분 전')
                WHEN AGE(NOW(), a.created_at) < INTERVAL '1 day' THEN CONCAT(EXTRACT(HOUR FROM AGE(NOW(), a.created_at))::INT, '시간 전')
                WHEN AGE(NOW(), a.created_at) < INTERVAL '2 day' THEN '1일 전'
                WHEN AGE(NOW(), a.created_at) < INTERVAL '3 day' THEN '2일 전'
                ELSE TO_CHAR(a.created_at, '오래 전') -- 그 외에는 원본 날짜 출력
            END AS time_ago,
            a.user_id,
            a.parent_id
        FROM post_comment a
        LEFT JOIN users b ON a.user_id = b.id
        WHERE (:postId IS NULL OR a.relation_id = :postId)
          AND (:type IS NULL OR a.type = :type)
        ORDER BY a.id ASC
    """, nativeQuery = true)
    List<Object[]> findPostCommentWithNickName(@Param("postId") Long postId, @Param("type") String type);

    @Query(value = """
        SELECT a.id,
            a.comment_text,
            b.nick_name,
            CASE 
                WHEN AGE(NOW(), a.created_at) < INTERVAL '1 minute' THEN '방금 전'
                WHEN AGE(NOW(), a.created_at) < INTERVAL '1 hour' THEN CONCAT(EXTRACT(MINUTE FROM AGE(NOW(), a.created_at))::INT, '분 전')
                WHEN AGE(NOW(), a.created_at) < INTERVAL '1 day' THEN CONCAT(EXTRACT(HOUR FROM AGE(NOW(), a.created_at))::INT, '시간 전')
                WHEN AGE(NOW(), a.created_at) < INTERVAL '2 day' THEN '1일 전'
                WHEN AGE(NOW(), a.created_at) < INTERVAL '3 day' THEN '2일 전'
                ELSE TO_CHAR(a.created_at, 'YYYY-MM-DD HH24:MI:SS')
            END AS time_ago,
            a.user_id,
            a.parent_id
        FROM post_comment a
        LEFT JOIN users b ON a.user_id = b.id
        WHERE a.parent_id = :parentId
        ORDER BY a.id ASC
    """, nativeQuery = true)
    List<Object[]> findRepliesWithNickName(@Param("parentId") Long parentId);

    @Query(value = """
        SELECT a.id,
            a.comment_text,
            b.nick_name,
            CASE 
                WHEN AGE(NOW(), a.created_at) < INTERVAL '1 minute' THEN '방금 전'
                WHEN AGE(NOW(), a.created_at) < INTERVAL '1 hour' THEN CONCAT(EXTRACT(MINUTE FROM AGE(NOW(), a.created_at))::INT, '분 전')
                WHEN AGE(NOW(), a.created_at) < INTERVAL '1 day' THEN CONCAT(EXTRACT(HOUR FROM AGE(NOW(), a.created_at))::INT, '시간 전')
                WHEN AGE(NOW(), a.created_at) < INTERVAL '2 day' THEN '1일 전'
                WHEN AGE(NOW(), a.created_at) < INTERVAL '3 day' THEN '2일 전'
                ELSE TO_CHAR(a.created_at, 'YYYY-MM-DD HH24:MI:SS')
            END AS time_ago,
            a.user_id,
            a.parent_id
        FROM post_comment a
        LEFT JOIN users b ON a.user_id = b.id
        WHERE a.id = :commentId
    """, nativeQuery = true)
    Object findCommentByIdWithNickName(@Param("commentId") Long commentId);



    List<PostComment> findByParentId(Long parentId);
    List<PostComment> findByRelationIdAndParentIdIsNull(Long postId);

    @Query(value = """
        SELECT a.id, a.comment_text, a.created_at, a.relation_id, b.user_id 
        FROM post_comment a
        LEFT JOIN posts b ON a.relation_id = b.id
        WHERE a.created_at > :timestamp
        """, nativeQuery = true)
    List<Object[]> findRecentCommentsWithAuthor(@Param("timestamp") LocalDateTime timestamp);
}
