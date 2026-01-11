package com.sacreson.tasktracker.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.sacreson.tasktracker.store.repositories")
@EntityScan(basePackages = "com.sacreson.tasktracker.store.entities")
public class TaskTrackerApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskTrackerApiApplication.class, args);
    }
}