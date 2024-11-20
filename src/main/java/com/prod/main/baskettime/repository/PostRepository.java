package com.prod.main.baskettime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prod.main.baskettime.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategoryId(Long categoryId); // 카테고리로 조회
}
