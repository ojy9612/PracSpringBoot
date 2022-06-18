package com.homework.hanghae99homework02.controller;

import com.homework.hanghae99homework02.dto.BoardDto;
import com.homework.hanghae99homework02.dto.BoardResponseDto;
import com.homework.hanghae99homework02.exception.eset.WrongIdException;
import com.homework.hanghae99homework02.model.User;
import com.homework.hanghae99homework02.repository.UserRepository;
import com.homework.hanghae99homework02.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static com.homework.hanghae99homework02.exception.ErrorCode.JUST_HANDLE_SELF;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class BoardControllerTest {

    @Autowired UserRepository userRepository;
    @Autowired BoardController boardController;


    private User baseUser;
    private UserDetailsImpl baseUserDetails;
    private MultipartFile nullMultipartFile;


    @Nested
    @DisplayName("게시글 CRUD Test")
    class RegisterTest {

        @BeforeEach
        void setUp(){
            baseUser = User.builder()
                    .email("email@test.com")
                    .nickname("nickname")
                    .password("password!")
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();


            baseUserDetails = new UserDetailsImpl(baseUser);

            nullMultipartFile = new MultipartFile() {
                @Override
                public String getName() {return null;}
                @Override
                public String getOriginalFilename() {return null;}
                @Override
                public String getContentType() {return null;}
                @Override
                public boolean isEmpty() {return true;}
                @Override
                public long getSize() {return 0;}
                @Override
                public byte[] getBytes() throws IOException {return new byte[0];}
                @Override
                public InputStream getInputStream() throws IOException {return null;}
                @Override
                public void transferTo(File dest) throws IOException, IllegalStateException {}
            };

        }


        @Nested
        @DisplayName("성공 테스트")
        class SuccessTest {

            @Test
            @Transactional
            @DisplayName("게시글 생성 & 조회")
            void 게시글생성및조회(){
                //when
                userRepository.save(baseUser);
                BoardResponseDto boardResponseDto = boardController.createBoard(
                        nullMultipartFile,
                        1,
                        "내용",
                        baseUserDetails
                );

                //then
                assertThat(boardResponseDto.getContent()).isEqualTo("내용");
                assertThat(boardResponseDto.getLayout()).isEqualTo(1);
            }

            @Test
            @Transactional
            @DisplayName("게시글 수정")
            void 게시글수정(){
                //given
                BoardDto boardDto = BoardDto.builder()
                        .layout(2)
                        .content("수정된 내용")
                        .build();

                //when
                userRepository.save(baseUser);
                BoardResponseDto createBRD = boardController.createBoard(
                        nullMultipartFile,
                        1,
                        "내용",
                        baseUserDetails
                );

                BoardResponseDto boardResponseDto = boardController.updateBoard(
                        nullMultipartFile,
                        createBRD.getBoard_id(),
                        boardDto,
                        baseUserDetails
                );

                //then
                assertThat(boardResponseDto.getContent()).isEqualTo("수정된 내용");
                assertThat(boardResponseDto.getLayout()).isEqualTo(2);
            }

            @Test
            @Transactional
            @DisplayName("게시글 삭제")
            void 게시글삭제(){
                //when
                userRepository.save(baseUser);
                BoardResponseDto createBRD = boardController.createBoard(
                        nullMultipartFile,
                        1,
                        "내용",
                        baseUserDetails
                );
                Long id = createBRD.getBoard_id();
                Long removeBoardId = boardController.removeBoard(id, baseUserDetails);

                //then
                assertThat(removeBoardId).isEqualTo(id);
            }

            @Test
            @Transactional
            @DisplayName("게시글 전체 목록 조회")
            void 게시글전체목록조회(){
                //given
                List<BoardResponseDto> allBoard1 = boardController.getAllBoard();

                //when
                userRepository.save(baseUser);

                for (int i = 0; i < 10; i++){
                    boardController.createBoard(nullMultipartFile, 1, "내용"+i, baseUserDetails);
                }

                List<BoardResponseDto> allBoard2 = boardController.getAllBoard();
                //then
                assertThat(allBoard2.size() - allBoard1.size()).isEqualTo(10);
            }
        }

        @Nested
        @DisplayName("실패 테스트")
        class FailTest {

            @Test
            @Transactional
            @DisplayName("아이디X, 게시글 생성")
            void 아이디X게시글생성() {
                //given
                User user = User.builder()
                        .email("")
                        .build();

                UserDetailsImpl nullUserDetails = new UserDetailsImpl(user);

                //when
                IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                        () -> boardController.createBoard(
                                nullMultipartFile,
                                1,
                                "내용",
                                nullUserDetails));
                //then
                assertThat(e.getMessage()).isEqualTo("해당 유저를 찾을 수 없습니다.");
            }

            @Test
            @Transactional
            @DisplayName("다른사람 게시글 수정")
            void 다른사람게시글수정() {
                //given
                User user2 = User.builder()
                        .email("email2@test.com")
                        .nickname("nickname2")
                        .password("password!2")
                        .roles(Collections.singletonList("ROLE_USER"))
                        .build();

                UserDetailsImpl userDetails2 = new UserDetailsImpl(user2);

                BoardDto boardDto = BoardDto.builder()
                        .layout(2)
                        .content("수정된 내용")
                        .build();

                //when
                userRepository.save(baseUser);
                userRepository.save(user2);
                BoardResponseDto createBRD = boardController.createBoard(
                        nullMultipartFile,
                        1,
                        "내용",
                        baseUserDetails);
                WrongIdException e = assertThrows(WrongIdException.class,
                        () -> boardController.updateBoard(
                                nullMultipartFile,
                                createBRD.getBoard_id(),
                                boardDto,
                                userDetails2));

                //then
                assertThat(e.getErrorCode()).isEqualTo(JUST_HANDLE_SELF);
            }

            @Test
            @Transactional
            @DisplayName("다른사람 게시글 삭제")
            void 다른사람게시글삭제() {
                //given
                User user2 = User.builder()
                        .email("email2@test.com")
                        .nickname("nickname2")
                        .password("password!2")
                        .roles(Collections.singletonList("ROLE_USER"))
                        .build();

                UserDetailsImpl userDetails2 = new UserDetailsImpl(user2);

                //when
                userRepository.save(baseUser);
                userRepository.save(user2);
                BoardResponseDto createBRD = boardController.createBoard(
                        nullMultipartFile,
                        1,
                        "내용",
                        baseUserDetails);

                WrongIdException e = assertThrows(WrongIdException.class,
                        () -> boardController.removeBoard(
                                createBRD.getBoard_id(),
                                userDetails2
                        ));
                //then
                assertThat(e.getErrorCode()).isEqualTo(JUST_HANDLE_SELF);

            }

        }

    }

}