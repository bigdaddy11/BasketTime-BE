package com.prod.main.baskettime.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.prod.main.baskettime.entity.Like;
import com.prod.main.baskettime.entity.View;
import com.prod.main.baskettime.repository.LikeRepository;
import com.prod.main.baskettime.repository.ViewRepository;

import jakarta.transaction.Transactional;

@Service
public class InteractionService {
    private final LikeRepository likeRepository;
    private final ViewRepository viewRepository;

    public InteractionService(LikeRepository likeRepository, ViewRepository viewRepository) {
        this.likeRepository = likeRepository;
        this.viewRepository = viewRepository;
    }

     // 좋아요 추가
    @Transactional // 트랜잭션 보장
    public void addLike(Long relationId, Long userId, String type) {
        if (!likeRepository.existsByRelationIdAndUserId(relationId, userId)) {
            Like like = new Like();
            like.setRelationId(relationId);
            like.setUserId(userId);
            like.setType(type);
            like.setCreatedAt(LocalDateTime.now());
            likeRepository.save(like);
        }
    }

    // 좋아요 취소
    @Transactional // 트랜잭션 보장
    public void removeLike(Long relationId, Long userId, String type) {
        likeRepository.deleteByRelationIdAndUserIdAndType(relationId, userId, type);
    }

    // 좋아요 조회
    public List<Like> getLikes(Long relationId) {
        return likeRepository.findByRelationId(relationId);
    }

    // 조회수 추가
    @Transactional
    public void addView(Long relationId, Long userId, String type) {
        // 동일 유저의 동일 게시물 조회 여부 확인
        boolean exists = viewRepository.existsByRelationIdAndUserIdAndType(relationId, userId, type);

        if (!exists) {
            View view = new View();
            view.setRelationId(relationId);
            view.setUserId(userId);
            view.setType(type);
            view.setCreatedAt(LocalDateTime.now());
            viewRepository.save(view);
        }
    }

    // 조회수 계산
    public long getViewCount(Long relationId) {
        return viewRepository.countByRelationId(relationId);
    }
}
