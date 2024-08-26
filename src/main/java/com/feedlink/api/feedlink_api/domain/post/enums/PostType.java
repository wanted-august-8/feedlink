package com.feedlink.api.feedlink_api.domain.post.enums;

import lombok.Getter;

@Getter
public enum PostType {
    TWITTER("twitter"),
    FACEBOOK("facebook"),
    INSTAGRAM("instagram"),
    THREADS("threads");

    private final String value;

    PostType(String value) {
        this.value = value;
    }
}

