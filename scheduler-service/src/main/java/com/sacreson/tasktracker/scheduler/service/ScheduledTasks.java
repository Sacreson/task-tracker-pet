package com.sacreson.tasktracker.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ScheduledTasks {


    @Scheduled(cron = "0 * * * * *")
    public void reportCurrentTime() {
        log.info("🕒 Scheduler is alive! Проверка задач...");

    }
}