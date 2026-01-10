//package com.sacreson.tasktracker.api.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sacreson.tasktracker.api.dto.CreateProjectDto;
//import com.sacreson.tasktracker.api.dto.ProjectDto;
//import com.sacreson.tasktracker.api.factories.ProjectDtoFactory;
//import com.sacreson.tasktracker.api.security.JwtUtil;
//import com.sacreson.tasktracker.api.service.CustomUserDetailsService;
//import com.sacreson.tasktracker.api.service.ProjectService;
//import com.sacreson.tasktracker.api.store.entities.ProjectEntity;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(ProjectController.class)
////отключение токенов безопасности
//@AutoConfigureMockMvc(addFilters = false)
//public class ProjectControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//    @MockitoBean
//    private ProjectService projectService;
//    @MockitoBean
//    private ProjectDtoFactory projectDtoFactory;
//    @MockitoBean // Фейковый JwtUtil, чтобы SecurityConfig не падал
//    private JwtUtil jwtUtil;
//
//    @MockitoBean // Фейковый UserDetailsService, так как JwtFilter зависит и от него
//    private CustomUserDetailsService customUserDetailsService;
//
//    @Test
//    @
//    void createProject_shouldReturn200_whenDataIsValid() throws Exception {
//        String projectName = "New Project";
//        ProjectEntity expectedEntity = ProjectEntity.builder().id(1L).name(projectName).build();
//        ProjectDto expectedDto = ProjectDto.builder().id(1L).name(projectName).build();
//
//        Mockito.when(projectService.createProject(projectName, ))
//                .thenReturn(expectedEntity);
//
//        Mockito.when(projectDtoFactory.makeProjectDto(expectedEntity))
//                .thenReturn(expectedDto);
//
//        CreateProjectDto dto = new CreateProjectDto(projectName);
//
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/projects")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(projectName));
//
//    }
//}
