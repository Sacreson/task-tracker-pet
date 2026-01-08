import Projects from './Projects';
import { useState } from 'react';
import { Button, Checkbox, Form, Input, Card, Typography, Alert, message } from 'antd';
import { LockOutlined, UserOutlined } from '@ant-design/icons';

const { Title } = Typography;

export default function App() {
    const [token, setToken] = useState(null); // Храним токен, если вошли

    // Эта функция сработает сама, когда форма валидна
    const onFinish = async (values) => {
        console.log('Отправляем:', values);

        try {
            const response = await fetch('/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(values), // AntD сам собрал username и password в объект values
            });

            if (response.ok) {
                const data = await response.json();
                setToken(data.token);
                message.success('Успешный вход!'); // Красивое всплывающее уведомление сверху
            } else {
                message.error('Ошибка входа: проверьте логин/пароль');
            }
        } catch (error) {
            message.error('Ошибка сети');
        }
    };

    // Если мы уже вошли, показываем приветствие
    if (token) {
        return <Projects token={token} />;
    }

    // Иначе показываем форму входа
    return (
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', background: '#f0f2f5' }}>
            <Card style={{ width: 400, boxShadow: "0 4px 8px rgba(0,0,0,0.1)" }}>
                <div style={{ textAlign: 'center', marginBottom: 20 }}>
                    <Title level={2}>Task Tracker</Title>
                    <p style={{ color: 'gray' }}>Пожалуйста, войдите в систему</p>
                </div>

                <Form
                    name="login_form"
                    initialValues={{ remember: true }}
                    onFinish={onFinish} // Сюда придут данные при сабмите
                    size="large"
                >
                    {/* Поле Логина */}
                    <Form.Item
                        name="username"
                        rules={[{ required: true, message: 'Введите логин!' }]}
                    >
                        <Input prefix={<UserOutlined />} placeholder="Username" />
                    </Form.Item>

                    {/* Поле Пароля */}
                    <Form.Item
                        name="password"
                        rules={[{ required: true, message: 'Введите пароль!' }]}
                    >
                        <Input.Password prefix={<LockOutlined />} placeholder="Password" />
                    </Form.Item>

                    <Form.Item>
                        <Button type="primary" htmlType="submit" block loading={false}>
                            Войти
                        </Button>
                    </Form.Item>
                </Form>
            </Card>
        </div>
    );
}