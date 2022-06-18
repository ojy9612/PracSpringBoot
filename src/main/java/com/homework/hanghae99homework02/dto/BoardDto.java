package com.homework.hanghae99homework02.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
public class BoardDto {

    @NotEmpty(message = "layout 비었어요.")
    private int layout;

    @NotEmpty(message = "content 비었어요.")
    private String content;

    @Builder
    public BoardDto(int layout, String content) {
        this.layout = layout;
        this.content = content;
    }
}
