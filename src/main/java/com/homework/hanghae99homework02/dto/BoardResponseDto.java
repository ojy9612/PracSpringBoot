package com.homework.hanghae99homework02.dto;

import com.homework.hanghae99homework02.model.Board;
import com.homework.hanghae99homework02.model.Likes;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardResponseDto {

    private final Long board_id;
    private final String imageLink;
    private final String content;
    private final String userNickname;
    private final String userEmail;
    private final int layout;
    private final List<LikesResponseDto> likes;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private List<LikesResponseDto> makeLikesList(List<Likes> LikesList){

        List<LikesResponseDto> likesResponseDtoList = new ArrayList<>();

        for(Likes likes1: LikesList){
            LikesResponseDto likesResponseDto = new LikesResponseDto(likes1);

            likesResponseDtoList.add(likesResponseDto);
        }

        return likesResponseDtoList;
    }

    public BoardResponseDto(Board board) {
        this.board_id = board.getBoard_id();
        this.imageLink = board.getImageLink();
        this.content = board.getContent();
        this.userNickname = board.getUser().getNickname();
        this.userEmail = board.getUser().getEmail();
        this.layout = board.getLayout();
        this.likes = makeLikesList(board.getLikesList());
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getModifiedAt();
    }
}
