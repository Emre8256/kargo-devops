import { useEffect, useState } from 'react'
import { getAddressesByUserId } from '../api/addressApi.js'
import { createShipment } from '../api/shipmentApi.js'
import { getUsers } from '../api/userApi.js'

function CreateShipmentPage() {
  const [users, setUsers] = useState([])
  const [senderUserId, setSenderUserId] = useState('')
  const [receiverUserId, setReceiverUserId] = useState('')
  const [senderAddresses, setSenderAddresses] = useState([])
  const [receiverAddresses, setReceiverAddresses] = useState([])
  const [senderAddressId, setSenderAddressId] = useState('')
  const [receiverAddressId, setReceiverAddressId] = useState('')
  const [weight, setWeight] = useState('')
  const [description, setDescription] = useState('')
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')

  async function loadUsers() {
    setError('')

    try {
      setUsers(await getUsers())
    } catch (error) {
      setError(error.message)
    }
  }

  async function loadAddresses(userId, setter) {
    if (!userId) {
      setter([])
      return
    }

    setError('')

    try {
      setter(await getAddressesByUserId(userId))
    } catch (error) {
      setError(error.message)
      setter([])
    }
  }

  function handleSenderChange(event) {
    const userId = event.target.value
    setSenderUserId(userId)
    setSenderAddressId('')
    loadAddresses(userId, setSenderAddresses)
  }

  function handleReceiverChange(event) {
    const userId = event.target.value
    setReceiverUserId(userId)
    setReceiverAddressId('')
    loadAddresses(userId, setReceiverAddresses)
  }

  async function handleCreateShipment(event) {
    event.preventDefault()
    setError('')
    setMessage('')

    try {
      const shipment = await createShipment({
        senderUserId: Number(senderUserId),
        receiverUserId: Number(receiverUserId),
        senderAddressId: Number(senderAddressId),
        receiverAddressId: Number(receiverAddressId),
        weight: Number(weight),
        description
      })

      setMessage(`Kargo oluşturuldu. Takip no: ${shipment.trackingNumber}`)
      setSenderUserId('')
      setReceiverUserId('')
      setSenderAddressId('')
      setReceiverAddressId('')
      setSenderAddresses([])
      setReceiverAddresses([])
      setWeight('')
      setDescription('')
    } catch (error) {
      setError(error.message)
    }
  }

  useEffect(() => {
    loadUsers()
  }, [])

  return (
    <form className="form-panel" onSubmit={handleCreateShipment}>
      <h2>Yeni Kargo</h2>

      {error && <p className="error">{error}</p>}
      {message && <p className="notice">{message}</p>}

      <label>Gönderici</label>
      <select value={senderUserId} onChange={handleSenderChange} required>
        <option value="">Gönderici seçin</option>
        {users.map((user) => (
          <option key={user.id} value={user.id}>
            {user.firstName} {user.lastName} - {user.email}
          </option>
        ))}
      </select>

      <label>Gönderici adresi</label>
      <select
        value={senderAddressId}
        onChange={(event) => setSenderAddressId(event.target.value)}
        disabled={!senderUserId}
        required
      >
        <option value="">Adres seçin</option>
        {senderAddresses.map((address) => (
          <option key={address.id} value={address.id}>
            {address.title} - {address.city} / {address.district}
          </option>
        ))}
      </select>

      <label>Alıcı</label>
      <select value={receiverUserId} onChange={handleReceiverChange} required>
        <option value="">Alıcı seçin</option>
        {users.map((user) => (
          <option key={user.id} value={user.id}>
            {user.firstName} {user.lastName} - {user.email}
          </option>
        ))}
      </select>

      <label>Alıcı adresi</label>
      <select
        value={receiverAddressId}
        onChange={(event) => setReceiverAddressId(event.target.value)}
        disabled={!receiverUserId}
        required
      >
        <option value="">Adres seçin</option>
        {receiverAddresses.map((address) => (
          <option key={address.id} value={address.id}>
            {address.title} - {address.city} / {address.district}
          </option>
        ))}
      </select>

      <label>Ağırlık</label>
      <input
        type="number"
        step="0.01"
        min="0.01"
        placeholder="Ağırlık"
        value={weight}
        onChange={(event) => setWeight(event.target.value)}
        required
      />

      <label>Açıklama</label>
      <textarea
        placeholder="Kargo açıklaması"
        maxLength={500}
        value={description}
        onChange={(event) => setDescription(event.target.value)}
      />

      <button type="submit">Kargo Oluştur</button>
    </form>
  )
}

export default CreateShipmentPage
