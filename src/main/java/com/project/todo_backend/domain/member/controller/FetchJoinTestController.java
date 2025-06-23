package com.project.todo_backend.domain.member.controller;

import com.project.todo_backend.domain.member.entity.Member;
import com.project.todo_backend.domain.member.repository.MemberRepository;
import com.project.todo_backend.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fetchjoin")
@RequiredArgsConstructor
@Slf4j
public class FetchJoinTestController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @GetMapping("/members-lazy")
    public void getMembersLazy() {
        List<Member> members = memberRepository.findAll();
        analyzeResults("LAZY N+1", members);
    }

    @GetMapping("/test-comprehensive")
    public void testComprehensive() {
        log.info("=== 1. DISTINCT 없음 (INNER JOIN) ===");
        List<Member> noDistinct = memberRepository.findAllWithTodosNoDistinct();
        analyzeResults("NoDistinct", noDistinct);

        log.info("\n=== 2. DISTINCT 있음 (INNER JOIN) ===");
        List<Member> withDistinct = memberRepository.findAllWithTodosWithDistinct();
        analyzeResults("WithDistinct", withDistinct);

        log.info("\n=== 3. LEFT JOIN (모든 Member 포함) ===");
        List<Member> leftJoin = memberRepository.findAllWithTodosLeftJoin();
        analyzeResults("LeftJoin", leftJoin);
    }

    @GetMapping("/test-multiple-bag")
    public void testMultipleBag() {
        try {
            memberRepository.findAllWithTodosAndComments();
        } catch (Exception e) {
            System.out.println("예외 발생: " + e.getMessage());
            // org.hibernate.loader.MultipleBagFetchException:
            // cannot simultaneously fetch multiple bags
        }
    }

    @GetMapping("/test--multiple-bag-sol1")
    public void testMultipleBag1() {
        try {
            memberService.getMembersWithTodosAndComments();
        } catch (Exception e) {
            System.out.println("예외 발생: " + e.getMessage());
            // org.hibernate.loader.MultipleBagFetchException:
            // cannot simultaneously fetch multiple bags
        }
    }

    @GetMapping("/testOOM1")
    public void testOOM() {
        memberService.getMembersWithPaging();
    }

    @GetMapping("/testOOM2")
    public void testOOM2() {
        memberService.getMembersWithPagingOptimized();
    }

    private void analyzeResults(String testName, List<Member> members) {
        log.info("{} - List.size(): {}", testName, members.size());

        Map<Long, Integer> memberCounts = new HashMap<>();
        for (Member member : members) {
            memberCounts.merge(member.getId(), 1, Integer::sum);
            log.info("  Member ID: {}, Username: {}, Todo count: {}, 객체 해시: {}",
                    member.getId(),
                    member.getUsername(),
                    member.getTodos().size(),
                    System.identityHashCode(member));
        }

        log.info("Member별 출현 횟수:");
        memberCounts.forEach((id, count) ->
                log.info("  Member {}: {}번", id, count));
    }
}
