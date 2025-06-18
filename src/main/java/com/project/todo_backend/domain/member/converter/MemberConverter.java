package com.project.todo_backend.domain.member.converter;

import com.project.todo_backend.domain.member.dto.request.MemberReqDTO;
import com.project.todo_backend.domain.member.dto.response.MemberResDTO;
import com.project.todo_backend.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberConverter {

    public static MemberResDTO.SignUpResponseDTO signUpResponseDTO(Member member) {

        return MemberResDTO.SignUpResponseDTO.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .username(member.getUsername())
                .build();
    }

    public static Member toMemberEntity(MemberReqDTO.SignUpRequestDTO reqDTO, PasswordEncoder passwordEncoder) {

        return Member.builder()
                .email(reqDTO.email())
                .username(reqDTO.username())
                .password(passwordEncoder.encode(reqDTO.password()))
                .build();
    }

    public static MemberResDTO.MemberWithTodoCountDTO toMemberWithTodoCountDTO(Member member) {
        return MemberResDTO.MemberWithTodoCountDTO.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .username(member.getUsername())
                .todoCount(member.getTodos().size())
                .build();
    }

    public static MemberResDTO.MemberWithTodoCountListDTO toMemberWithTodoCountListDTO(List<Member> members) {
        return MemberResDTO.MemberWithTodoCountListDTO.builder()
                .memberWithTodoCountListDTO(members.stream()
                        .map(MemberConverter::toMemberWithTodoCountDTO)
                        .toList())
                .build();
    }
}
