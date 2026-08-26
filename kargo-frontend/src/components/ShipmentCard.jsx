import { Link } from 'react-router-dom'

function ShipmentCard({ shipment }) {
  return (
    <div className="card">
      <h3>{shipment.trackingNumber}</h3>
      <p><span className="meta">Durum:</span> {shipment.status}</p>
      <p><span className="meta">Gönderici:</span> {shipment.senderName || '-'}</p>
      <p><span className="meta">Alıcı:</span> {shipment.receiverName || '-'}</p>

      <div className="card-actions">
        <Link to={`/shipments/${shipment.id}`}>Detayı Gör</Link>
      </div>
    </div>
  )
}

export default ShipmentCard
