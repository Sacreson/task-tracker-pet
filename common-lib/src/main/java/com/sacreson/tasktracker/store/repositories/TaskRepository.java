package com.sacreson.tasktracker.store.repositories;

import com.sacreson.tasktracker.store.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Optional<TaskEntity> findByTitle(String title);

    List<TaskEntity> findAllByOrderByCreatedAtDesc();

    List<TaskEntity> findAllByTitleContainingIgnoreCase(String title);

    Optional<TaskEntity> findByTitleAndProjectId(String title, Long projectId);

    List<TaskEntity> findAllByProjectId(Long projectId);
}
