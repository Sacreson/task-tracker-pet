import { useState } from 'react';
import { Button, Checkbox, Form, Input, Card, Typography, Alert, message } from 'antd';
import { LockOutlined, UserOutlined } from '@ant-design/icons';
import Projects from './Projects';
import Tasks from './Tasks'; // <--- Импортируем новый компонент

const { Title } = Typography;

export default function App() {
    const [token, setToken] = useState(null);
    // Состояние: какой проект сейчас открыт? (null = никакой, смотрим список)
    const [selectedProjectId, setSelectedProjectId] = useState(null);

    const onFinish = async (values) => {
        // ... (тут код логина остается тем же, я сокращу для краткости) ...
        try {
            const response = await fetch('/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(values),
            });
            if (response.ok) {
                const data = await response.json();
                setToken(data.token);
                message.success('Успешный вход!');
            } else { message.error('Ошибка входа'); }
        } catch (error) { message.error('Ошибка сети'); }
    };

    // --- ЛОГИКА ПЕРЕКЛЮЧЕНИЯ ЭКРАНОВ ---

    // 1. Если нет токена -> Показываем ЛОГИН
    if (!token) {
        return (
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', background: '#f0f2f5' }}>
                <Card style={{ width: 400, boxShadow: "0 4px 8px rgba(0,0,0,0.1)" }}>
                    <div style={{ textAlign: 'center', marginBottom: 20 }}>
                        <Title level={2}>Task Tracker</Title>
                        <p>Вход в систему</p>
                    </div>
                    <Form onFinish={onFinish} size="large">
                        <Form.Item name="username" rules={[{ required: true }]}>
                            <Input prefix={<UserOutlined />} placeholder="Username" />
                        </Form.Item>
                        <Form.Item name="password" rules={[{ required: true }]}>
                            <Input.Password prefix={<LockOutlined />} placeholder="Password" />
                        </Form.Item>
                        <Form.Item>
                            <Button type="primary" htmlType="submit" block>Войти</Button>
                        </Form.Item>
                    </Form>
                </Card>
            </div>
        );
    }

    // 2. Если есть токен И выбран проект -> Показываем ЗАДАЧИ
    if (selectedProjectId) {
        return (
            <Tasks
                token={token}
                projectId={selectedProjectId}
                onBack={() => setSelectedProjectId(null)} // Кнопка "Назад" обнуляет выбор
            />
        );
    }

    // 3. Если есть токен, но проект не выбран -> Показываем СПИСОК ПРОЕКТОВ
    return (
        <Projects
            token={token}
            onProjectSelect={(id) => setSelectedProjectId(id)} // Прокидываем функцию выбора
        />
    );
}