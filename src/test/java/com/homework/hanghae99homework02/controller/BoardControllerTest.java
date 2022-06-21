package com.homework.hanghae99homework02.controller;

import com.homework.hanghae99homework02.dto.BoardDto;
import com.homework.hanghae99homework02.dto.BoardResponseDto;
import com.homework.hanghae99homework02.exception.eset.WrongIdException;
import com.homework.hanghae99homework02.model.User;
import com.homework.hanghae99homework02.repository.UserRepository;
import com.homework.hanghae99homework02.security.UserDetailsImpl;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.transaction.Transactional;

import java.io.*;
import java.nio.file.Files;
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
    @DisplayName("게시글 기능 Test")
    class RegisterTest {

        @BeforeEach
        void setUp() throws IOException {
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
            void CreateAndGetBoard() throws IOException {
                //given
                File file = new File("src\\test\\java\\com\\homework\\hanghae99homework02\\image\\" +
                        "test.jpg");
                FileItem fileItem = new DiskFileItem("test.jpg",
                        Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());

                try {
                     IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
                } catch (IOException ex) {
                    System.err.println("에러다 에러 ! ex.getMessage() = " + ex.getMessage());
                }

                MultipartFile multipartFile = new CommonsMultipartFile(fileItem);

                //when
                userRepository.save(baseUser);
                BoardResponseDto boardResponseDto1 = boardController.createBoard(
                        nullMultipartFile,
                        1,
                        "내용",
                        baseUserDetails
                );
                BoardResponseDto boardResponseDto2 = boardController.createBoard(
                        multipartFile,
                        1,
                        "내용",
                        baseUserDetails
                );

                //then
                assertThat(boardResponseDto1.getContent()).isEqualTo("내용");
                assertThat(boardResponseDto1.getLayout()).isEqualTo(1);

                assertThat(boardResponseDto2.getImageLink() != null).isEqualTo(true);
                assertThat(boardResponseDto2.getContent()).isEqualTo("내용");
                assertThat(boardResponseDto2.getLayout()).isEqualTo(1);

                boardController.removeBoard(boardResponseDto2.getBoard_id(), baseUserDetails);
            }

            @Test
            @Transactional
            @DisplayName("게시글 수정")
            void UpdateBoard(){
                //given

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
                        2,
                        "수정된 내용",
                        baseUserDetails
                );

                //then
                assertThat(boardResponseDto.getContent()).isEqualTo("수정된 내용");
                assertThat(boardResponseDto.getLayout()).isEqualTo(2);
            }

            @Test
            @Transactional
            @DisplayName("게시글 삭제")
            void DeleteBoard(){
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
            void GetAllBoard(){
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
            void NoIdCreateBoard() {
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
            void AnotherIdUpdateBoard() {
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
                        () -> boardController.updateBoard(
                                nullMultipartFile,
                                createBRD.getBoard_id(),
                                2,
                                "수정된 내용",
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

//            @Test
//            @Transactional
//            @DisplayName("텍스트파일 업로드")
//            void 텍스트파일업로드() throws IOException {
//                //given
//                userRepository.save(baseUser);
//
//                File file1 = new File("src\\test\\java\\com\\homework\\hanghae99homework02\\image\\" +
//                        "test_txt_file.txt");
//                File file2 = new File("src\\test\\java\\com\\homework\\hanghae99homework02\\image\\" +
//                        "test_txt_file.jpg");
//
//                FileItem fileItem1 = new DiskFileItem("test_txt_file.txt",
//                        Files.probeContentType(file1.toPath()), false, file1.getName(), (int) file1.length(), file1.getParentFile());
//                FileItem fileItem2 = new DiskFileItem("test_txt_file.jpg",
//                        Files.probeContentType(file2.toPath()), false, file2.getName(), (int) file2.length(), file2.getParentFile());
//
//                try {
//                    IOUtils.copy(new FileInputStream(file1), fileItem1.getOutputStream());
//                    IOUtils.copy(new FileInputStream(file2), fileItem2.getOutputStream());
//                } catch (IOException ex) {
//                    System.err.println("에러다 에러 ! ex.getMessage() = " + ex.getMessage());
//                }
//
//                MultipartFile txtMultipartFile = new CommonsMultipartFile(fileItem1);
//                MultipartFile fakeJpgMultipartFile = new CommonsMultipartFile(fileItem2);
//
//                //when
//                IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class,
//                        () -> boardController.createBoard(
//                                txtMultipartFile,
//                                1,
//                                "내용",
//                                baseUserDetails
//                        ));
//                IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class,
//                        () -> boardController.createBoard(
//                                fakeJpgMultipartFile,
//                                1,
//                                "내용",
//                                baseUserDetails
//                        ));
//
//                //then
//                assertThat(e1.getMessage()).isEqualTo("AwsS3 : 올바른 파일이 아닙니다.");
//                assertThat(e2.getMessage()).isEqualTo("AwsS3 : 올바른 파일이 아닙니다.");
//            }

        }

    }

}