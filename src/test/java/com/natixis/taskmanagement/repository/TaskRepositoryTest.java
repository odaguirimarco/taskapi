package com.natixis.taskmanagement.repository;

import com.natixis.taskmanagement.entity.Task;
import com.natixis.taskmanagement.entity.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void save_shouldSaveTask() {
        Task task = new Task(null, "To Do Task", "This is a to do task", LocalDate.now(),
                TaskStatus.TO_DO);
        Task savedTask = taskRepository.save(task);

        assertNotNull(savedTask.getId());
        assertEquals("To Do Task", savedTask.getTitle());
    }

    @Test
    void findById_shouldReturnTaskWhenFound() {
        Task task = new Task(null, "In progress task", "This is a in progress task", LocalDate.now(),
                TaskStatus.IN_PROGRESS);
        Task persistedTask = entityManager.persistAndFlush(task);

        Optional<Task> foundTask = taskRepository.findById(persistedTask.getId());

        assertTrue(foundTask.isPresent());
        assertEquals(persistedTask.getId(), foundTask.get().getId());
    }

    @Test
    void findById_shouldReturnEmptyOptionalWhenNotFound() {
        Optional<Task> foundTask = taskRepository.findById(100L); // Assuming ID 100 doesn't exist

        assertFalse(foundTask.isPresent());
    }

    @Test
    void findAll_shouldReturnListOfTasks() {
        Task task1 = new Task(null, "First Task", "Task to do", LocalDate.now(), TaskStatus.TO_DO);
        Task task2 = new Task(null, "Second Task", "Task in progress", LocalDate.now().plusDays(1),
                TaskStatus.IN_PROGRESS);
        entityManager.persist(task1);
        entityManager.persist(task2);
        entityManager.flush();

        List<Task> tasks = taskRepository.findAll();

        assertEquals(2, tasks.size());
        assertEquals("First Task", tasks.get(0).getTitle());
        assertEquals("Second Task", tasks.get(1).getTitle());
    }

    @Test
    void deleteById_shouldDeleteTask() {
        Task task = new Task(null, "Task to be deleted", "Delete this test", LocalDate.now(),
                TaskStatus.DONE);
        Task persistedTask = entityManager.persistAndFlush(task);

        taskRepository.deleteById(persistedTask.getId());

        Optional<Task> deletedTask = taskRepository.findById(persistedTask.getId());
        assertFalse(deletedTask.isPresent());
    }

    @Test
    void existsById_shouldReturnTrueWhenExists() {
        Task task = new Task(null, "Third task", "This task will be find", LocalDate.now(),
                TaskStatus.DONE);
        Task persistedTask = entityManager.persistAndFlush(task);

        assertTrue(taskRepository.existsById(persistedTask.getId()));
    }

    @Test
    void existsById_shouldReturnFalseWhenNotExists() {
        assertFalse(taskRepository.existsById(100L));
    }
}
