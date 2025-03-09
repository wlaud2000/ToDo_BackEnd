package com.project.todo_backend.domain.todo.controller;

import com.project.todo_backend.domain.todo.dto.request.TodoReqDTO;
import com.project.todo_backend.domain.todo.dto.response.TodoResDTO;
import com.project.todo_backend.domain.todo.service.TodoService;
import com.project.todo_backend.global.apiPayload.ApiResponse;
import com.project.todo_backend.global.security.annotation.CurrentUser;
import com.project.todo_backend.global.security.userdetails.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    // 투두 생성
    @PostMapping("")
    public ResponseEntity<ApiResponse<TodoResDTO.TodoResponseDTO>> createTodo(@CurrentUser AuthUser authUser,
                                                                              @RequestBody TodoReqDTO.TodoCreateRequestDTO reqDTO) {
        TodoResDTO.TodoResponseDTO resDTO = todoService.createTodo(authUser, reqDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.onSuccess(HttpStatus.CREATED, resDTO));
    }

    // 투두 전체 조회

    // 투두 content 수정

    // 투두 상태 수정

    // 투두 삭제


}
