package com.homework.hanghae99homework02.service;

import com.homework.hanghae99homework02.dto.RegisterDto;
import com.homework.hanghae99homework02.dto.UserDto;
import com.homework.hanghae99homework02.jwt.JwtTokenProvider;
import com.homework.hanghae99homework02.model.User;
import com.homework.hanghae99homework02.repository.UserRepository;
import com.homework.hanghae99homework02.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void registerUser(RegisterDto registerDto) {
        String email = registerDto.getEmail();
        // 회원 ID 중복 확인
        Optional<User> found = userRepository.findByEmail(email);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 ID 가 존재합니다.");
        }
        userRepository.save(User.builder()
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .roles(Collections.singletonList("ROLE_USER"))
                .email(email)
                .nickname(registerDto.getNickname())
                .build());
    }

    public String loginUser(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("ID를 찾을 수 없습니다."));
        if(!passwordEncoder.matches(userDto.getPassword(),user.getPassword())){
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return jwtTokenProvider.createToken(user.getEmail(), user.getRoles());
    }

}
