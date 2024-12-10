package com.prod.main.baskettime.service;

import org.springframework.stereotype.Service;

import com.prod.main.baskettime.repository.CommentRepository;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public long getCommentCount(Long postId) {
        return commentRepository.countByPostId(postId);
    }
}
