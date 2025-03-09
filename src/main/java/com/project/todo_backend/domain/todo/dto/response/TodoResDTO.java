package com.project.todo_backend.domain.todo.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class TodoResDTO {

    //투두 응답
    @Builder
    public record TodoResponseDTO(
            Long todoId,
            String content,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ){
    }

    //투두 전체조회(커서 페이지네이션)
    @Builder
    public record TodoResponseListDTO(
            List<TodoResponseDTO> todoResponseListDTO,
            boolean hasNext,
            Long cursor
    ){
    }
}
