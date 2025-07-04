package com.project.todo_backend.domain.todo.service;

import com.project.todo_backend.domain.member.entity.Member;
import com.project.todo_backend.domain.member.exception.MemberErrorCode;
import com.project.todo_backend.domain.member.exception.MemberException;
import com.project.todo_backend.domain.member.repository.MemberRepository;
import com.project.todo_backend.domain.todo.converter.TodoConverter;
import com.project.todo_backend.domain.todo.dto.request.TodoReqDTO;
import com.project.todo_backend.domain.todo.dto.response.TodoResDTO;
import com.project.todo_backend.domain.todo.entity.Todo;
import com.project.todo_backend.domain.todo.exception.TodoErrorCode;
import com.project.todo_backend.domain.todo.exception.TodoException;
import com.project.todo_backend.domain.todo.repository.TodoRepository;
import com.project.todo_backend.global.security.userdetails.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    // 투두 전체 조회(ID 기반 커서 페이지네이션)
    @Transactional(readOnly = true)
    public TodoResDTO.TodoResponseListDTO getTodoList(AuthUser authUser, Long cursor, int offset) {
        // 1. 사용자 조회
        Member member = memberRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.USER_NOT_FOUND_404));

        // 2. 커서 기반 페이지네이션으로 Todo목록 조회
        Slice<Todo> todoSlice;
        if (cursor == null) {
            // 첫 페이지 요청 (최신순 = ID 내림차순)
            todoSlice = todoRepository.findByMemberOrderByIdDesc(member, PageRequest.of(0, offset));
        } else {
            // 이후 페이지 요청 (커서보다 작은 ID)
            todoSlice = todoRepository.findByMemberAndIdLessThanOrderByIdDesc(member, cursor, PageRequest.of(0, offset));
        }

        List<Todo> todoList = todoSlice.getContent();

        // 3. 다음 커서 값 계산 (마지막 항목의 ID)
        Long nextCursor = !todoList.isEmpty() ? todoList.get(todoList.size() - 1).getId() : null;

        // 4. 컨버터를 사용하여 응답 DTO 생성 및 반환
        return TodoConverter.toResponseListDTO(todoList, todoSlice.hasNext(), nextCursor);
    }

    // 투두 content 수정
    @Transactional
    public TodoResDTO.TodoResponseDTO modifyTodoContent(AuthUser authUser, Long todoId, TodoReqDTO.TodoModifyRequestDTO reqDTO) {
        Todo todo = validateAndGetTodo(authUser, todoId);
        todo.updateContent(reqDTO.content());
        log.info("todo 내용 수정 완료");

        return TodoConverter.toTodoResponseDTO(todo);
    }

    // 투두 상태 토글 (완료/미완료)
    @Transactional
    public TodoResDTO.TodoResponseDTO toggleTodoStatus(AuthUser authUser, Long todoId) {
        Todo todo = validateAndGetTodo(authUser, todoId);
        todo.toggleCompleted();
        log.info("todo 토글 수정 완료");

        return TodoConverter.toTodoResponseDTO(todo);
    }

    // 투두 삭제
    @Transactional
    public void deleteTodo(AuthUser authUser, Long todoId) {
        Todo todo = validateAndGetTodo(authUser, todoId);
        todoRepository.delete(todo);
    }

    // 공통 메서드
    private Todo validateAndGetTodo(AuthUser authUser, Long todoId) {
        // 1. 사용자 조회
        Member member = memberRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.USER_NOT_FOUND_404));
        // 2. Todo조회
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoException(TodoErrorCode.TODO_NOT_FOUND));
        // 3. 사용자 소유권 확인
        if (!todo.getMember().getId().equals(member.getId())) {
            throw new TodoException(TodoErrorCode.UNAUTHORIZED_TODO_ACCESS);
        }
        return todo;
    }
    
}
