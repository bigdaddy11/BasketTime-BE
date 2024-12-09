package com.prod.main.baskettime.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.prod.main.baskettime.entity.Post;
import com.prod.main.baskettime.repository.PostRepository;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // public List<Post> getPostsByCategoryId(Long categoryId) {
    //     if (categoryId == null) {
    //         return postRepository.findAllByOrderByIdAsc(); // 카테고리 ID가 없으면 전체 조회
    //     }
    //     return postRepository.findByCategoryIdOrderByIdAsc(categoryId); // 카테고리 ID로 조회
    // }

    public List<Post> getPostsByCategoryId(Long categoryId) {
        List<Object[]> rawPosts = postRepository.findPostsWithNickName(categoryId);

        // Object[] 데이터를 Post 엔티티로 변환
        List<Post> posts = new ArrayList<>();
        for (Object[] row : rawPosts) {
            Post post = new Post();
            post.setId(((Number) row[0]).longValue());
            post.setCategoryId(((Number) row[1]).longValue());
            post.setContent((String) row[2]);
            post.setCreatedAt(row[3] != null ? ((java.sql.Timestamp) row[3]).toLocalDateTime() : null);
            post.setTitle((String) row[4]);
            post.setUpdatedAt(row[5] != null ? ((java.sql.Timestamp) row[5]).toLocalDateTime() : null);
            post.setUserId(((Number) row[6]).longValue());
            post.setNickName((String) row[7]);
            post.setCategoryName((String) row[8]);
            post.setTimeAgo((String) row[9]);
            posts.add(post);
        }
        return posts;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Post updatePost(Long id, Post updatedPost) {
        return postRepository.findById(id)
                .map(post -> {
                    post.setCategoryId(updatedPost.getCategoryId());
                    post.setTitle(updatedPost.getTitle());
                    post.setContent(updatedPost.getContent());
                    return postRepository.save(post);
                })
                .orElseThrow(() -> new RuntimeException("Post not found with id " + id));
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
