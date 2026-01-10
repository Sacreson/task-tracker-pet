package com.sacreson.tasktracker.api.service;

import com.sacreson.tasktracker.api.dto.ProjectDto;
import com.sacreson.tasktracker.api.factories.ProjectDtoFactory;
import com.sacreson.tasktracker.api.store.entities.ProjectEntity;
import com.sacreson.tasktracker.api.store.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectDtoFactory projectDtoFactory;

    public List<ProjectEntity> getProjects(Long userId) {
        return projectRepository.findAllByOwnerId(userId);
    }

    @Transactional
    public ProjectEntity createProject(Long ownerId, String name) {
        log.debug("Request to create project. Name: {}, OwnerId: {}", name, ownerId);
        if (projectRepository.findByNameAndOwnerId(name, ownerId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project already exists");
        }
        ProjectEntity project = ProjectEntity.builder()
                .name(name)
                .ownerId(ownerId)
                .build();

        ProjectEntity savedProject = projectRepository.save(project);

        log.info("Project created successfully. ID: {}, ProjectName: {}", savedProject.getId(), savedProject.getName());
        return savedProject;
    }

    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(projectDtoFactory::makeProjectDto) // Используй свою фабрику
                .collect(Collectors.toList());
    }

    @Transactional
    public ProjectEntity updateProject(Long id, String name, Long ownerId) {
        log.debug("Request to update project. ProjectId: {}, ProjectName: {}, OwnerId: {}", id , name, ownerId);
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project not found"));

        if (projectRepository.findByNameAndOwnerId(name, ownerId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project already exists");
        }
        project.setName(name);

        log.info("Project updated successfully. ID: {}, ProjectNewName: {}, OwnerId: {}", id, project.getName(), ownerId);

        return projectRepository.save(project);
    }

    @Transactional
    public Boolean deleteProject(Long ownerId, Long projectId) {
        log.debug("Request to delete project. ProjectId: {}, OwnerId: {}", projectId, ownerId);
        ProjectEntity project = projectRepository.findById(projectId)
                .filter(p -> p.getOwnerId().equals(ownerId)) // Проверка владельца
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project nod found or not owned"));

        projectRepository.delete(project);
        log.info("Project deleted successfully. ID: {}, ProjectName: {}, OwnerId: {}", projectId, project.getName(), ownerId);
        return true;
    }
}
