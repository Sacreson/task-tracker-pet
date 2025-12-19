package com.sacreson.tasktracker.api.store.repositories;

import com.sacreson.tasktracker.api.store.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    Optional<ProjectEntity> findByName(String name);

    List<ProjectEntity> findAllByNameContainingIgnoreCase(String name);
}
