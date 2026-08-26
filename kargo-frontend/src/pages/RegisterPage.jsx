import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { register } from '../api/authApi.js'

function RegisterPage() {
  const navigate = useNavigate()
  const [form, setForm] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    phone: ''
  })
  const [error, setError] = useState('')
  const [message, setMessage] = useState('')

  function updateForm(field, value) {
    setForm((currentForm) => ({ ...currentForm, [field]: value }))
  }

  async function handleRegister(event) {
    event.preventDefault()
    setError('')
    setMessage('')

    try {
      await register(form)
      setMessage('Kayıt başarılı. Giriş sayfasına yönlendiriliyorsunuz.')
      setTimeout(() => navigate('/login'), 1000)
    } catch (error) {
      setError(error.message)
    }
  }

  return (
    <form className="form-panel" onSubmit={handleRegister}>
      <h2>Kayıt Ol</h2>

      {error && <p className="error">{error}</p>}
      {message && <p className="notice">{message}</p>}

      <label>Ad</label>
      <input value={form.firstName} onChange={(event) => updateForm('firstName', event.target.value)} required />

      <label>Soyad</label>
      <input value={form.lastName} onChange={(event) => updateForm('lastName', event.target.value)} required />

      <label>E-posta</label>
      <input type="email" value={form.email} onChange={(event) => updateForm('email', event.target.value)} required />

      <label>Şifre</label>
      <input type="password" value={form.password} onChange={(event) => updateForm('password', event.target.value)} required />

      <label>Telefon</label>
      <input value={form.phone} onChange={(event) => updateForm('phone', event.target.value)} />

      <button type="submit">Kayıt Ol</button>
    </form>
  )
}

export default RegisterPage
