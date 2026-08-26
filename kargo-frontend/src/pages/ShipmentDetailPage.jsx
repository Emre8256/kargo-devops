import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { getShipment, updateShipmentStatus } from '../api/shipmentApi.js'
import { SHIPMENT_STATUSES } from '../constants/shipmentStatuses.js'
import { getRole } from '../utils/auth.js'

function ShipmentDetailPage() {
  const { id } = useParams()
  const isAdmin = getRole() === 'ADMIN'
  const [shipment, setShipment] = useState(null)
  const [status, setStatus] = useState('')
  const [error, setError] = useState('')
  const [message, setMessage] = useState('')

  async function loadShipment() {
    setError('')

    try {
      const data = await getShipment(id)
      setShipment(data)
      setStatus(data.status)
    } catch (error) {
      setError(error.message)
    }
  }

  async function handleStatusUpdate(event) {
    event.preventDefault()
    setError('')
    setMessage('')

    try {
      const data = await updateShipmentStatus(id, status)
      setShipment(data)
      setStatus(data.status)
      setMessage('Durum güncellendi.')
    } catch (error) {
      setError(error.message)
    }
  }

  useEffect(() => {
    loadShipment()
  }, [id])

  if (error && !shipment) {
    return <p className="error">{error}</p>
  }

  if (!shipment) {
    return <p className="page">Yükleniyor...</p>
  }

  return (
    <div className="page">
      <h2>Kargo Detayı</h2>

      {error && <p className="error">{error}</p>}
      {message && <p className="notice">{message}</p>}

      <div className="card">
        <h3>{shipment.trackingNumber}</h3>
        <p><span className="meta">Gönderici:</span> {shipment.senderName}</p>
        <p><span className="meta">Gönderici Adresi:</span> {shipment.senderAddress}</p>
        <p><span className="meta">Alıcı:</span> {shipment.receiverName}</p>
        <p><span className="meta">Alıcı Adresi:</span> {shipment.receiverAddress}</p>
        <p><span className="meta">Ağırlık:</span> {shipment.weight}</p>
        <p><span className="meta">Açıklama:</span> {shipment.description || '-'}</p>
        <p><span className="meta">Durum:</span> {shipment.status}</p>
        <p>
          <span className="meta">Oluşturulma Tarihi:</span>{' '}
          {new Date(shipment.createdAt).toLocaleString()}
        </p>
      </div>

      {isAdmin && (
        <form onSubmit={handleStatusUpdate}>
          <h2>Durum Güncelle</h2>
          <select value={status} onChange={(event) => setStatus(event.target.value)}>
            {SHIPMENT_STATUSES.map((shipmentStatus) => (
              <option key={shipmentStatus} value={shipmentStatus}>
                {shipmentStatus}
              </option>
            ))}
          </select>
          <button type="submit">Güncelle</button>
        </form>
      )}
    </div>
  )
}

export default ShipmentDetailPage
