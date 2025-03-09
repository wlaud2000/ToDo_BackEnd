package com.project.todo_backend.domain.todo.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

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
}
