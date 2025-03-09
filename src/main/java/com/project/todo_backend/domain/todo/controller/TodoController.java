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
import org.springframework.web.bind.annotation.*;

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

    // 투두 전체 조회(커서 기반 페이지네이션)
    @GetMapping("")
    public ApiResponse<TodoResDTO.TodoResponseListDTO> getTodoList(@CurrentUser AuthUser authUser,
                                                                   @RequestParam(value = "cursor", required = false) Long cursor,
                                                                   @RequestParam(value = "offset", defaultValue = "10") int offset) {
        TodoResDTO.TodoResponseListDTO responseListDTO = todoService.getTodoList(authUser, cursor, offset);
        return ApiResponse.onSuccess(responseListDTO);
    }

    // 투두 content 수정
    @PatchMapping("/{todoId}")
    public ApiResponse<TodoResDTO.TodoResponseDTO> modifyTodoContent(@CurrentUser AuthUser authUser,
                                                              @PathVariable Long todoId,
                                                              @RequestBody TodoReqDTO.TodoModifyRequestDTO reqDTO) {
        TodoResDTO.TodoResponseDTO resDTO = todoService.modifyTodoContent(authUser, todoId, reqDTO);
        return ApiResponse.onSuccess(resDTO);
    }

    // 투두 상태 토글 (완료/미완료)
    @PatchMapping("/{todoId}/toggle")
    public ApiResponse<TodoResDTO.TodoResponseDTO> toggleTodoStatus(@CurrentUser AuthUser authUser,
                                                                    @PathVariable Long todoId) {
        TodoResDTO.TodoResponseDTO responseDTO = todoService.toggleTodoStatus(authUser, todoId);
        return ApiResponse.onSuccess(responseDTO);
    }

    // 투두 삭제
    @DeleteMapping("/{todoId}")
    public ApiResponse<String> deleteTodo(@CurrentUser AuthUser authUser,
                                          @PathVariable Long todoId) {
        todoService.deleteTodo(authUser, todoId);
        return ApiResponse.onSuccess("Todo 삭제 완료. todoId : " +todoId);
    }

}
