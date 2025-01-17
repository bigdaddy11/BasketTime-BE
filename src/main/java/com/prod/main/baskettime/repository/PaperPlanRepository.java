package com.prod.main.baskettime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prod.main.baskettime.entity.PaperPlan;

public interface PaperPlanRepository extends JpaRepository<PaperPlan, Long> {
    List<PaperPlan> findByRUserId(Long receiverId); // 특정 사용자가 받은 메시지 조회
    List<PaperPlan> findBySUserId(Long senderId); // 특정 사용자가 보낸 메시지 조회
}
