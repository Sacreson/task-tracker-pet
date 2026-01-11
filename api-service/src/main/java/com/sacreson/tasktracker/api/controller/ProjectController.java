package com.sacreson.tasktracker.api.controller;

import com.sacreson.tasktracker.api.dto.AckDto;
import com.sacreson.tasktracker.api.dto.CreateProjectDto;
import com.sacreson.tasktracker.api.dto.ProjectDto;
import com.sacreson.tasktracker.api.dto.UpdateProjectDto;
import com.sacreson.tasktracker.api.factories.ProjectDtoFactory;
import com.sacreson.tasktracker.api.service.ProjectService;
import com.sacreson.tasktracker.store.entities.ProjectEntity;
import com.sacreson.tasktracker.store.entities.UserEntity;
import com.sacreson.tasktracker.store.repositories.UserRepository;
import com.sacreson.tasktracker.api.utils.Constants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final UserRepository userRepository;
    private final ProjectDtoFactory projectDtoFactory;

    @GetMapping(value = Constants.PROJECTS, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProjectDto> getProjects(Principal principal) {
        UserEntity user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        List<ProjectEntity> projects = projectService.getProjects(user.getId());

        return projects.stream()
                .map(projectDtoFactory::makeProjectDto)
                .collect(Collectors.toList());
    }

    @PostMapping(value = Constants.PROJECTS, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ProjectDto createProject(@Valid @RequestBody CreateProjectDto createProjectDto, Principal principal) {

        UserEntity user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        ProjectEntity project = projectService.createProject(user.getId(), createProjectDto.getName());

        return projectDtoFactory.makeProjectDto(project);
    }

//    @GetMapping(value = Constants.PROJECTS, produces = MediaType.APPLICATION_JSON_VALUE) //
//    public List<ProjectDto> getAllProjects() {
//        return projectService.getAllProjects();
//    }


    @PatchMapping(value = Constants.PROJECTS + "/{project_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ProjectDto updateProjectName(@PathVariable("project_id") Long projectId, @RequestBody UpdateProjectDto updateProjectDto, Principal principal) {
        UserEntity user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        ProjectEntity project = projectService.updateProject(projectId, updateProjectDto.getName(), user.getId());

        return projectDtoFactory.makeProjectDto(project);
    }

    @DeleteMapping(value = Constants.PROJECTS + "/{project_id}")
    public AckDto deleteProject(@PathVariable Long project_id, Principal principal) {
        UserEntity user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        return AckDto.builder()
                .answer(projectService.deleteProject(user.getId(), project_id))
                .build();
    }
}
