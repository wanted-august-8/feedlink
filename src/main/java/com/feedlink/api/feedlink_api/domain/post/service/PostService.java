package com.feedlink.api.feedlink_api.domain.post.service;

import com.feedlink.api.feedlink_api.domain.post.dto.PostDTO;
import com.feedlink.api.feedlink_api.domain.post.entity.Post;
import com.feedlink.api.feedlink_api.domain.post.enums.PostType;
import com.feedlink.api.feedlink_api.domain.post.repository.PostRepository;
import com.feedlink.api.feedlink_api.global.error.ErrorCode;
import com.feedlink.api.feedlink_api.global.exception.CustomException;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    private static final Map<String, String> SORT_FIELDS_MAPPING = Map.of(
        "created_at", "postCreateTime",
        "updated_at", "postUpdateTime",
        "like_count", "postLikeCnt",
        "share_count", "postShareCnt",
        "view_count", "postViewCnt"
    );

    private Sort createSort(String orderBy, String sortDirection) {
        String field = SORT_FIELDS_MAPPING.getOrDefault(orderBy, "postCreateTime");
        Sort.Direction direction = sortDirection != null && sortDirection.equalsIgnoreCase("asc")
            ? Sort.Direction.ASC
            : Sort.Direction.DESC;

        return Sort.by(direction, field);
    }

    @Transactional
    public Page<PostDTO> getAllPosts(String memberAccount, String hashtag, PostType type, String orderBy, String searchBy, String search, int pageCount, int page) {
        String effectiveHashtag = (hashtag != null) ? hashtag : memberAccount;

        Specification<Post> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.join("postHashtagList").join("hashtag").get("hashtagName"), effectiveHashtag));

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

        Sort sort = createSort(orderBy, "desc");
        Pageable pageable = PageRequest.of(page, pageCount, sort);

        Page<Post> posts = postRepository.findAll(spec, pageable);

        Page<PostDTO> postDTOs = posts.map(post -> {
            List<String> hashtags = post.getPostHashtagList().stream()
                .map(postHashtag -> postHashtag.getHashtag().getHashtagName())
                .collect(Collectors.toList());

            String limitedContent = post.getPostContent();
            if (limitedContent.length() > 20) {
                limitedContent = limitedContent.substring(0, 20) + "...";
            }

            return PostDTO.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .postContent(limitedContent)
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

    @Transactional
    public PostDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));

        post.setPostViewCnt(post.getPostViewCnt() + 1);
        postRepository.save(post);

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
    }
}
