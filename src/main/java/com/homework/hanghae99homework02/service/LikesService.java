package com.homework.hanghae99homework02.service;

import com.homework.hanghae99homework02.model.Board;
import com.homework.hanghae99homework02.model.Likes;
import com.homework.hanghae99homework02.model.User;
import com.homework.hanghae99homework02.repository.BoardRepository;
import com.homework.hanghae99homework02.repository.LikesRepository;
import com.homework.hanghae99homework02.repository.UserRepository;
import com.homework.hanghae99homework02.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikesService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final LikesRepository likesRepository;

    @Autowired
    public LikesService(BoardRepository boardRepository, UserRepository userRepository, LikesRepository likesRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.likesRepository = likesRepository;
    }

    public void GoLikes(Long boardId, UserDetailsImpl userDetailsImpl){
        User user = userRepository.findByEmail(userDetailsImpl.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다.")
        );
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("게시글 ID를 찾을 수 없습니다.")
        );

        Optional<Likes> optionalLikes = likesRepository.findByBoardAndUser(board, user);

        if (optionalLikes.isPresent()){
            likesRepository.delete(optionalLikes.get());
        }else{
            Likes likes = new Likes(user, board);
            likesRepository.save(likes);
        }

    }


}
