package com.project.todo_backend.domain.member.dto.response;

import lombok.Builder;

import java.util.List;

public class MemberResDTO {

    @Builder
    public record SignUpResponseDTO(
            Long memberId,
            String email,
            String username
    ){
    }

    @Builder
    public record MemberWithTodoCountDTO(
            Long memberId,
            String email,
            String username,
            Integer todoCount
    ) {
    }

    @Builder
    public record MemberWithTodoCountListDTO(
            List<MemberWithTodoCountDTO> memberWithTodoCountListDTO
    ){
    }
}
