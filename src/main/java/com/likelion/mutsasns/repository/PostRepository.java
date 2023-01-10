package com.likelion.mutsasns.repository;

import com.likelion.mutsasns.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findAllByUserId(Integer userId, Pageable pageable);
}