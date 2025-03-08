package com.project.todo_backend.domain.member.converter;

import com.project.todo_backend.domain.member.dto.response.MemberResDTO;
import com.project.todo_backend.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberConverter {

    public static MemberResDTO.SignUpResponseDTO signUpResponseDTO(Member member) {

        return MemberResDTO.SignUpResponseDTO.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .username(member.getUsername())
                .build();
    }
}
