import { useState } from 'react';
import { Button, Form, Input, Card, Typography, message } from 'antd';
import { LockOutlined, UserOutlined } from '@ant-design/icons';
import Projects from './Projects';
import Tasks from './Tasks';
import Register from './Register'; // <--- Импортируем новый компонент

const { Title } = Typography;

export default function App() {
    const [token, setToken] = useState(null);
    const [selectedProjectId, setSelectedProjectId] = useState(null);

    // --- НОВОЕ СОСТОЯНИЕ ---
    const [isRegisterMode, setIsRegisterMode] = useState(false);

    // Логика входа (Login)
    const onLoginFinish = async (values) => {
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
            } else {
                message.error('Неверный логин или пароль');
            }
        } catch (error) {
            message.error('Ошибка сети');
        }
    };

    // --- ЛОГИКА ОТОБРАЖЕНИЯ ЭКРАНОВ ---

    // 1. Если мы авторизованы (есть токен)
    if (token) {
        if (selectedProjectId) {
            return (
                <Tasks
                    token={token}
                    projectId={selectedProjectId}
                    onBack={() => setSelectedProjectId(null)}
                />
            );
        }
        return (
            <Projects
                token={token}
                onProjectSelect={(id) => setSelectedProjectId(id)}
            />
        );
    }

    // 2. Если НЕ авторизованы, проверяем режим регистрации
    if (isRegisterMode) {
        return (
            <Register
                onSuccess={() => setIsRegisterMode(false)} // Если успешно -> идем на логин
                onCancel={() => setIsRegisterMode(false)}  // Если передумал -> идем на логин
            />
        );
    }

    // 3. Иначе показываем форму ВХОДА (Login)
    return (
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', background: '#f0f2f5' }}>
            <Card style={{ width: 400, boxShadow: "0 4px 8px rgba(0,0,0,0.1)" }}>
                <div style={{ textAlign: 'center', marginBottom: 20 }}>
                    <Title level={2}>Task Tracker</Title>
                    <p>Вход в систему</p>
                </div>

                <Form onFinish={onLoginFinish} size="large">
                    <Form.Item name="username" rules={[{ required: true, message: 'Введите логин' }]}>
                        <Input prefix={<UserOutlined />} placeholder="Username" />
                    </Form.Item>

                    <Form.Item name="password" rules={[{ required: true, message: 'Введите пароль' }]}>
                        <Input.Password prefix={<LockOutlined />} placeholder="Password" />
                    </Form.Item>

                    <Form.Item>
                        <Button type="primary" htmlType="submit" block>Войти</Button>
                    </Form.Item>

                    {/* Ссылка на регистрацию */}
                    <div style={{ textAlign: 'center' }}>
                        Нет аккаунта? <Button type="link" onClick={() => setIsRegisterMode(true)} style={{ padding: 0 }}>Зарегистрироваться</Button>
                    </div>
                </Form>
            </Card>
        </div>
    );
}