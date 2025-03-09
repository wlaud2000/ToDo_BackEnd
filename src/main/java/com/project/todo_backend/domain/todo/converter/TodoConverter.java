package com.project.todo_backend.domain.todo.converter;

import com.project.todo_backend.domain.member.entity.Member;
import com.project.todo_backend.domain.todo.dto.request.TodoReqDTO;
import com.project.todo_backend.domain.todo.dto.response.TodoResDTO;
import com.project.todo_backend.domain.todo.entity.Todo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodoConverter {

    // Todo엔티티 컨버터
    public static Todo toTodoEntity(Member member, TodoReqDTO.TodoCreateRequestDTO reqDTO) {

        return Todo.builder()
                .content(reqDTO.content())
                .completed(false)
                .member(member)
                .build();
    }

    // Todo엔티티 -> ResponseDTO
    public static TodoResDTO.TodoResponseDTO toTodoResponseDTO(Todo todo) {

        return TodoResDTO.TodoResponseDTO.builder()
                .todoId(todo.getId())
                .content(todo.getContent())
                .createdAt(todo.getCreatedAt())
                .updatedAt(todo.getUpdatedAt())
                .build();
    }
}
