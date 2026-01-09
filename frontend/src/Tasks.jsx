import { useEffect, useState } from 'react';
import { Button, message, Card, List, Input, Checkbox, Typography } from 'antd';
import { ArrowLeftOutlined, PlusOutlined } from '@ant-design/icons';

const { Text } = Typography;

export default function Tasks({ token, projectId, onBack }) {
    const [tasks, setTasks] = useState([]);
    const [loading, setLoading] = useState(false);
    const [newTaskName, setNewTaskName] = useState('');

    const fetchTasks = async () => {
        setLoading(true);
        try {
            const response = await fetch(`/api/projects/${projectId}/tasks`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (response.ok) {
                const data = await response.json();
                setTasks(data);
            }
        } catch (error) {
            message.error('Ошибка загрузки задач');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchTasks();
    }, [projectId]);

    const handleCreateTask = async () => {
        if (!newTaskName.trim()) return;
        try {
            // ⚠️ Важно: отправляем поле title, как ждет бэкенд
            const response = await fetch(`/api/projects/${projectId}/tasks`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ title: newTaskName, description: "Создано с фронта" })
            });

            if (response.ok) {
                setNewTaskName('');
                fetchTasks();
                message.success('Задача добавлена');
            }
        } catch (e) {
            message.error('Ошибка создания');
        }
    };

    // --- 🔥 НОВАЯ ФУНКЦИЯ: ПЕРЕКЛЮЧЕНИЕ СТАТУСА ---
    const toggleTaskStatus = async (task) => {
        // Если сейчас TODO, то станет DONE. И наоборот.
        // (Зависит от того, какие статусы у тебя в Java Enum. Обычно TODO/DONE или IN_PROGRESS/DONE)
        const newStatus = task.status === 'DONE' ? 'TODO' : 'DONE';

        try {
            // Предполагаем, что у тебя есть PATCH метод для обновления задачи
            // Если его нет на бэке — получим 405 Method Not Allowed, тогда скажи мне.
            const response = await fetch(`/api/projects/${projectId}/tasks/${task.id}`, {
                method: 'PATCH',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ status: newStatus })
            });

            if (response.ok) {
                // Обновляем список локально, чтобы галочка переключилась мгновенно
                setTasks(currentTasks =>
                    currentTasks.map(t =>
                        t.id === task.id ? { ...t, status: newStatus } : t
                    )
                );
                message.success('Статус обновлен');
            } else {
                message.error('Не удалось обновить статус');
            }
        } catch (e) {
            console.error(e);
            message.error('Ошибка сети');
        }
    };

    return (
        <div style={{ padding: '50px', maxWidth: '600px', margin: '0 auto' }}>
            <Button icon={<ArrowLeftOutlined />} onClick={onBack} style={{ marginBottom: 16 }}>
                Назад к проектам
            </Button>

            <Card title={`Задачи проекта #${projectId}`}>
                <div style={{ display: 'flex', gap: '10px', marginBottom: '20px' }}>
                    <Input
                        placeholder="Что нужно сделать?"
                        value={newTaskName}
                        onChange={(e) => setNewTaskName(e.target.value)}
                        onPressEnter={handleCreateTask}
                    />
                    <Button type="primary" icon={<PlusOutlined />} onClick={handleCreateTask}>
                        Добавить
                    </Button>
                </div>

                <List
                    loading={loading}
                    dataSource={tasks}
                    renderItem={(task) => (
                        <List.Item>
                            {/* 🔥 ОБНОВЛЕННЫЙ ЧЕКБОКС */}
                            <Checkbox
                                checked={task.status === 'DONE'}
                                onChange={() => toggleTaskStatus(task)} // При клике вызываем функцию
                            >
                                {/* Если DONE, текст зачеркнут */}
                                <Text delete={task.status === 'DONE'}>{task.title}</Text>
                            </Checkbox>
                        </List.Item>
                    )}
                    locale={{ emptyText: 'Нет задач. Отдыхаем! 🌴' }}
                />
            </Card>
        </div>
    );
}