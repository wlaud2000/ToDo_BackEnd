package com.project.todo_backend.domain.member.controller;

import com.project.todo_backend.domain.member.dto.request.MemberReqDTO;
import com.project.todo_backend.domain.member.dto.response.MemberResDTO;
import com.project.todo_backend.domain.member.service.MemberService;
import com.project.todo_backend.global.apiPayload.ApiResponse;
import com.project.todo_backend.global.security.dto.JwtDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<MemberResDTO.SignUpResponseDTO>> signUp(@RequestBody @Valid MemberReqDTO.SignUpRequestDTO reqDTO) {
        MemberResDTO.SignUpResponseDTO resDTO = memberService.createMember(reqDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.onSuccess(HttpStatus.CREATED, resDTO));
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
