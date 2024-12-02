package com.prod.main.baskettime.service;

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

    public List<Post> getPostsByCategoryId(Long categoryId) {
        if (categoryId == null) {
            return postRepository.findAllByOrderByIdAsc(); // 카테고리 ID가 없으면 전체 조회
        }
        return postRepository.findByCategoryIdOrderByIdAsc(categoryId); // 카테고리 ID로 조회
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
