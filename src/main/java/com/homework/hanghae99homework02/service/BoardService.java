package com.homework.hanghae99homework02.service;

import com.homework.hanghae99homework02.dto.AwsS3;
import com.homework.hanghae99homework02.dto.BoardDto;
import com.homework.hanghae99homework02.dto.BoardResponseDto;
import com.homework.hanghae99homework02.exception.eset.WrongIdException;
import com.homework.hanghae99homework02.model.Board;
import com.homework.hanghae99homework02.model.User;
import com.homework.hanghae99homework02.repository.BoardRepository;
import com.homework.hanghae99homework02.repository.UserRepository;
import com.homework.hanghae99homework02.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.homework.hanghae99homework02.exception.ErrorCode.JUST_HANDLE_SELF;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;

    @Autowired
    public BoardService(BoardRepository boardRepository, UserRepository userRepository, AwsS3Service awsS3Service) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.awsS3Service = awsS3Service;
    }

    public List<BoardResponseDto> getAllBoard(){
        List<Board> boardList = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();
        for (Board board: boardList){
            boardResponseDtoList.add(new BoardResponseDto(board));
        }
        return boardResponseDtoList;
    }

    @Transactional
    public BoardResponseDto createBoard(MultipartFile multipartFile, BoardDto boardDto, UserDetailsImpl userDetailsImpl) {
        User user = userRepository.findByEmail(userDetailsImpl.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다.")
        );


        Board board;

        AwsS3 awsS3;
        try {
            awsS3 = awsS3Service.upload(multipartFile,"mydir");
        } catch (IOException e) {
            throw new RuntimeException("올바르지 않은 이미지 파일 입니다.");
        }

        board = new Board(awsS3.getPath(), awsS3.getKey(), boardDto.getContent(), boardDto.getLayout(), user);

        return new BoardResponseDto(boardRepository.save(board));


    }


    public BoardResponseDto getOneBoard(Long board_id) {
        Board board = boardRepository.findById(board_id).orElseThrow(
                () -> new IllegalArgumentException("ID를 찾을 수 없습니다.")
        );
        return new BoardResponseDto(board);
    }


    @Transactional
    public Long removeBoard(Long board_id, UserDetailsImpl userDetailsImpl) {
        Board board = boardRepository.findById(board_id).orElseThrow(
                () -> new IllegalArgumentException("게시글 ID를 찾을 수 없습니다 : " + board_id)
        );
        User user = userRepository.findByEmail(userDetailsImpl.getUsername()).orElseThrow(
                () -> new IllegalArgumentException(userDetailsImpl.getUsername() + " 해당 유저를 찾을 수 없습니다.")
        );

        if(Objects.equals(board.getUser().getId(), user.getId())){

            awsS3Service.remove(AwsS3.builder()
                    .key(board.getImageKey())
                    .path(board.getImageLink())
                    .build());


            boardRepository.delete(board);
            return board_id;
        }else{
            throw new WrongIdException(JUST_HANDLE_SELF);
        }

    }

    @Transactional
    public BoardResponseDto updateBoard(MultipartFile multipartFile, Long board_id, BoardDto boardDto, UserDetailsImpl userDetailsImpl) {
        Board board = boardRepository.findById(board_id).orElseThrow(
                () -> new IllegalArgumentException("게시글 ID를 찾을 수 없습니다 : " + board_id)
        );
        User user = userRepository.findByEmail(userDetailsImpl.getUsername()).orElseThrow(
                () -> new IllegalArgumentException(userDetailsImpl.getUsername() + " 해당 유저를 찾을 수 없습니다.")
        );

        if(Objects.equals(board.getUser().getId(), user.getId())){

            awsS3Service.remove(AwsS3.builder()
                    .path(board.getImageLink())
                    .key(board.getImageKey())
                    .build());

            AwsS3 awsS3;
            try {
                awsS3 = awsS3Service.upload(multipartFile, "mydir");
            } catch (IOException e) {
                throw new RuntimeException("올바르지 않은 이미지 파일 입니다.");
            }

            board.update(awsS3.getPath(), awsS3.getKey(), boardDto.getContent(), boardDto.getLayout());

            return new BoardResponseDto(board);
        }else{
            throw new WrongIdException(JUST_HANDLE_SELF);
        }
    }

}
