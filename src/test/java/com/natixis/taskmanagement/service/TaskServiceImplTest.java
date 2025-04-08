package com.natixis.taskmanagement.service;

import com.natixis.taskmanagement.entity.Task;
import com.natixis.taskmanagement.entity.TaskStatus;
import com.natixis.taskmanagement.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task firstTask;
    private Task secondTask;

    @BeforeEach
    void setUp() {
        firstTask = new Task(1L, "First Task", "Task to do", LocalDate.now(), TaskStatus.TO_DO);
        secondTask = new Task(2L, "Second Task", "Task in progress", LocalDate.now().plusDays(1),
                TaskStatus.IN_PROGRESS);
    }

    @Test
    void createTask_shouldSaveTaskAndReturnCreatedTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(firstTask);

        Task createdTask = taskService.createTask(firstTask);

        assertEquals(firstTask, createdTask);
        verify(taskRepository, times(1)).save(firstTask);
    }

    @Test
    void getAllTasks_shouldReturnListOfTasks() {
        List<Task> tasks = Arrays.asList(firstTask, secondTask);
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> returnedTasks = taskService.getAllTasks();

        assertEquals(2, returnedTasks.size());
        assertEquals(tasks, returnedTasks);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getTaskById_shouldReturnTaskWhenFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(firstTask));

        Task foundTask = taskService.getTaskById(1L);

        assertEquals(firstTask, foundTask);
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskById_shouldThrowExceptionWhenNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> taskService.getTaskById(1L));
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void updateTask_shouldUpdateTaskWhenFound() {
        Task updatedTask = new Task(1L, "Updated Task", "Updated Description - DONE",
                LocalDate.now().plusDays(2), TaskStatus.DONE);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(firstTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Task result = taskService.updateTask(1L, updatedTask);

        assertEquals(updatedTask, result);
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(updatedTask);
    }

    @Test
    void updateTask_shouldThrowExceptionWhenNotFound() {
        Task updatedTask = new Task(1L, "Updated Task", "Updated Description - DONE",
                LocalDate.now().plusDays(2), TaskStatus.DONE);
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> taskService.updateTask(1L, updatedTask));
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void deleteTask_shouldDeleteTaskWhenFound() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        assertDoesNotThrow(() -> taskService.deleteTask(1L));
        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTask_shouldThrowExceptionWhenNotFound() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> taskService.deleteTask(1L));
        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, never()).deleteById(anyLong());
    }
}
