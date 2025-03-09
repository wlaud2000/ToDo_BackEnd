package com.project.todo_backend.domain.todo.repository;

import com.project.todo_backend.domain.member.entity.Member;
import com.project.todo_backend.domain.todo.entity.Todo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    // ID 기준 내림차순 정렬, 특정 회원의 Todo조회 (첫 페이지)
    Slice<Todo> findByMemberOrderByIdDesc(Member member, Pageable pageable);

    // ID 기준 커서 이후 페이지 조회 (ID가 커서보다 작은 항목들)
    Slice<Todo> findByMemberAndIdLessThanOrderByIdDesc(Member member, Long cursor, Pageable pageable);
}
