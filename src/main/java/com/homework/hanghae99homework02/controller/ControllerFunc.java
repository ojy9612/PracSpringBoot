package com.homework.hanghae99homework02.controller;

import com.homework.hanghae99homework02.exception.eset.WhoAreYouException;
import com.homework.hanghae99homework02.security.UserDetailsImpl;

import static com.homework.hanghae99homework02.exception.ErrorCode.NO_TOKEN_BABE;

public class ControllerFunc {

    static void userchecker(UserDetailsImpl userDetails){
        if (userDetails == null){
            throw new WhoAreYouException(NO_TOKEN_BABE);
        }
    }


}
