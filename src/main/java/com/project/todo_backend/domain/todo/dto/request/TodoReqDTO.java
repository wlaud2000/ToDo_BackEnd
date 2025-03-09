package com.project.todo_backend.domain.todo.dto.request;

public class TodoReqDTO {

    //투두 생성 요청
    public record TodoCreateRequestDTO(
            String content
    ){
    }

    //투두 수정 요청
    public record TodoModifyRequestDTO(
            String content
    ){
    }
}
