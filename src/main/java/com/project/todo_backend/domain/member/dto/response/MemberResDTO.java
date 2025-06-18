package com.project.todo_backend.domain.member.dto.response;

import lombok.Builder;

public class MemberResDTO {

    @Builder
    public record SignUpResponseDTO(
            Long memberId,
            String email,
            String username
    ){
    }

    @Builder
    public record MemberDetailResDTO(
            Long memberId,
            String email,
            String username,
            Integer todoNum
    ) {
    }
}
