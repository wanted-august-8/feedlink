package com.feedlink.api.feedlink_api.domain.post.service;

import com.feedlink.api.feedlink_api.domain.post.dto.PostDTO;
import com.feedlink.api.feedlink_api.domain.post.entity.Post;
import com.feedlink.api.feedlink_api.domain.post.enums.PostType;
import com.feedlink.api.feedlink_api.domain.post.repository.PostRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Transactional
    public Page<PostDTO> getAllPosts(String hashtag, PostType type, String orderBy, String searchBy, String search, int pageCount, int page) {
        Specification<Post> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (hashtag != null) {
                predicates.add(criteriaBuilder.equal(root.join("postHashtagList").join("hashtag").get("hashtagName"), hashtag));
            }

            if (type != null) {
                predicates.add(criteriaBuilder.equal(root.get("postType"), type));
            }

            if (search != null && !search.isEmpty()) {
                if ("title".equals(searchBy)) {
                    predicates.add(criteriaBuilder.like(root.get("postTitle"), "%" + search + "%"));
                } else if ("content".equals(searchBy)) {
                    predicates.add(criteriaBuilder.like(root.get("postContent"), "%" + search + "%"));
                } else {
                    Predicate titlePredicate = criteriaBuilder.like(root.get("postTitle"), "%" + search + "%");
                    Predicate contentPredicate = criteriaBuilder.like(root.get("postContent"), "%" + search + "%");
                    predicates.add(criteriaBuilder.or(titlePredicate, contentPredicate));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Sort sort = Sort.by(Sort.Direction.DESC, orderBy != null ? orderBy : "postCreateTime");
        Pageable pageable = PageRequest.of(page, pageCount, sort);

        Page<Post> posts = postRepository.findAll(spec, pageable);

        Page<PostDTO> postDTOs = posts.map(post -> {
            List<String> hashtags = post.getPostHashtagList().stream()
                .map(postHashtag -> postHashtag.getHashtag().getHashtagName())
                .collect(Collectors.toList());

            return PostDTO.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .postViewCnt(post.getPostViewCnt())
                .postLikeCnt(post.getPostLikeCnt())
                .postShareCnt(post.getPostShareCnt())
                .postType(post.getPostType())
                .postCreateTime(post.getPostCreateTime())
                .postUpdateTime(post.getPostUpdateTime())
                .hashtags(hashtags)
                .build();
        });

        return postDTOs;
    }
}
