package com.prod.main.baskettime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prod.main.baskettime.entity.View;

public interface ViewRepository extends JpaRepository<View, Long> {
    List<View> findByRelationId(Long relationId);
    long countByRelationId(Long relationId); // 조회수 계산
    boolean existsByRelationIdAndUserIdAndType(Long relationId, Long userId, String type);
}
