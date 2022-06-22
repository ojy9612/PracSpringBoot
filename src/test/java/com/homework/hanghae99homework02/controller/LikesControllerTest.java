package com.homework.hanghae99homework02.controller;

import com.homework.hanghae99homework02.model.Board;
import com.homework.hanghae99homework02.model.User;
import com.homework.hanghae99homework02.repository.BoardRepository;
import com.homework.hanghae99homework02.repository.UserRepository;
import com.homework.hanghae99homework02.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class LikesControllerTest {

    @Autowired
    LikesController likesController;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRepository boardRepository;

    @Nested
    @DisplayName("좋아요 기능 Test")
    class RegisterTest {

        @Nested
        @DisplayName("성공 테스트")
        class SuccessTest {

            @Test
            @Transactional
            @DisplayName("좋아요 생성")
            void 좋아요생성(){
                //given
                List<User> userList = new ArrayList<>();
                List<UserDetailsImpl> userDetailsList = new ArrayList<>();

                for (int i = 0; i < 10; i++){
                    User user = User.builder()
                            .email("email"+i+"@test.com")
                            .nickname("nickname"+i)
                            .password("password!"+i)
                            .roles(Collections.singletonList("ROLE_USER"))
                            .build();
                    userList.add(user);
                    userRepository.save(user);

                    UserDetailsImpl userDetails = new UserDetailsImpl(user);
                    userDetailsList.add(userDetails);
                }

                Board board = Board.builder()
                        .user(userList.get(0))
                        .content("내용")
                        .imageKey("")
                        .imageLink("")
                        .layout(1)
                        .build();

                boardRepository.save(board);

                //when
                for(int i =0; i < 10; i++){
                    likesController.GoLikes(board.getBoard_id(),userDetailsList.get(i));
                }
                //then
                assertThat(board.getLikesList().size()).isEqualTo(10);
            }

//            @Test
//            @Transactional
//            @DisplayName("좋아요 취소")
//            void 좋아요취소(){
//                //given
//                List<User> userList = new ArrayList<>();
//                List<UserDetailsImpl> userDetailsList = new ArrayList<>();
//
//                for (int i = 0; i < 10; i++){
//                    User user = User.builder()
//                            .email("email"+i+"@test.com")
//                            .nickname("nickname"+i)
//                            .password("password!"+i)
//                            .roles(Collections.singletonList("ROLE_USER"))
//                            .build();
//                    userList.add(user);
//                    userRepository.save(user);
//
//                    UserDetailsImpl userDetails = new UserDetailsImpl(user);
//                    userDetailsList.add(userDetails);
//                }
//
//                Board board = Board.builder()
//                        .user(userList.get(0))
//                        .content("내용")
//                        .imageKey("")
//                        .imageLink("")
//                        .layout(1)
//                        .build();
//
//                boardRepository.save(board);
//
//                //when
//                for(int i =0; i < 10; i++){
//                    likesController.GoLikes(board.getBoard_id(),userDetailsList.get(i));
//                }
//                for(int i =0; i < 4; i++){
//                    likesController.GoLikes(board.getBoard_id(),userDetailsList.get(i));
//                }
//
//                //then
//                assertThat(board.getLikesList().size()).isEqualTo(6);
//            }

        }

        @Nested
        @DisplayName("실패 테스트")
        class FailTest {

            @Test
            @Transactional
            @DisplayName("유저X 좋아요")
            void 유저X좋아요(){
                //given
                User user1 = User.builder()
                        .email("email1@test.com")
                        .nickname("nickname1")
                        .password("password!1")
                        .roles(Collections.singletonList("ROLE_USER"))
                        .build();
                User user2 = User.builder()
                        .email("email2@test.com")
                        .nickname("nickname2")
                        .password("password!2")
                        .roles(Collections.singletonList("ROLE_USER"))
                        .build();

                UserDetailsImpl userDetails1 = new UserDetailsImpl(user1);
                UserDetailsImpl userDetails2 = new UserDetailsImpl(user2);

                userRepository.save(user1);

                Board board = Board.builder()
                        .user(user1)
                        .content("내용")
                        .imageKey("")
                        .imageLink("")
                        .layout(1)
                        .build();

                boardRepository.save(board);

                //when
                IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                        () -> likesController.GoLikes(board.getBoard_id(), userDetails2));
                //then
                assertThat(e.getMessage()).isEqualTo("해당 유저를 찾을 수 없습니다.");
            }

            @Test
            @Transactional
            @DisplayName("게시글X 좋아요")
            void 게시글X좋아요() {
                //given
                User user = User.builder()
                        .email("email1@test.com")
                        .nickname("nickname1")
                        .password("password!1")
                        .roles(Collections.singletonList("ROLE_USER"))
                        .build();


                UserDetailsImpl userDetails = new UserDetailsImpl(user);

                userRepository.save(user);

                Board board = Board.builder()
                        .user(user)
                        .content("내용")
                        .imageKey("")
                        .imageLink("")
                        .layout(1)
                        .build();

                boardRepository.save(board);

                //when
                IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                        () -> likesController.GoLikes(-1L, userDetails));
                //then
                assertThat(e.getMessage()).isEqualTo("게시글 ID를 찾을 수 없습니다.");

            }

        }

    }
}