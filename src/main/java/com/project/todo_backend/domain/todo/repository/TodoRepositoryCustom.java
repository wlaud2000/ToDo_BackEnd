package com.project.todo_backend.domain.todo.repository;

import com.project.todo_backend.domain.todo.entity.Todo;

import java.util.List;

public interface TodoRepositoryCustom {
    List<Todo> findTodosWithMember(Long memberId, Long cursor, int limit);
}
