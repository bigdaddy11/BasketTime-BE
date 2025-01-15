package com.prod.main.baskettime.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class PostImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imagePaths; // 이미지 경로

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post; // Post와의 관계

    // 기본 생성자
    public PostImages() {}

    // imagePath만을 위한 생성자
    public PostImages(String imagePaths) {
        this.imagePaths = imagePaths;
    }

    // imagePath와 post를 모두 처리하는 생성자
    public PostImages(String imagePaths, Post post) {
        this.imagePaths = imagePaths;
        this.post = post;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImagePaths() {
        return imagePaths;
    }

    public void setImagePath(String imagePaths) {
        this.imagePaths = imagePaths;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
