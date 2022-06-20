package com.homework.hanghae99homework02.service;

import com.homework.hanghae99homework02.dto.RegisterDto;
import com.homework.hanghae99homework02.jwt.JwtTokenProvider;
import com.homework.hanghae99homework02.model.User;
import com.homework.hanghae99homework02.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @Nested
    @DisplayName("회원가입")
    class registerTest{

        @Nested
        @DisplayName("성공 테스트")
        class SuccessTest{

            @Test
            @DisplayName("유저 생성")
            void 유저생성(){
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

                when(userRepository.save(any(User.class)))
                        .thenReturn(user);

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
            @DisplayName("중복Email")
            void 중복된Email(){
                User user1 = User.builder()
                        .email("email")
                        .nickname("nickname")
                        .password("password")
                        .roles(Collections.singletonList("ROLE_USER"))
                        .build();

                User user2 = User.builder()
                        .email("email")
                        .nickname("nickname")
                        .password("password")
                        .roles(Collections.singletonList("ROLE_USER"))
                        .build();



            }
        }
    }


}