package com.prod.main.baskettime.service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.prod.main.baskettime.dto.PaperPlanWithNickName;
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
            message.setIsRead(false);
        }
        return paperPlanRepository.save(message);
    }

    // 받은 메시지 조회
    public List<PaperPlanWithNickName> getReceivedMessages(Long receiverId) {
        List<Object[]> rawResults = paperPlanRepository.findReceivedMessagesWithNickName(receiverId);
        return rawResults.stream()
            .map(result -> new PaperPlanWithNickName(
                ((Long) result[0]).longValue(), // id
                (String) result[1], // content
                (String) result[2], // nickName
                (String) result[3],  // timeAgo
                (Boolean) result[4]  // isRead
            ))
            .collect(Collectors.toList());
    }

    // 보낸 메시지 조회
    public List<PaperPlanWithNickName> getSentMessages(Long senderId) {
        List<Object[]> rawResults = paperPlanRepository.findSentMessagesWithNickName(senderId);
        return rawResults.stream()
            .map(result -> new PaperPlanWithNickName(
                ((Long) result[0]).longValue(), // id
                (String) result[1], // content
                (String) result[2], // nickName
                (String) result[3],  // timeAgo
                (Boolean) result[4]  // isRead
            ))
            .collect(Collectors.toList());
    }

    public void markMessageAsRead(Long messageId) {
        PaperPlan message = paperPlanRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid message ID: " + messageId));
        message.setIsRead(true); // isRead 값을 true로 변경
        paperPlanRepository.save(message);
    }
}
