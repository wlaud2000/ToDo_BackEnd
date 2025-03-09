package com.project.todo_backend.domain.todo.service;

import com.project.todo_backend.domain.member.entity.Member;
import com.project.todo_backend.domain.member.exception.MemberErrorCode;
import com.project.todo_backend.domain.member.exception.MemberException;
import com.project.todo_backend.domain.member.repository.MemberRepository;
import com.project.todo_backend.domain.todo.converter.TodoConverter;
import com.project.todo_backend.domain.todo.dto.request.TodoReqDTO;
import com.project.todo_backend.domain.todo.dto.response.TodoResDTO;
import com.project.todo_backend.domain.todo.entity.Todo;
import com.project.todo_backend.domain.todo.repository.TodoRepository;
import com.project.todo_backend.global.security.userdetails.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodoService {

    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;

    // 투두 생성
    public TodoResDTO.TodoResponseDTO createTodo(AuthUser authUser, TodoReqDTO.TodoCreateRequestDTO reqDTO) {
        Member member = memberRepository.findByEmail(authUser.getEmail())
                .orElseThrow(() -> new MemberException(MemberErrorCode.USER_NOT_FOUND_404));

        Todo todo = TodoConverter.toTodoEntity(member, reqDTO);

        Todo savedTodo = todoRepository.save(todo);

        return TodoConverter.toTodoResponseDTO(savedTodo);
    }

    // 투두 전체 조회

    // 투두 content 수정

    // 투두 상태 수정

    // 투두 삭제
}
