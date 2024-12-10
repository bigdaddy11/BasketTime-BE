package com.prod.main.baskettime.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prod.main.baskettime.entity.PostComment;

public interface CommentRepository extends JpaRepository<PostComment, Long> {
    long countByPostId(Long postId);
}