import { useEffect, useState } from 'react'
import { getShipments } from '../api/shipmentApi.js'
import ShipmentCard from '../components/ShipmentCard.jsx'

function ShipmentListPage({ title, emptyMessage }) {
  const [shipments, setShipments] = useState([])
  const [error, setError] = useState('')

  async function loadShipments() {
    setError('')

    try {
      setShipments(await getShipments())
    } catch (error) {
      setError(error.message)
    }
  }

  useEffect(() => {
    loadShipments()
  }, [])

  return (
    <div className="page page-wide">
      <h2>{title}</h2>

      {error && <p className="error">{error}</p>}
      {shipments.length === 0 && !error && <p>{emptyMessage}</p>}

      <div className="card-list">
        {shipments.map((shipment) => (
          <ShipmentCard key={shipment.id} shipment={shipment} />
        ))}
      </div>
    </div>
  )
}

export default ShipmentListPage
