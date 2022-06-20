package com.homework.hanghae99homework02.controller;


import com.homework.hanghae99homework02.controller.UserController;
import com.homework.hanghae99homework02.dto.RegisterDto;
import com.homework.hanghae99homework02.dto.UserDto;
import com.homework.hanghae99homework02.jwt.JwtTokenProvider;
import com.homework.hanghae99homework02.model.User;
import com.homework.hanghae99homework02.repository.UserRepository;
import com.homework.hanghae99homework02.security.UserDetailsImpl;
import com.homework.hanghae99homework02.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class UserControllerTest {

    @Autowired
    UserController userController;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Nested
    @DisplayName("유저 기능 Test")
    class RegisterTest {

        @Nested
        @DisplayName("성공 테스트")
        class SuccessTest {

            @Test
            @Transactional
            @DisplayName("유저 생성")
            void RegisterUser() {
                RegisterDto registerDto = RegisterDto.builder()
                        .email("email@test.com")
                        .nickname("nickname")
                        .password("password!")
                        .build();

                User user = User.builder()
                        .email(registerDto.getEmail())
                        .nickname(registerDto.getNickname())
                        .password(registerDto.getPassword())
                        .roles(Collections.singletonList("ROLE_USER"))
                        .build();

                User result = userRepository.save(user);
                assertThat(result.getEmail()).isEqualTo(registerDto.getEmail());
                assertThat(result.getNickname()).isEqualTo(registerDto.getNickname());
                assertThat(result.getPassword()).isEqualTo(registerDto.getPassword());
            }

        }

        @Nested
        @DisplayName("실패 테스트")
        class FailTest {

            @Test
            @Transactional
            @DisplayName("중복Email")
            void SameEmail(){
                //given
                RegisterDto registerDto1 = RegisterDto.builder()
                        .email("email@test.com")
                        .nickname("nickname")
                        .password("password!")
                        .build();


                RegisterDto registerDto2 = RegisterDto.builder()
                        .email("email@test.com")
                        .nickname("nickname")
                        .password("password!")
                        .build();

                //when
                userController.registerUser(registerDto1);
                IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                        () -> userController.registerUser(registerDto2));

                //then
                assertThat(e.getMessage()).isEqualTo("중복된 사용자 ID 가 존재합니다.");

            }

        }

    }

    @Nested
    @DisplayName("로그인")
    class LoginTest{

        @Nested
        @DisplayName("성공 테스트")
        class SuccessTest{

            @Test
            @Transactional
            @DisplayName("로그인 확인")
            void LoginUser(){
                //given
                userController.registerUser(RegisterDto.builder()
                        .email("email@test.com")
                        .nickname("nickname")
                        .password("password!")
                        .build());
                UserDto userDto = UserDto.builder()
                        .email("email@test.com")
                        .password("password!")
                        .build();

                //when
                String token = userController.loginUser(userDto);

                UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsServiceImpl.
                        loadUserByUsername(jwtTokenProvider.getUserPk(token));

                //then
                assertThat(userDetails.getUsername()).isEqualTo("email@test.com");
                assertThat(userDetails.getNickName()).isEqualTo("nickname");
            }

        }

        @Nested
        @DisplayName("실패 테스트")
        class FailTest{

            @Test
            @Transactional
            @DisplayName("아이디 오류")
            void NoId(){
                //given
                userController.registerUser(RegisterDto.builder()
                        .email("email@test.com")
                        .nickname("nickname")
                        .password("password!")
                        .build());
                UserDto userDto = UserDto.builder()
                        .email("wrongEmail@test.com")
                        .password("password!")
                        .build();

                //when
                IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                        () -> userController.loginUser(userDto));

                //then
                assertThat(e.getMessage()).isEqualTo("ID를 찾을 수 없습니다.");

            }

            @Test
            @Transactional
            @DisplayName("비밀번호 오류")
            void WrongPassword(){
                //given
                userController.registerUser(RegisterDto.builder()
                        .email("email@test.com")
                        .nickname("nickname")
                        .password("password!")
                        .build());
                UserDto userDto = UserDto.builder()
                        .email("email@test.com")
                        .password("worngPassword")
                        .build();

                //when
                IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                        () -> userController.loginUser(userDto));

                //then
                assertThat(e.getMessage()).isEqualTo("잘못된 비밀번호입니다.");

            }

        }


    }
}