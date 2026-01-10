import { useState } from 'react';
import { Button, Form, Input, Card, Typography, message } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';

const { Title } = Typography;

// onSuccess: вызываем, когда юзер создан (чтобы переключить на логин)
// onCancel: вызываем, если юзер передумал и нажал "Вернуться ко входу"
export default function Register({ onSuccess, onCancel }) {
    const [loading, setLoading] = useState(false);

    const onFinish = async (values) => {
        setLoading(true);
        try {
            // 1. Отправляем запрос на регистрацию
            const response = await fetch('/api/auth/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(values),
            });

            // 2. Обрабатываем ответ
            if (response.ok) {
                message.success('Регистрация успешна! Теперь войдите.');
                onSuccess(); // Переключаемся на форму входа
            } else {
                // Пытаемся прочитать текст ошибки от сервера (например, "User already exists")
                const errorData = await response.json();
                // Если сервер вернул message, показываем его, иначе общее сообщение
                const errorMsg = errorData.message || 'Ошибка регистрации';
                message.error(errorMsg);
            }
        } catch (error) {
            message.error('Ошибка сети. Проверьте сервер.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', background: '#f0f2f5' }}>
            <Card style={{ width: 400, boxShadow: "0 4px 8px rgba(0,0,0,0.1)" }}>
                <div style={{ textAlign: 'center', marginBottom: 20 }}>
                    <Title level={2}>Регистрация 📝</Title>
                    <p style={{ color: 'gray' }}>Придумайте логин и пароль</p>
                </div>

                <Form
                    name="register_form"
                    onFinish={onFinish}
                    size="large"
                >
                    <Form.Item
                        name="username"
                        rules={[
                            { required: true, message: 'Введите логин!' },
                            { min: 3, message: 'Логин должен быть длиннее 3 символов' } // Валидация на фронте
                        ]}
                    >
                        <Input prefix={<UserOutlined />} placeholder="Придумайте Login" />
                    </Form.Item>

                    <Form.Item
                        name="password"
                        rules={[
                            { required: true, message: 'Введите пароль!' },
                            { min: 4, message: 'Пароль слишком короткий' }
                        ]}
                    >
                        <Input.Password prefix={<LockOutlined />} placeholder="Придумайте пароль" />
                    </Form.Item>

                    {/* Кнопка Регистрации */}
                    <Form.Item>
                        <Button type="primary" htmlType="submit" block loading={loading}>
                            Зарегистрироваться
                        </Button>
                    </Form.Item>

                    {/* Кнопка "Назад ко входу" */}
                    <div style={{ textAlign: 'center' }}>
                        <Button type="link" onClick={onCancel}>
                            У меня уже есть аккаунт
                        </Button>
                    </div>
                </Form>
            </Card>
        </div>
    );
}