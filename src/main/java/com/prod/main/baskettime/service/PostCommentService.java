package com.prod.main.baskettime.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prod.main.baskettime.entity.Post;
import com.prod.main.baskettime.entity.PostComment;
import com.prod.main.baskettime.repository.PostCommentRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PostCommentService {
    private final PostCommentRepository postCommentRepository;

    public PostCommentService(PostCommentRepository postCommentRepository) {
        this.postCommentRepository = postCommentRepository;
    }

    public List<PostComment> getPostCommentsByPostId(Long postId, String type) {
        List<Object[]> rawPosts = postCommentRepository.findPostCommentWithNickName(postId, type);
    
        List<PostComment> postComments = new ArrayList<>();
        Map<Long, PostComment> commentMap = new HashMap<>();

        for (Object[] row : rawPosts) {
            PostComment postComment = new PostComment();
            postComment.setId(row[0] != null ? ((Number) row[0]).longValue() : null);
            postComment.setCommentText(row[1] != null ? (String) row[1] : "");
            postComment.setNickName(row[2] != null ? (String) row[2] : "익명");
            postComment.setTimeAgo(row[3] != null ? (String) row[3] : "방금 전");
            postComment.setUserId(row[4] != null ? ((Number) row[4]).longValue() : null);
            postComment.setParentId(row[5] != null ? ((Number) row[5]).longValue() : null);
            postComments.add(postComment);

            commentMap.put(postComment.getId(), postComment);
        }

        for (PostComment comment : commentMap.values()) {
            if (comment.getParentId() != null) {
                PostComment parent = commentMap.get(comment.getParentId());
                if (parent != null) {
                    parent.getReplies().add(comment);
                }
            }
        }
        return postComments;
    }

    // 특정 게시글의 댓글 가져오기
    public List<PostComment> getCommentsByPostId(Long postId) {
        return postCommentRepository.findByRelationIdOrderByCreatedAtAsc(postId);
    }

    // 댓글 및 대댓글 조회 (계층형)
    public List<PostComment> getPostCommentsWithReplies(Long relationId, String type) {
        List<PostComment> comments = postCommentRepository.findByRelationIdOrderByCreatedAtAsc(relationId);
        comments.sort(Comparator.comparing(PostComment::getParentId, Comparator.nullsFirst(Long::compareTo))
                            .thenComparing(PostComment::getCreatedAt));
        return comments;
    }

    // 부모 댓글 + 대댓글 모두 가져오기
    public List<PostComment> getCommentsWithReplies(Long postId) {
        List<PostComment> comments = postCommentRepository.findByRelationIdAndParentIdIsNull(postId);
        for (PostComment comment : comments) {
            List<PostComment> replies = postCommentRepository.findByParentId(comment.getId());
            comment.setReplies(replies);
        }
        return comments;
    }

    // 특정 댓글의 대댓글만 가져오기
    public List<PostComment> getRepliesByCommentId(Long parentId) {
        //return postCommentRepository.findByParentId(parentId);
        List<Object[]> rawReplies = postCommentRepository.findRepliesWithNickName(parentId);
        List<PostComment> replies = new ArrayList<>();
        for (Object[] row : rawReplies) {
            PostComment reply = new PostComment();
            reply.setId(row[0] != null ? ((Number) row[0]).longValue() : null);
            reply.setCommentText(row[1] != null ? (String) row[1] : "");
            reply.setNickName(row[2] != null ? (String) row[2] : "익명");
            reply.setTimeAgo(row[3] != null ? (String) row[3] : "방금 전");
            reply.setUserId(row[4] != null ? ((Number) row[4]).longValue() : null);
            reply.setParentId(row[5] != null ? ((Number) row[5]).longValue() : null);

            replies.add(reply);
        }
        return replies;
    }

    // 특정 댓글(ID)로 댓글 가져오기
    public PostComment getCommentById(Long id) {
        Object row = postCommentRepository.findCommentByIdWithNickName(id);
        if (row == null) throw new EntityNotFoundException("댓글을 찾을 수 없습니다.");

        Object[] data = (Object[]) row;  // Object[]로 변환
        PostComment comment = new PostComment();
        comment.setId(data[0] != null ? ((Number) data[0]).longValue() : null);
        comment.setCommentText(data[1] != null ? (String) data[1] : "");
        comment.setNickName(data[2] != null ? (String) data[2] : "익명");
        comment.setTimeAgo(data[3] != null ? (String) data[3] : "방금 전");
        comment.setUserId(data[4] != null ? ((Number) data[4]).longValue() : null);
        comment.setParentId(data[5] != null ? ((Number) data[5]).longValue() : null);

        return comment;
}

    // 댓글 저장
    public PostComment saveComment(PostComment comment) {
        return postCommentRepository.save(comment);
    }

    // 댓글 작성
    @Transactional
    public PostComment addComment(PostComment comment) {
        return postCommentRepository.save(comment);
    }

    // 댓글 수정
    public PostComment updateComment(Long id, PostComment postComment) {
        return postCommentRepository.findById(id)
                .map(comment -> {
                    System.out.println(id);
                    System.out.println(postComment.getCommentText());
                    comment.setCommentText(postComment.getCommentText());
                    return postCommentRepository.save(comment);
                })
                .orElseThrow(() -> new RuntimeException("Comment not found with id " + id));
    }

    // 댓글 삭제
    @Transactional
    public void deleteCommentById(Long id) {
        postCommentRepository.deleteById(id);
    }
}
