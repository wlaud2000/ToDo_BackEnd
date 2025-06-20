package com.project.todo_backend.domain.member.repository;

import com.project.todo_backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Boolean existsByEmail(String email);

    // DISTINCT 없음
    @Query("SELECT m FROM Member m JOIN FETCH m.todos")
    List<Member> findAllWithTodosNoDistinct();

    // DISTINCT 있음
    @Query("SELECT DISTINCT m FROM Member m JOIN FETCH m.todos")
    List<Member> findAllWithTodosWithDistinct();

    // LEFT JOIN으로 test3도 포함
    @Query("SELECT DISTINCT m FROM Member m LEFT JOIN FETCH m.todos")
    List<Member> findAllWithTodosLeftJoin();

    @Query("SELECT DISTINCT m FROM Member m LEFT JOIN FETCH m.todos")
    List<Member> findAllWithTodosIncludeEmpty();

    @Query("SELECT m FROM Member m " +
            "JOIN FETCH m.todos " +          // 첫 번째 컬렉션
            "JOIN FETCH m.comments")         // 두 번째 컬렉션 - 예외
    List<Member> findAllWithTodosAndComments(); // MultipleBagFetchException

    // ------------------------------------------------------------------------------------------
    // 1단계: Member + Todos
    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.todos")
    List<Member> findAllWithTodos();

    // 2단계: Member + Comments (특정 Member ID들만)
    @Query("SELECT m FROM Member m " +
            "LEFT JOIN FETCH m.comments " +
            "WHERE m.id IN :memberIds")
    List<Member> findMembersWithComments(@Param("memberIds") List<Long> memberIds);
}
