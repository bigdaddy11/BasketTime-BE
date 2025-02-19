package com.prod.main.baskettime.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prod.main.baskettime.entity.PostReport;

public interface PostReportRepository extends JpaRepository<PostReport, Long> {
    boolean existsByUserIdAndTypeAndRelationId(Long userId, String type, Long relationId);
}
