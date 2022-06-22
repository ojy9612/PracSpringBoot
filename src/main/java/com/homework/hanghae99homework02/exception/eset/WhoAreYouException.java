package com.homework.hanghae99homework02.exception.eset;

import com.homework.hanghae99homework02.exception.ErrorCode;
import lombok.Getter;

@Getter
public class WhoAreYouException extends RuntimeException {
        private final ErrorCode errorCode;

        public WhoAreYouException(ErrorCode errorCode) {
            this.errorCode = errorCode;
        }
    }