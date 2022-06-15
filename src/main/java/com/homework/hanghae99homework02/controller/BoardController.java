package com.homework.hanghae99homework02.controller;


import com.homework.hanghae99homework02.dto.BoardDto;
import com.homework.hanghae99homework02.model.Board;
import com.homework.hanghae99homework02.security.UserDetailsImpl;
import com.homework.hanghae99homework02.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class BoardController {

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }
    private final BoardService boardService;


    @GetMapping("/api/board")
    public List<Board> getAllBoard(){

        return boardService.getAllBoard();
    }


    @PostMapping("/api/board")
    public Board createBoard(@RequestParam("image") MultipartFile multipartFile,
                             @RequestParam("layout") int layout,
                             @RequestParam("content") String content,
                             @AuthenticationPrincipal UserDetailsImpl userDetails){
        BoardDto boardDto = new BoardDto(layout,content);
        return boardService.createBoard(multipartFile,boardDto,userDetails);
    }


    @GetMapping("/api/board/{board_id}")
    public Board getOneBoard(@PathVariable Long board_id){

        return boardService.getOneBoard(board_id);
    }


    @DeleteMapping("/api/board/{board_id}")
    public Long removeBoard(@PathVariable Long board_id,
                            @AuthenticationPrincipal UserDetailsImpl userDetails){

        ControllerFunc.userchecker(userDetails);
        return boardService.removeBoard(board_id,userDetails);
    }


    @PutMapping("/api/board/{board_id}")
    public Board updateBoard(@RequestParam("image") MultipartFile multipartFile,
                             @PathVariable Long board_id,
                             @RequestBody BoardDto boardDto,
                             @AuthenticationPrincipal UserDetailsImpl userDetails){

        ControllerFunc.userchecker(userDetails);
        return boardService.updateBoard(multipartFile,board_id,boardDto,userDetails);
    }

}
