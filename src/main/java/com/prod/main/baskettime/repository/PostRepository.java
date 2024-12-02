package com.prod.main.baskettime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prod.main.baskettime.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategoryIdOrderByIdAsc(Long categoryId); // 카테고리로 조회
   
    List<Post> findAllByOrderByIdAsc();  // 전체 게시글을 id 기준으로 오름차순 정렬
}
