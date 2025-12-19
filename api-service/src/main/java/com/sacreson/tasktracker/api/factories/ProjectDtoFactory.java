package com.sacreson.tasktracker.api.factories;

import com.sacreson.tasktracker.api.dto.ProjectDto;
import com.sacreson.tasktracker.api.dto.TaskDto;
import com.sacreson.tasktracker.api.store.entities.ProjectEntity;
import com.sacreson.tasktracker.api.store.entities.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class ProjectDtoFactory {

        public ProjectDto makeProjectDto(ProjectEntity projectEntity) {
        return ProjectDto.builder()
                .id(projectEntity.getId())
                .name(projectEntity.getName())
                .createdAt(projectEntity.getCreatedAt())
                .updatedAt(projectEntity.getUpdatedAt())
                .build();
    }
}
