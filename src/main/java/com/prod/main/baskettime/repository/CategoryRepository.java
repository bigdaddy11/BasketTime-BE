package com.prod.main.baskettime.repository;

import com.prod.main.baskettime.entity.Category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByOrderByIdAsc();  // 전체 게시글을 id 기준으로 오름차순 정렬
}
