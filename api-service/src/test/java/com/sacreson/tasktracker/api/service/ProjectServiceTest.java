package com.sacreson.tasktracker.api.service;

import com.sacreson.tasktracker.api.store.entities.ProjectEntity;
import com.sacreson.tasktracker.api.store.repositories.ProjectRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void createProject_shouldReturnProjectEntity_whenProjectNameIsUnique() {

        String projectName = "Test Project";
        Long ownerId = 1L;


        Mockito.when(projectRepository.findByNameAndOwnerId(projectName, ownerId))
                .thenReturn(Optional.empty());


        ProjectEntity savedEntity = ProjectEntity.builder()
                .id(10L)
                .name(projectName)
                .ownerId(ownerId)
                .createdAt(Instant.now())
                .build();

        Mockito.when(projectRepository.save(Mockito.any(ProjectEntity.class)))
                .thenReturn(savedEntity);


        ProjectEntity result = projectService.createProject(ownerId, projectName);


        Assertions.assertNotNull(result);
        Assertions.assertEquals(10L, result.getId());
        Assertions.assertEquals(projectName, result.getName());
        Assertions.assertEquals(ownerId, result.getOwnerId());


        Mockito.verify(projectRepository, Mockito.times(1))
                .findByNameAndOwnerId(projectName, ownerId);
        Mockito.verify(projectRepository, Mockito.times(1))
                .save(Mockito.any(ProjectEntity.class));
    }

    @Test
    void createProject_shouldThrowException_whenProjectNameAlreadyExists() {
        // ARRANGE
        String existingName = "Existing Project";
        Long ownerId = 1L;


        Mockito.when(projectRepository.findByNameAndOwnerId(existingName, ownerId))
                .thenReturn(Optional.of(ProjectEntity.builder().name(existingName).build()));


        Assertions.assertThrows(ResponseStatusException.class, () ->
                projectService.createProject(ownerId, existingName));


        Mockito.verify(projectRepository, Mockito.never()).save(Mockito.any());
    }
}