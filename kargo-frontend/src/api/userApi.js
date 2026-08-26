import { apiFetch } from './client.js'

export function getUsers() {
  return apiFetch('/users', {
    errorMessage: 'Kullanıcılar alınamadı.'
  })
}
