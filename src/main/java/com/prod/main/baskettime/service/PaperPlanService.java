package com.prod.main.baskettime.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.prod.main.baskettime.entity.PaperPlan;
import com.prod.main.baskettime.repository.PaperPlanRepository;

@Service
public class PaperPlanService {
    private final PaperPlanRepository paperPlanRepository;

    public PaperPlanService(PaperPlanRepository paperPlanRepository) {
        this.paperPlanRepository = paperPlanRepository;
    }

     // 메시지 저장
     public PaperPlan saveMessage(PaperPlan message) {
        // 생성일을 수동으로 설정 (클라이언트에서 제공되지 않는 경우 대비)
        if (message.getCreatedAt() == null) {
            message.setCreatedAt(LocalDateTime.now());
        }
        return paperPlanRepository.save(message);
    }

    // 받은 메시지 조회
    public List<PaperPlan> getReceivedMessages(Long receiverId) {
        return paperPlanRepository.findByRUserId(receiverId);
    }

    // 보낸 메시지 조회
    public List<PaperPlan> getSentMessages(Long senderId) {
        return paperPlanRepository.findBySUserId(senderId);
    }
}
