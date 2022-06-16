package com.homework.hanghae99homework02.exception.eset;


import com.homework.hanghae99homework02.exception.ErrorCode;
import lombok.Getter;

@Getter
public class WrongIdException extends RuntimeException {
    private final ErrorCode errorCode;

    public WrongIdException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
