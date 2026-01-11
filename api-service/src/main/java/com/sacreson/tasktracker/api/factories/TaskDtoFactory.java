package com.sacreson.tasktracker.api.factories;

import com.sacreson.tasktracker.api.dto.TaskDto;
import com.sacreson.tasktracker.store.entities.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskDtoFactory {

    public TaskDto makeTaskDto(TaskEntity taskEntity) {
        return TaskDto.builder()
                .id(taskEntity.getId())
                .title(taskEntity.getTitle())
                .description(taskEntity.getDescription())
                .status(taskEntity.getStatus())
                .createdAt(taskEntity.getCreatedAt())
                .build();
    }
}
