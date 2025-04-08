package com.natixis.taskmanagement.service;

import com.natixis.taskmanagement.entity.Task;
import com.natixis.taskmanagement.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task with id " + id + " not found"));
    }

    @Override
    public Task updateTask(Long id, Task updatedTask) {
        return taskRepository.findById(id).map(task -> {
            updatedTask.setId(id);
            return taskRepository.save(updatedTask);
        }).orElseThrow(() -> new NoSuchElementException("Task with id " + id + " not found"));
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new NoSuchElementException("Task with id " + id + " not found");
        }
        taskRepository.deleteById(id);
    }
}
