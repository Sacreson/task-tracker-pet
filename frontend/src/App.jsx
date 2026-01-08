import { useState } from 'react'

function App() {
    // 1. Состояние (State): Храним то, что вводит юзер
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [message, setMessage] = useState('Здесь будет ответ сервера...')

    // 2. Функция, которая срабатывает при нажатии кнопки
    async function handleLogin() {
        setMessage("Отправляю запрос...")

        try {
            // 3. FETCH: Делаем запрос.
            // Заметь: мы пишем '/api/...', а не 'http://localhost:8080/api...'
            // Vite сам перенаправит это на бэкенд благодаря настройке Proxy.
            const response = await fetch('/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username, password })
            })

            // 4. Обрабатываем ответ
            if (response.ok) {
                const data = await response.json()
                setMessage(`✅ УСПЕХ! Токен: ${data.token.slice(0, 15)}...`)
                console.log("Полный ответ:", data)
            } else {
                setMessage(`❌ ОШИБКА: Статус ${response.status}`)
            }

        } catch (error) {
            setMessage(`💀 ОШИБКА СЕТИ: ${error.message}`)
        }
    }

    // 5. Визуальная часть (HTML/JSX)
    return (
        <div style={{ padding: "50px", maxWidth: "400px", margin: "0 auto" }}>
            <h1>Вход в систему 🔐</h1>

            <div style={{ display: "flex", flexDirection: "column", gap: "10px" }}>
                <input
                    placeholder="Username"
                    value={username}
                    onChange={e => setUsername(e.target.value)}
                    style={{ padding: "10px", fontSize: "16px" }}
                />

                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                    style={{ padding: "10px", fontSize: "16px" }}
                />

                <button
                    onClick={handleLogin}
                    style={{ padding: "10px", background: "#007bff", color: "white", border: "none", cursor: "pointer" }}
                >
                    Войти
                </button>
            </div>

            <p style={{ marginTop: "20px", fontWeight: "bold" }}>{message}</p>
        </div>
    )
}

export default App