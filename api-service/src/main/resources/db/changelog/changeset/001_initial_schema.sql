--liquibase formatted sql

--changeset sacreson:001_initial_schema

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL
);

CREATE TABLE project (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         owner_id BIGINT NOT NULL,
                         created_at TIMESTAMP(6) WITH TIME ZONE,
                         updated_at TIMESTAMP(6) WITH TIME ZONE,

    -- связь eсли удалить юзера, удалятся и его проекты, ну и сам даю имя форейн кию
                         CONSTRAINT fk_project_owner FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Индекс для ускорения поиска проектов конкретного юзера
CREATE INDEX idx_project_name_owner ON project(name, owner_id);

CREATE TABLE task (
                      id BIGSERIAL PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      description TEXT,
                      status VARCHAR(50) DEFAULT 'TODO',
                      created_at TIMESTAMP(6) WITH TIME ZONE,
                      updated_at TIMESTAMP(6) WITH TIME ZONE,
                      project_id BIGINT NOT NULL,

                      CONSTRAINT fk_task_project FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE
);

-- Индекс для внешнего ключа (хорошая практика для JOIN)
CREATE INDEX idx_task_project_id ON task(project_id);

--changeset sacreson:002_insert_demo_data
-- демо-юзер (Login: demo, Pass: 12345)
INSERT INTO users (username, password, role)
VALUES ('demo', '$2a$10$Xl0yHVzLIaJ.9izJ67IyYO1J.6A.1J.6A.1J.6A.1J.6A.1J.6A.', 'USER');