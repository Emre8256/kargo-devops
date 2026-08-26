import { apiFetch } from './client.js'

export function getAddresses() {
  return apiFetch('/addresses', {
    errorMessage: 'Adresler alınamadı.'
  })
}

export function getAddressesByUserId(userId) {
  return apiFetch(`/addresses/user/${userId}`, {
    errorMessage: 'Adresler alınamadı.'
  })
}

export function createAddress(address) {
  return apiFetch('/addresses', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(address),
    errorMessage: 'Adres oluşturulamadı.'
  })
}

export function updateAddress(id, address) {
  return apiFetch(`/addresses/${id}`, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(address),
    errorMessage: 'Adres güncellenemedi.'
  })
}

export function deleteAddress(id) {
  return apiFetch(`/addresses/${id}`, {
    method: 'DELETE',
    errorMessage: 'Adres silinemedi.'
  })
}
