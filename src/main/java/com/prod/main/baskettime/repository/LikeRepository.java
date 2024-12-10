package com.prod.main.baskettime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prod.main.baskettime.entity.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByRelationId(Long relationId);
    boolean existsByRelationIdAndUserId(Long relationId, Long userId);
    void deleteByRelationIdAndUserIdAndType(Long relationId, Long userId, String type);
}
