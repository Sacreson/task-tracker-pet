package com.sacreson.tasktracker.api.controller;

import com.sacreson.tasktracker.api.dto.AckDto;
import com.sacreson.tasktracker.api.dto.CreateProjectDto;
import com.sacreson.tasktracker.api.dto.ProjectDto;
import com.sacreson.tasktracker.api.dto.UpdateProjectDto;
import com.sacreson.tasktracker.api.factories.ProjectDtoFactory;
import com.sacreson.tasktracker.api.service.ProjectService;
import com.sacreson.tasktracker.api.store.entities.ProjectEntity;
import com.sacreson.tasktracker.api.utils.Constants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectDtoFactory projectDtoFactory;

    @PostMapping(value = Constants.PROJECTS, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ProjectDto createProject(@Valid @RequestBody CreateProjectDto createProjectDto) {
        ProjectEntity project = projectService.createProject(createProjectDto.getName());

        return  projectDtoFactory.makeProjectDto(project);
    }

    @GetMapping(value = Constants.PROJECTS, produces = MediaType.APPLICATION_JSON_VALUE) //
    public List<ProjectDto> getAllProjects() {
        return projectService.getAllProjects();
    }


    @PatchMapping(value = Constants.PROJECTS + "/{project_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ProjectDto updateProjectName(@PathVariable("project_id") Long projectId, @RequestBody UpdateProjectDto updateProjectDto) {
        ProjectEntity project = projectService.updateProject(projectId, updateProjectDto.getName());

        return projectDtoFactory.makeProjectDto(project);
    }

    @DeleteMapping(value = Constants.PROJECTS + "/{project_id}")
    public AckDto deleteProject(@PathVariable Long project_id) {
        return AckDto.builder()
                .answer(projectService.deleteProject(project_id))
                .build();
    }
}
