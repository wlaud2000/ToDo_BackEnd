package com.project.todo_backend.domain.member.controller;

import com.project.todo_backend.domain.member.dto.request.MemberReqDTO;
import com.project.todo_backend.domain.member.dto.response.MemberResDTO;
import com.project.todo_backend.domain.member.repository.MemberRepository;
import com.project.todo_backend.domain.member.service.MemberService;
import com.project.todo_backend.domain.todo.entity.Todo;
import com.project.todo_backend.domain.todo.exception.TodoErrorCode;
import com.project.todo_backend.domain.todo.exception.TodoException;
import com.project.todo_backend.domain.todo.repository.TodoRepository;
import com.project.todo_backend.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final TodoRepository todoRepository;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<MemberResDTO.SignUpResponseDTO>> signUp(@RequestBody @Valid MemberReqDTO.SignUpRequestDTO reqDTO) {
        MemberResDTO.SignUpResponseDTO resDTO = memberService.createMember(reqDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.onSuccess(HttpStatus.CREATED, resDTO));
    }

    @GetMapping("/list")
    public ApiResponse<MemberResDTO.MemberWithTodoCountListDTO> getAllMembersWithTodoCount() {
        log.info("N+1 문제 발생 API - 연관된 Entity를 가져오면?");
        MemberResDTO.MemberWithTodoCountListDTO members = memberService.getAllMembersWithTodoCount();
        return ApiResponse.onSuccess(members);
    }

    @GetMapping("/list2")
    public ApiResponse<MemberResDTO.MemberWithTodoCountListDTO2> getAllMembersWithTodoCount2() {
        log.info("N+1 문제 발생 API - 연관된 Entity를 가져오지 않으면?");
        MemberResDTO.MemberWithTodoCountListDTO2 members = memberService.getAllMembersWithTodoCount2();
        return ApiResponse.onSuccess(members);
    }

    @GetMapping("/test1")
    public void test1() {
        Todo todo = todoRepository.findById(1L).orElseThrow(() -> new TodoException(TodoErrorCode.TODO_NOT_FOUND));
        String name = todo.getMember().getUsername();
    }


    //Swagger용 가짜 컨트롤러
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberReqDTO.LoginRequestDTO reqDTO) {
        return null;
    }

    //Swagger용 가짜 컨트롤러
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return null;
    }
}
