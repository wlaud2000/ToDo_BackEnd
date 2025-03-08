package com.project.todo_backend.domain.member.exception;

import com.project.todo_backend.global.apiPayload.exception.CustomException;
import lombok.Getter;

@Getter
public class MemberException extends CustomException {

    public MemberException(MemberErrorCode errorCode){
        super(errorCode);
    }
}
