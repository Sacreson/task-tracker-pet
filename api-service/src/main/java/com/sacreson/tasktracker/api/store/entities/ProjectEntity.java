package com.sacreson.tasktracker.api.store.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@ToString(exclude = "tasks")
@EqualsAndHashCode(exclude = "tasks")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "project")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<TaskEntity> tasks = new ArrayList<>();

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    public void addTask(TaskEntity task) {
        tasks.add(task);
        task.setProject(this);
    }

    public void removeTask(TaskEntity task) {
        tasks.remove(task);
        task.setProject(null);
    }
}
