package com.natixis.taskmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.natixis.taskmanagement.entity.Task;
import com.natixis.taskmanagement.entity.TaskStatus;
import com.natixis.taskmanagement.service.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskServiceImpl taskService;

    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        task1 = new Task(1L, "First Task", "Task to do", LocalDate.now(), TaskStatus.TO_DO);
        task2 = new Task(2L, "Second Task", "Task in progress", LocalDate.now().plusDays(1),
                TaskStatus.IN_PROGRESS);
    }

    @Test
    @WithMockUser
    void createTask_shouldReturnCreatedTaskAndStatusCreated() throws Exception {
        when(taskService.createTask(any(Task.class))).thenReturn(task1);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task1))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Add CSRF token
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("First Task"));

        verify(taskService, times(1)).createTask(any(Task.class));
    }

    @Test
    @WithMockUser
    void getAllTasks_shouldReturnListOfTasksAndStatusOk() throws Exception {
        List<Task> tasks = Arrays.asList(task1, task2);
        when(taskService.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    @WithMockUser
    void getTaskById_shouldReturnTaskAndStatusOkWhenFound() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(task1);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L));

        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    @WithMockUser
    void getTaskById_shouldReturnStatusNotFoundWhenNotFound() throws Exception {
        when(taskService.getTaskById(1L)).thenThrow(new NoSuchElementException("Task not found"));

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    @WithMockUser
    void updateTask_shouldReturnUpdatedTaskAndStatusOk() throws Exception {
        Task updatedTask = new Task(1L, "Updated Task", "Updated Description - DONE",
                LocalDate.now().plusDays(2), TaskStatus.DONE);
        when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(updatedTask);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Add CSRF token
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Updated Task"));

        verify(taskService, times(1)).updateTask(eq(1L), any(Task.class));
    }

    @Test
    @WithMockUser
    void updateTask_shouldReturnStatusNotFoundWhenNotFound() throws Exception {
        Task updatedTask = new Task(1L, "Updated Task", "Updated Description",
                LocalDate.now().plusDays(2), TaskStatus.DONE);
        when(taskService.updateTask(eq(1L), any(Task.class))).thenThrow(new NoSuchElementException("Task not found"));

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Add CSRF token
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).updateTask(eq(1L), any(Task.class));
    }

    @Test
    @WithMockUser
    void deleteTask_shouldReturnStatusNoContent() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Add CSRF token)
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    @WithMockUser
    void deleteTask_shouldReturnStatusNotFoundWhenNotFound() throws Exception {
        doThrow(new NoSuchElementException("Task not found")).when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Add CSRF token)
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).deleteTask(1L);
    }
}
