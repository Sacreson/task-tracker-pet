package com.sacreson.tasktracker.api.utils;

import lombok.experimental.UtilityClass;

@UtilityClass // Аннотация Lombok: делает класс final и кидает ошибку, если попробовать создать его экземпляр
public class Constants {

    public static final String API = "/api";
    public static final String TASKS = API + "/tasks";
    public static  final String PROJECTS = API + "/projects";
    public static final String TASKS_BY_PROJECT = PROJECTS + "/{project_id}/tasks";
    public static final String REGISTER = API + "/auth/register";
    public static final String LOGIN = API + "/auth/login";
}
