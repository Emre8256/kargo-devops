import { apiFetch } from './client.js'

export function getShipments() {
  return apiFetch('/shipments', {
    errorMessage: 'Kargolar alınamadı.'
  })
}

export function getShipment(id) {
  return apiFetch(`/shipments/${id}`, {
    errorMessage: 'Kargo alınamadı.'
  })
}

export function createShipment(shipment) {
  return apiFetch('/shipments', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(shipment),
    errorMessage: 'Kargo oluşturulamadı.'
  })
}

export function updateShipmentStatus(id, status) {
  return apiFetch(`/shipments/${id}/status`, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ status }),
    errorMessage: 'Durum güncellenemedi.'
  })
}

export function trackShipment(trackingNumber) {
  return apiFetch(`/shipments/tracking/${trackingNumber}`, {
    errorMessage: 'Kargo bulunamadı.'
  })
}
