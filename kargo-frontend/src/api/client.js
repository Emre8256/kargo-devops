const API_URL = 'http://localhost:8080/api'

export async function apiFetch(path, options = {}) {
  const token = localStorage.getItem('token')
  const headers = {
    ...options.headers
  }

  if (token) {
    headers.Authorization = `Bearer ${token}`
  }

  const response = await fetch(`${API_URL}${path}`, {
    ...options,
    headers
  })

  if (!response.ok) {
    throw new Error(options.errorMessage || `İşlem başarısız. (${response.status})`)
  }

  if (response.status === 204) {
    return null
  }

  return response.json()
}
