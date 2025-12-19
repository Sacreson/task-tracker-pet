package com.sacreson.tasktracker.api.service;

import com.sacreson.tasktracker.api.store.entities.ProjectEntity;
import com.sacreson.tasktracker.api.store.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Transactional
    public ProjectEntity createProject(String name) {
        if (projectRepository.findByName(name).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project already exists");
        }
        ProjectEntity project = ProjectEntity.builder()
                .name(name)
                .build();

        return projectRepository.save(project);
    }

    @Transactional
    public ProjectEntity updateProject(Long id, String name) {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project not found"));

        if (projectRepository.findByName(name).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project already exists");
        }
        project.setName(name);
        return projectRepository.save(project);
    }

    @Transactional
    public Boolean deleteProject(Long id) {
        projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        projectRepository.deleteById(id);
        return true;
    }
}
