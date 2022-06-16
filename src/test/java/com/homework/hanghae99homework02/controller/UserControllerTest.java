package com.homework.hanghae99homework02.controller;

import com.homework.hanghae99homework02.dto.RegisterDto;
import com.homework.hanghae99homework02.model.User;
import com.homework.hanghae99homework02.repository.UserRepository;
import com.homework.hanghae99homework02.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserControllerTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Nested
    @DisplayName("회원가입")
    class registerTest {

        @Nested
        @DisplayName("성공 테스트")
        class SuccessTest {

//            @Test
//            @DisplayName("유저 생성")
//            void 회원가입() {
//                RegisterDto registerDto = new RegisterDto();
//                registerDto.setEmail("t1@t2.t3");
//                registerDto.setNickname("nickname");
//                registerDto.setPassword("password");
//
//
//                User user = User.builder()
//                        .email(registerDto.getEmail())
//                        .nickname(registerDto.getNickname())
//                        .password(registerDto.getPassword())
//                        .roles(Collections.singletonList("ROLE_USER"))
//                        .build();
//
//                User result = userRepository.save(user);
//                assertThat(result.getEmail()).isEqualTo(registerDto.getEmail());
//                assertThat(result.getNickname()).isEqualTo(registerDto.getNickname());
//                assertThat(result.getPassword()).isEqualTo(registerDto.getPassword());
//            }

        }

        @Nested
        @DisplayName("실패 테스트")
        class FailTest {

//            @Test
//            @DisplayName("중복Email")
//            void 중복된Email(){
//                RegisterDto registerDto1 = new RegisterDto();
//                registerDto1.setEmail("email");
//                registerDto1.setNickname("nickname");
//                registerDto1.setPassword("password");
//
//                userService.registerUser(registerDto1);
//
//                RegisterDto registerDto2 = new RegisterDto();
//                registerDto2.setEmail("email");
//                registerDto2.setNickname("nickname");
//                registerDto2.setPassword("password");
//
//                userService.registerUser(registerDto2);
//
//            }
        }
    }
}