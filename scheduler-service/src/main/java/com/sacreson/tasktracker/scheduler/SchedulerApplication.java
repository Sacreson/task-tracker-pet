package com.sacreson.tasktracker.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

    @SpringBootApplication
    @EnableScheduling
    @EnableJpaRepositories(basePackages = "com.sacreson.tasktracker.store.repositories")
    @EntityScan(basePackages = "com.sacreson.tasktracker.store.entities")
    public class SchedulerApplication {
        public static void main(String[] args) {
            SpringApplication.run(SchedulerApplication.class, args);
        }
    }

