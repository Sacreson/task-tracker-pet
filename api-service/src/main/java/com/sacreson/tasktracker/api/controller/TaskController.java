package com.sacreson.tasktracker.api.controller;

import com.sacreson.tasktracker.api.dto.AckDto;
import com.sacreson.tasktracker.api.dto.CreateTaskDto;
import com.sacreson.tasktracker.api.dto.TaskDto;
import com.sacreson.tasktracker.api.dto.UpdateTaskDto;
import com.sacreson.tasktracker.api.factories.TaskDtoFactory;
import com.sacreson.tasktracker.api.service.TaskService;
import com.sacreson.tasktracker.api.store.entities.TaskEntity;
import com.sacreson.tasktracker.api.store.repositories.TaskRepository;
import com.sacreson.tasktracker.api.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskDtoFactory taskDtoFactory;

    @GetMapping(value = Constants.TASKS)
    public List<TaskDto> getAllTasks(@RequestParam(required = false) String prefix_name) {
        return taskService.getList(prefix_name).stream()
                .map(taskDtoFactory::makeTaskDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = Constants.TASKS_BY_PROJECT)
    public List<TaskDto> getTasksByProjectId(@PathVariable("project_id") Long projectId) {
        List<TaskEntity> tasksByProjectId = taskService.getTasksByProjectId(projectId);

        return tasksByProjectId.stream()
                .map(taskDtoFactory::makeTaskDto)
                .collect(Collectors.toList());
    }

    @PostMapping(value = Constants.TASKS_BY_PROJECT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public TaskDto createTask(@PathVariable("project_id") Long projectId, @RequestBody CreateTaskDto createTaskDto) throws BadRequestException {
        TaskEntity task = taskService.createTask(createTaskDto.getTitle(), createTaskDto.getDescription(), projectId);

        return taskDtoFactory.makeTaskDto(task);
    }

    @PostMapping(Constants.PROJECTS + "/{project_id}/tasks/batch")
    public List<TaskDto> createBatchTasks(@PathVariable("project_id") Long projectId,
                                          @RequestBody List<CreateTaskDto> requests) {

        List<TaskEntity> tasks = taskService.createBatchTasks(projectId, requests);

        return tasks.stream()
                .map(taskDtoFactory::makeTaskDto)
                .collect(Collectors.toList());
    }

    @PatchMapping(value = Constants.TASKS + "/{task_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public TaskDto updateTask(@PathVariable("task_id") Long taskId, @RequestBody UpdateTaskDto updateTaskDto) {
        TaskEntity task = taskService.updateTask(
                taskId,
                updateTaskDto.getTitle(),
                updateTaskDto.getDescription(),
                updateTaskDto.getStatus()
        );

        return taskDtoFactory.makeTaskDto(task);
    }

    @DeleteMapping(value = Constants.TASKS + "/{task_id}")
    public AckDto deleteTask(@PathVariable("task_id") Long taskId) {
        return AckDto.builder()
                .answer(taskService.deleteTask(taskId))
                .build();
    }
}
