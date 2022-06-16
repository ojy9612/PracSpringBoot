package com.homework.hanghae99homework02.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Setter
@Getter
public class RegisterDto {


    @NotBlank(message = "이메일을 적어주세요.")
    @Email(message = "email 형식으로 적어주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 적어주세요.")
    @Pattern(regexp = "^[a-zA-Z\\d`~!@#$%^&*()-_=+?.,;:'\"|<>\\]\\[}{]{4,}$",
            message = "최소 4자 이상이며, 닉네임과 같은 값이 포함되지 않아야 합니다.")
    private String password;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z\\d]{3,}$",
            message = "최소 3자 이상, 알파벳 대소문자(a~z, A~Z), 숫자(0~9)로 구성되어야 합니다.")
    private String nickname;

    @AssertTrue(message = "이메일이나 닉네임이 비밀번호에 포함되면 안됩니다.")
    public boolean checkPassword() {
        return !this.password.contains(this.email.split("@")[0])
                && !this.password.contains(this.nickname.split("@")[0]);
    }
}