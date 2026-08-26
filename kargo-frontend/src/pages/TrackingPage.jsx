import { useState } from 'react'
import { trackShipment } from '../api/shipmentApi.js'

function TrackingPage() {
  const [trackingNumber, setTrackingNumber] = useState('')
  const [shipment, setShipment] = useState(null)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  async function handleSearch(event) {
    event.preventDefault()
    setError('')
    setShipment(null)
    setLoading(true)

    try {
      setShipment(await trackShipment(trackingNumber))
    } catch (error) {
      setError(error.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <form className="form-panel" onSubmit={handleSearch}>
      <h2>Kargo Sorgula</h2>

      <label>Takip numarası</label>
      <input
        type="text"
        placeholder="Takip numarası"
        value={trackingNumber}
        onChange={(event) => setTrackingNumber(event.target.value)}
        required
      />

      <button type="submit" disabled={loading}>
        {loading ? 'Aranıyor...' : 'Sorgula'}
      </button>

      {error && <p className="error">{error}</p>}

      {shipment && (
        <div className="card">
          <p><span className="meta">Durum:</span> {shipment.status}</p>
          <p><span className="meta">Açıklama:</span> {shipment.description || '-'}</p>
          <p>
            <span className="meta">Oluşturulma Tarihi:</span>{' '}
            {new Date(shipment.createdAt).toLocaleString()}
          </p>
        </div>
      )}
    </form>
  )
}

export default TrackingPage
