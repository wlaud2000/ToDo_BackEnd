package com.project.todo_backend.global;

import com.project.todo_backend.domain.member.entity.Member;
import com.project.todo_backend.domain.member.repository.MemberRepository;
import com.project.todo_backend.domain.todo.entity.Todo;
import com.project.todo_backend.domain.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final TodoRepository todoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        // 기존 데이터가 있는지 확인
        if (memberRepository.count() > 0) {
            return; // 이미 데이터가 있으면 초기화 건너뛰기
        }

        // 멤버 생성
        List<Member> members = createMembers();

        // Todo 생성 - 첫 번째 사용자만 10,000개
        createTodos(members);

        System.out.println("데이터 초기화 완료: 첫 번째 사용자에게 10,000개의 Todo 항목이 생성되었습니다.");
    }

    private List<Member> createMembers() {
        List<Member> members = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            Member member = Member.builder()
                    .email("user" + i + "@example.com")
                    .password(passwordEncoder.encode("Test!123"))
                    .username("User " + i)
                    .build();

            members.add(memberRepository.save(member));
        }

        return members;
    }

    private void createTodos(List<Member> members) {
        Random random = new Random();

        // 불균등한 Todo 분포 생성 - 첫 번째 사용자에게만 10,000개 할당
        int[] todoCounts = {
                10000,  // 첫 번째 사용자 - 대량 테스트용
                50, 50, 50, 50, 30, 30, 30, 30, 20,
                20, 20, 20, 10, 10, 10, 10, 5, 5, 5  // 나머지 19명의 사용자
        };

        // 배치 저장을 위한 리스트
        List<Todo> todos = new ArrayList<>();

        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            int todoCount = todoCounts[i];

            System.out.println("사용자 " + member.getUsername() + "에 대해 " + todoCount + "개의 Todo 항목 생성 중...");

            for (int j = 1; j <= todoCount; j++) {
                Todo todo = Todo.builder()
                        .member(member)
                        .content("Todo #" + j + " for " + member.getUsername())
                        .completed(random.nextBoolean())
                        .build();

                todos.add(todo);

                // 1,000개마다 일괄 저장하여 메모리 효율성 확보
                if (todos.size() >= 1000) {
                    todoRepository.saveAll(todos);
                    System.out.println("1,000개 Todo 항목 일괄 저장 완료");
                    todos.clear();
                }
            }
        }

        // 남은 항목들 저장
        if (!todos.isEmpty()) {
            todoRepository.saveAll(todos);
            System.out.println("남은 " + todos.size() + "개 Todo 항목 저장 완료");
        }
    }
}