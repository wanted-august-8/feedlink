package com.feedlink.api.feedlink_api.enums;

import lombok.Getter;

@Getter
public enum PostType {
    TWITTER("twitter"),
    FACEBOOK("facebook"),
    INSTAGRAM("instagram"),
    THREADS("threads");

    private String value;

    PostType(String value) {
        this.value = value;
    }
}

