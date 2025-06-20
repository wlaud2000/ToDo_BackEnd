package com.project.todo_backend.domain.member.controller;

import com.project.todo_backend.domain.member.dto.request.MemberReqDTO;
import com.project.todo_backend.domain.member.dto.response.MemberResDTO;
import com.project.todo_backend.domain.member.entity.Member;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;

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

    @GetMapping("/members-lazy")
    public void getMembersLazy() {
        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            System.out.println("회원: " + member.getUsername());
            System.out.println("할일 개수: " + member.getTodos().size()); // 여기서 N번의 추가 쿼리
        }
    }

    @GetMapping("/test-comprehensive")
    public void testComprehensive() {
        System.out.println("=== 1. DISTINCT 없음 (INNER JOIN) ===");
        List<Member> noDistinct = memberRepository.findAllWithTodosNoDistinct();
        analyzeResults("NoDistinct", noDistinct);

        System.out.println("\n=== 2. DISTINCT 있음 (INNER JOIN) ===");
        List<Member> withDistinct = memberRepository.findAllWithTodosWithDistinct();
        analyzeResults("WithDistinct", withDistinct);

        System.out.println("\n=== 3. LEFT JOIN (모든 Member 포함) ===");
        List<Member> leftJoin = memberRepository.findAllWithTodosLeftJoin();
        analyzeResults("LeftJoin", leftJoin);
    }

    @GetMapping("/test-left-join")
    public void testLeftJoin() {
        List<Member> members = memberRepository.findAllWithTodosIncludeEmpty();

        System.out.println("조회된 Member 수: " + members.size());

        for (Member member : members) {
            System.out.println("Member: " + member.getUsername() +
                    ", Todo 개수: " + member.getTodos().size());
        }
    }

    @GetMapping("/test-multiple-bag")
    public void testMultipleBag() {
        try {
            List<Member> members = memberRepository.findAllWithTodosAndComments();
        } catch (Exception e) {
            System.out.println("예외 발생: " + e.getMessage());
            // org.hibernate.loader.MultipleBagFetchException:
            // cannot simultaneously fetch multiple bags
        }
    }

    @GetMapping("/test--multiple-bag-sol1")
    public void testMultipleBag1() {
        try {
            List<Member> members = memberService.getMembersWithTodosAndComments();
        } catch (Exception e) {
            System.out.println("예외 발생: " + e.getMessage());
            // org.hibernate.loader.MultipleBagFetchException:
            // cannot simultaneously fetch multiple bags
        }
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

    private void analyzeResults(String testName, List<Member> members) {
        System.out.println(testName + " - List.size(): " + members.size());

        Map<Long, Integer> memberCounts = new HashMap<>();
        for (Member member : members) {
            memberCounts.merge(member.getId(), 1, Integer::sum);
            System.out.println("  Member ID: " + member.getId() +
                    ", Username: " + member.getUsername() +
                    ", Todo count: " + member.getTodos().size() +
                    ", 객체 해시: " + System.identityHashCode(member));
        }

        System.out.println("Member별 출현 횟수:");
        memberCounts.forEach((id, count) ->
                System.out.println("  Member " + id + ": " + count + "번"));
    }
}
