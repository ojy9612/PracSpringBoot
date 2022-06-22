package com.homework.hanghae99homework02.controller;

import com.homework.hanghae99homework02.security.UserDetailsImpl;
import com.homework.hanghae99homework02.service.LikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LikesController {

    private final LikesService likesService;

    @Autowired
    public LikesController(LikesService likesService) {
        this.likesService = likesService;
    }


    @GetMapping("/api/board/{boardId}/like")
    public void GoLikes(@PathVariable Long boardId,
                          @AuthenticationPrincipal UserDetailsImpl userDetails){
        ControllerFunc.userchecker(userDetails);
        likesService.GoLikes(boardId,userDetails);
    }

}
