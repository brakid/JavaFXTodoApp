package com.brakid.logic;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brakid.types.Todo;
import com.google.common.collect.ImmutableList;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.datatype.guava.GuavaModule;

public class TodoManager {
    private static final String TODOS_JSON_FILE = "todos.json";
    private final static Logger LOGGER = LoggerFactory.getLogger(TodoManager.class);
    private final static JsonMapper MAPPER = JsonMapper.builder().addModule(new GuavaModule()).build();
    private final ArrayList<Todo> todos = new ArrayList<>();

    public TodoManager() {
        try {
            MAPPER.readValue(new File(TODOS_JSON_FILE), new TypeReference<List<Todo>>() {}).forEach(todo -> this.todos.add(todo));
            LOGGER.info("Read TODOs from file");
        } catch (Exception e) {
            LOGGER.info("Failed to read TODOs: {}", e);
        }
    }

    public Todo add(String todoText) {
        final Todo newTodo = new Todo(UUID.randomUUID().hashCode(), todoText, ZonedDateTime.now());
        LOGGER.info("Creating new TODO: {}", newTodo);
        this.todos.add(newTodo);
        return newTodo;
    }

    public ImmutableList<Todo> getAll() {
        return ImmutableList.copyOf(this.todos);
    }

    public boolean delete(int todoId) {
        LOGGER.info("Deleting TODO with ID: {}", todoId);
        return this.todos.removeIf(todo -> todo.id() == todoId);
    }

    public void storeTodos() {
        try {
            MAPPER.writeValue(new File(TODOS_JSON_FILE), this.todos);
            LOGGER.info("Wrote TODOs to file");
        } catch (Exception e) {
            LOGGER.info("Failed to write TODOs: {}", e);
        }
    }
}
