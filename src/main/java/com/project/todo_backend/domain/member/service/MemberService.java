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

import java.util.List;

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
        Member member = MemberConverter.toMemberEntity(reqDTO, passwordEncoder);

        Member savedMember = memberRepository.save(member);

        return MemberConverter.signUpResponseDTO(savedMember);
    }

    @Transactional(readOnly = true)
    public MemberResDTO.MemberWithTodoCountListDTO getAllMembersWithTodoCount() {
        List<Member> members = memberRepository.findAll();
        return MemberConverter.toMemberWithTodoCountListDTO(members);
    }

    @Transactional(readOnly = true)
    public MemberResDTO.MemberWithTodoCountListDTO2 getAllMembersWithTodoCount2() {
        List<Member> members = memberRepository.findAll();
        return MemberConverter.toMemberWithTodoCountListDTO2(members);
    }

    @Transactional(readOnly = true)
    public List<Member> getMembersWithTodosAndComments() {

        // 1단계: Member와 Todos 조회
        List<Member> members = memberRepository.findAllWithTodos();

        // 2단계: 조회된 Member들의 ID로 Comments 조회
        List<Long> memberIds = members.stream()
                .map(Member::getId)
                .toList();

        List<Member> membersWithComments = memberRepository
                .findMembersWithComments(memberIds);

        // 3단계: 결과 병합 (JPA가 영속성 컨텍스트에서 자동으로 병합)
        return members;
    }
}
