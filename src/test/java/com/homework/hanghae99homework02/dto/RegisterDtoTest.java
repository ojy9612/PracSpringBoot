package com.homework.hanghae99homework02.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterDtoTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("회원가입 Dto 유효성 검사")
    class ValidationTest{

        @Test
        @DisplayName("성공 테스트")
        void 회원가입성공(){
            //given
            RegisterDto registerDto = RegisterDto.builder()
                    .email("email@test.com")
                    .password("password!")
                    .nickname("nickname")
                    .build();
            //when
            Set<ConstraintViolation<RegisterDto>> violations = validator.validate(registerDto);
            //then
            assertThat(violations.size()).isEqualTo(0);
        }

        @Test
        @DisplayName("이메일 오류1")
        void 이메일오류1(){
            //given
            RegisterDto registerDto = RegisterDto.builder()
                    .email("email@@test.com")
                    .password("password!")
                    .nickname("nickname")
                    .build();
            //when
            Set<ConstraintViolation<RegisterDto>> violations = validator.validate(registerDto);
            //then
            for (ConstraintViolation<RegisterDto> violation : violations) {
                System.err.println(violation.getMessage());
            }
            assertThat(violations.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("이메일 오류2")
        void 이메일오류2(){
            //given
            RegisterDto registerDto = RegisterDto.builder()
                    .email("email.test")
                    .password("password!")
                    .nickname("nickname")
                    .build();
            //when
            Set<ConstraintViolation<RegisterDto>> violations = validator.validate(registerDto);
            //then
            for (ConstraintViolation<RegisterDto> violation : violations) {
                System.err.println(violation.getMessage());
            }
            assertThat(violations.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("이메일 오류3")
        void 이메일오류3(){
            //given
            RegisterDto registerDto = RegisterDto.builder()
                    .email("")
                    .password("password!")
                    .nickname("nickname")
                    .build();
            //when
            Set<ConstraintViolation<RegisterDto>> violations = validator.validate(registerDto);
            //then
            for (ConstraintViolation<RegisterDto> violation : violations) {
                System.err.println(violation.getMessage());
            }
            assertThat(violations.size()).isEqualTo(2);
        }

        @Test
        @DisplayName("비밀번호 오류1")
        void 비밀번호오류1(){
            //given
            RegisterDto registerDto = RegisterDto.builder()
                    .email("email@test.com")
                    .password("pas")
                    .nickname("nickname")
                    .build();
            //when
            Set<ConstraintViolation<RegisterDto>> violations = validator.validate(registerDto);
            //then
            for (ConstraintViolation<RegisterDto> violation : violations) {
                System.err.println(violation.getMessage());
            }
            assertThat(violations.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("비밀번호 오류2")
        void 비밀번호오류2(){
            //given
            RegisterDto registerDto = RegisterDto.builder()
                    .email("email@test.com")
                    .password("emailpassword")
                    .nickname("nickname")
                    .build();
            //when
            Set<ConstraintViolation<RegisterDto>> violations = validator.validate(registerDto);
            //then
            for (ConstraintViolation<RegisterDto> violation : violations) {
                System.err.println(violation.getMessage());
            }
            assertThat(violations.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("비밀번호 오류3")
        void 비밀번호오류3(){
            //given
            RegisterDto registerDto = RegisterDto.builder()
                    .email("email@test.com")
                    .password("")
                    .nickname("nickname")
                    .build();
            //when
            Set<ConstraintViolation<RegisterDto>> violations = validator.validate(registerDto);
            //then
            for (ConstraintViolation<RegisterDto> violation : violations) {
                System.err.println(violation.getMessage());
            }
            assertThat(violations.size()).isEqualTo(2);
        }

        @Test
        @DisplayName("닉네임 오류1")
        void 닉네임오류1(){
            //given
            RegisterDto registerDto = RegisterDto.builder()
                    .email("email@test.com")
                    .password("password!")
                    .nickname("한글은")
                    .build();
            //when
            Set<ConstraintViolation<RegisterDto>> violations = validator.validate(registerDto);
            //then
            for (ConstraintViolation<RegisterDto> violation : violations) {
                System.err.println(violation.getMessage());
            }
            assertThat(violations.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("닉네임 오류2")
        void 닉네임오류2(){
            //given
            RegisterDto registerDto = RegisterDto.builder()
                    .email("email@test.com")
                    .password("password!")
                    .nickname("")
                    .build();
            //when
            Set<ConstraintViolation<RegisterDto>> violations = validator.validate(registerDto);
            //then
            for (ConstraintViolation<RegisterDto> violation : violations) {
                System.err.println(violation.getMessage());
            }
            assertThat(violations.size()).isEqualTo(3);
        }


    }



}