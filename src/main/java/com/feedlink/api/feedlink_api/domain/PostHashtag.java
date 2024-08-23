package com.feedlink.api.feedlink_api.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_hashtag")
@Getter
@NoArgsConstructor
public class PostHashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postHashtagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="hashtag_id")
    private Hashtag hashtag;

    @Builder
    public PostHashtag(Long postHashtagId, Post post, Hashtag hashtag) {
        this.postHashtagId = postHashtagId;
        this.post = post;
        this.hashtag = hashtag;
    }
}
