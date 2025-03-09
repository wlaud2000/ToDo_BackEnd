package com.project.todo_backend.domain.todo.exception;

import com.project.todo_backend.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TodoErrorCode implements BaseErrorCode {

    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "TODO404_0","해당 할 일을 찾을 수 없습니다."),
    UNAUTHORIZED_TODO_ACCESS(HttpStatus.FORBIDDEN, "TODO403_0","해당 할 일에 접근 권한이 없습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
