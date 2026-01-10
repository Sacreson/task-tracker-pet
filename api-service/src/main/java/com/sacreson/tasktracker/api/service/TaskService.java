package com.sacreson.tasktracker.api.service;

import com.sacreson.tasktracker.api.dto.CreateTaskDto;
import com.sacreson.tasktracker.api.store.entities.ProjectEntity;
import com.sacreson.tasktracker.api.store.entities.TaskEntity;
import com.sacreson.tasktracker.api.store.enums.TaskStatus;
import com.sacreson.tasktracker.api.store.repositories.ProjectRepository;
import com.sacreson.tasktracker.api.store.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public List<TaskEntity> getList(String prefixName) {
        log.debug("Request to get list of tasks");
        // Если фильтр не передали (null) — возвращаем всё
        if (prefixName == null || prefixName.trim().isEmpty()) {
            return taskRepository.findAll();
        }
        // Если передали — ищем по частичному совпадению
        return taskRepository.findAllByTitleContainingIgnoreCase(prefixName);
    }

    @Transactional
    public TaskEntity createTask(String title, String description, Long projectId) {
        log.debug("Request to create task. Title: {}, Description: {}, ProjectId: {}", title, description, projectId);
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        // проверка на уникальность
        if (taskRepository.findByTitle(title).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task already exists");
        }

        TaskEntity task = TaskEntity.builder()
                .title(title)
                .project(project)
                .description(description)
                .build();

        TaskEntity saved = taskRepository.save(task);
        log.info("Task created successfully. ID: {}, Title: {}, ProjectId: {}", saved.getId(), saved.getTitle(), projectId);
        return saved;
    }

    @Transactional
    public List<TaskEntity> createBatchTasks(Long projectId, List<CreateTaskDto> requests) {
        log.debug("Request to create batch tasks. ProjectId: {}, Requests: {}", projectId, requests);
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        List<TaskEntity> tasks = requests.stream()
                .map(request -> TaskEntity.builder()
                        .title(request.getTitle())
                        .description(request.getDescription())
                        .project(project) // Всем ставим один и тот же проект
                        .status(TaskStatus.TODO)
                        .build())
                .toList();

        List<TaskEntity> saved = taskRepository.saveAll(tasks);
        log.info("Batch tasks created successfully. ProjectId: {}, Requests: {}", projectId, requests);
        return saved;
    }

    @Transactional
    public TaskEntity updateTask(Long taskId, String title, String description, TaskStatus status) {
        log.debug("Request to update task. TaskId: {}, Title: {}, Description: {}, Status: {}", taskId, title, description, status);
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        if (title != null) {
            // TODO Тут можно сделать потом проверку на уникальность
            task.setTitle(title);
        }
        if (description != null) {
            task.setDescription(description);
        }
        if (status != null) {
            task.setStatus(status);
        }
        // save вызывать не обязательно, если стоит @Transactional,
        TaskEntity saved = taskRepository.save(task);
        log.info("Task updated successfully. ID: {}, Title: {}, Description: {}, Status: {}", taskId, saved.getTitle(), saved.getDescription(), saved.getStatus());
        return saved;
    }

    @Transactional
    public Boolean deleteTask(Long taskId) {
        log.debug("Request to delete task. TaskId: {}", taskId);
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        taskRepository.delete(task);
        log.info("Task deleted successfully. ID: {}, Title: {}, ProjectId: {}", taskId, task.getTitle(), task.getProject().getId());
        return true;
    }

    public List<TaskEntity> getTasksByProjectId(Long projectId) {
        log.debug("Request to get tasks by project ID. ProjectId: {}", projectId);
        if (!projectRepository.existsById(projectId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }
        List<TaskEntity> allByProjectId = taskRepository.findAllByProjectId(projectId);
        log.info("Tasks fetched by projectId successfully. ProjectId: {}, Tasks: {}", projectId, allByProjectId);
        return allByProjectId;
    }
}
