package com.project.todo_backend.domain.todo.repository;

import com.project.todo_backend.domain.member.entity.QMember;
import com.project.todo_backend.domain.todo.entity.QTodo;
import com.project.todo_backend.domain.todo.entity.Todo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Todo> findTodosWithMember(Long memberId, Long cursor, int limit) {
        QTodo todo = QTodo.todo;
        QMember member = QMember.member;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(todo.member.id.eq(memberId));

        if (cursor != null) {
            builder.and(todo.id.lt(cursor));
        }

        long startTime = System.currentTimeMillis();

        List<Todo> results = queryFactory
                .selectFrom(todo)
                .join(todo.member, member).fetchJoin() // Fetch Join 사용
                .where(builder)
                .orderBy(todo.id.desc())
                .limit(limit + 1) // 다음 페이지 존재 여부 확인용
                .fetch();

        long endTime = System.currentTimeMillis();
        log.debug("QueryDSL 쿼리 실행 시간: {}ms", (endTime - startTime));

        return results;
    }
}
