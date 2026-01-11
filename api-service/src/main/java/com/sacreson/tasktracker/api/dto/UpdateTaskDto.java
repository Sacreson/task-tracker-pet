package com.sacreson.tasktracker.api.dto;

import com.sacreson.tasktracker.store.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTaskDto {

    private String title;
    private String description;
    private TaskStatus status;

}
