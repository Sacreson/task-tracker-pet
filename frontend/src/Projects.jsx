import { useEffect, useState } from 'react';
import { Table, Button, message, Card, Modal, Input } from 'antd'; // Добавили Modal и Input
import { PlusOutlined, ReloadOutlined } from '@ant-design/icons';

export default function Projects({ token }) {
    const [projects, setProjects] = useState([]);
    const [loading, setLoading] = useState(false);

    // Новые состояния для модального окна
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [newProjectName, setNewProjectName] = useState('');

    // --- ЗАГРУЗКА ПРОЕКТОВ ---
    const fetchProjects = async () => {
        setLoading(true);
        try {
            const response = await fetch('/api/projects', {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (response.ok) {
                const data = await response.json();
                setProjects(data);
            }
        } catch (error) {
            message.error('Ошибка загрузки');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchProjects();
    }, []);

    // --- СОЗДАНИЕ ПРОЕКТА ---
    const handleCreateProject = async () => {
        if (!newProjectName.trim()) {
            message.warning('Введите название проекта');
            return;
        }

        try {
            const response = await fetch('/api/projects', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ name: newProjectName }) // Отправляем DTO
            });

            if (response.ok) {
                message.success('Проект создан!');
                setIsModalOpen(false); // Закрываем окно
                setNewProjectName(''); // Чистим поле
                fetchProjects(); // 🔄 Обновляем таблицу, чтобы увидеть новый проект
            } else {
                message.error('Не удалось создать проект');
            }
        } catch (error) {
            message.error('Ошибка сети');
        }
    };

    const columns = [
        { title: 'ID', dataIndex: 'id', key: 'id', width: 50 },
        { title: 'Название проекта', dataIndex: 'name', key: 'name', render: (text) => <b>{text}</b> },
    ];

    return (
        <div style={{ padding: '50px' }}>
            <Card
                title="Мои Проекты"
                extra={
                    <>
                        <Button icon={<ReloadOutlined />} onClick={fetchProjects} style={{ marginRight: 8 }} />
                        <Button type="primary" icon={<PlusOutlined />} onClick={() => setIsModalOpen(true)}>
                            Новый проект
                        </Button>
                    </>
                }
            >
                <Table
                    dataSource={projects}
                    columns={columns}
                    rowKey="id"
                    loading={loading}
                    locale={{ emptyText: 'Нет проектов' }}
                />
            </Card>

            {/* --- МОДАЛЬНОЕ ОКНО --- */}
            <Modal
                title="Создать новый проект"
                open={isModalOpen}
                onOk={handleCreateProject} // Нажатие на "OK" вызывает создание
                onCancel={() => setIsModalOpen(false)} // Нажатие на "Отмена" или крестик
                okText="Создать"
                cancelText="Отмена"
            >
                <Input
                    placeholder="Введите название (например, 'Учеба')"
                    value={newProjectName}
                    onChange={(e) => setNewProjectName(e.target.value)}
                    onPressEnter={handleCreateProject} // Чтобы работало по Enter
                />
            </Modal>

        </div>
    );
}