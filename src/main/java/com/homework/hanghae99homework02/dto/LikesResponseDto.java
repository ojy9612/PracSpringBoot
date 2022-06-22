package com.homework.hanghae99homework02.dto;

import com.homework.hanghae99homework02.model.Likes;
import lombok.Getter;

@Getter
public class LikesResponseDto {

    private final String user_email;
    private final String user_nickname;

    public LikesResponseDto(Likes likes) {
        this.user_email = likes.getUser().getEmail();
        this.user_nickname = likes.getUser().getNickname();
    }
}
