package com.project.todo_backend.domain.member.service;

import com.project.todo_backend.domain.member.converter.MemberConverter;
import com.project.todo_backend.domain.member.dto.request.MemberReqDTO;
import com.project.todo_backend.domain.member.dto.response.MemberResDTO;
import com.project.todo_backend.domain.member.entity.Member;
import com.project.todo_backend.domain.member.exception.MemberErrorCode;
import com.project.todo_backend.domain.member.exception.MemberException;
import com.project.todo_backend.domain.member.repository.MemberRepository;
import com.project.todo_backend.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder; //비밀번호 암호화

    //회원가입
    @Transactional
    public MemberResDTO.SignUpResponseDTO createMember(MemberReqDTO.SignUpRequestDTO reqDTO) {

        // 이메일 중복 확인
        if(memberRepository.existsByEmail(reqDTO.email())) {
            throw new MemberException(MemberErrorCode.EMAIL_ALREADY_EXISTS);
        }

        //DTO 정보 DB 저장
        Member member = Member.builder()
                .email(reqDTO.email())
                .username(reqDTO.username())
                .password(passwordEncoder.encode(reqDTO.password()))
                .build();

        Member savedMember = memberRepository.save(member);

        return MemberConverter.signUpResponseDTO(savedMember);
    }
}
