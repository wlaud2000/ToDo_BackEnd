package com.project.todo_backend.domain.todo.repository;

import com.project.todo_backend.domain.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
