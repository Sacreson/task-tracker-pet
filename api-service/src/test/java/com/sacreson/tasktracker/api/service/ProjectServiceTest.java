package com.sacreson.tasktracker.api.service;

import com.sacreson.tasktracker.api.dto.ProjectDto;
import com.sacreson.tasktracker.api.factories.ProjectDtoFactory;
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

    @Mock
    private ProjectDtoFactory projectDtoFactory;

    @InjectMocks
    private ProjectService projectService; // НАСТОЯЩИЙ сервис, в который Mockito сам вставит моки выше

    @Test
    void createProject_shouldReturnProjectDto_whenProjectNameIsUnique() {
        String projectName = "Test Project";

        ProjectEntity savedEntity = ProjectEntity.builder()
                .id(1L)
                .name(projectName)
                .createdAt(Instant.now())
                .build();

        //учим репозиторий что возвращать при сохр любового объекта
        Mockito.when(projectRepository.save(Mockito.any(ProjectEntity.class)))
                .thenReturn(savedEntity);

        //учим фабрику что возвращать
//        Mockito.when(projectDtoFactory.makeProjectDto(savedEntity))
//                .thenReturn(ProjectDto.builder().id(1L).name(projectName).build());


        ProjectEntity result = projectService.createProject(projectName);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(projectName, result.getName());

        //чекаем что сервис реально вызывал у репо 1 раз метод сейв
        Mockito.verify(projectRepository, Mockito.times(1)).save(Mockito.any(ProjectEntity.class));
    }

    @Test
    void createProject_shouldThrowException_whenProjectNameAlreadyExists() {
        String existingName = "Existing Project";

        Mockito.when(projectRepository.findByName(existingName))
                .thenReturn(Optional.of(ProjectEntity.builder().name(existingName).build()));

        Assertions.assertThrows(ResponseStatusException.class, () ->
                projectService.createProject(existingName));

        Mockito.verify(projectRepository, Mockito.never()).save(Mockito.any(ProjectEntity.class));
    }
}
