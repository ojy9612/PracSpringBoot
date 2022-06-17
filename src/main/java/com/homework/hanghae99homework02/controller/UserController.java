package com.homework.hanghae99homework02.controller;


import com.homework.hanghae99homework02.dto.RegisterDto;
import com.homework.hanghae99homework02.dto.UserDto;
import com.homework.hanghae99homework02.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/api/login")
    public String loginUser(@Valid @RequestBody UserDto userDto) {
        return userService.loginUser(userDto);
    }

    @PostMapping("/api/register")
    public void registerUser(@Valid @RequestBody RegisterDto registerDto){
        registerDto.isCheckPassword();
        userService.registerUser(registerDto);
    }

}
