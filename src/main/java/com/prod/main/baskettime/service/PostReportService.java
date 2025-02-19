package com.prod.main.baskettime.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prod.main.baskettime.entity.PostReport;
import com.prod.main.baskettime.repository.PostReportRepository;

@Service
public class PostReportService {
    private final PostReportRepository postReportRepository;

    @Autowired
    public PostReportService(PostReportRepository postReportRepository) {
        this.postReportRepository = postReportRepository;
    }

    public void reportContent(Long userId, String type, Long relationId) {
        // 중복 신고 방지
        if (postReportRepository.existsByUserIdAndTypeAndRelationId(userId, type, relationId)) {
            throw new IllegalStateException("이미 신고한 게시글/댓글입니다.");
        }

        // 신고 데이터 저장
        PostReport postReport = new PostReport();
        postReport.setUserId(userId);
        postReport.setType(type);
        postReport.setRelationId(relationId);

        postReportRepository.save(postReport);
    }
}
