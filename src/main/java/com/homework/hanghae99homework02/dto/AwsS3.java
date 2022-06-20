package com.homework.hanghae99homework02.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class AwsS3 {
    private String key;
    private String path;

    public AwsS3() {

    }

    @Builder
    public AwsS3(String key, String path) {
        this.key = key;
        this.path = path;
    }
}