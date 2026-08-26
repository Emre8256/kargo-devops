import { useState } from 'react'
import { login } from '../api/authApi.js'
import { saveAuth } from '../utils/auth.js'

function LoginPage({ onLoginSuccess }) {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')

  async function handleLogin(event) {
    event.preventDefault()
    setError('')

    try {
      const data = await login({ email, password })
      saveAuth(data.token, data.role)
      onLoginSuccess(data.role)
    } catch (error) {
      setError(error.message)
    }
  }

  return (
    <form className="form-panel" onSubmit={handleLogin}>
      <h2>Giriş Yap</h2>

      {error && <p className="error">{error}</p>}

      <label>E-posta</label>
      <input
        type="email"
        placeholder="ornek@mail.com"
        value={email}
        onChange={(event) => setEmail(event.target.value)}
        required
      />

      <label>Şifre</label>
      <input
        type="password"
        placeholder="Şifreniz"
        value={password}
        onChange={(event) => setPassword(event.target.value)}
        required
      />

      <button type="submit">Giriş Yap</button>
    </form>
  )
}

export default LoginPage
