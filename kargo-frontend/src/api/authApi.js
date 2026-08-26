import { apiFetch } from './client.js'

export function login(credentials) {
  return apiFetch('/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(credentials),
    errorMessage: 'E-posta veya şifre hatalı.'
  })
}

export function register(user) {
  return apiFetch('/auth/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(user),
    errorMessage: 'Kayıt oluşturulamadı.'
  })
}
