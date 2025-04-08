package com.natixis.taskmanagement.service;

import com.natixis.taskmanagement.entity.Task;

import java.util.List;

public interface TaskService {
    Task createTask(Task task);
    List<Task> getAllTasks();
    Task getTaskById(Long id);
    Task updateTask(Long id, Task updatedTask);
    void deleteTask(Long id);
}
