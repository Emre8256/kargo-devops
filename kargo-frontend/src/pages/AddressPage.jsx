import { useEffect, useState } from 'react'
import {
  createAddress,
  deleteAddress,
  getAddresses,
  updateAddress
} from '../api/addressApi.js'

const emptyAddress = { title: '', city: '', district: '', fullAddress: '' }

function AddressPage() {
  const [addresses, setAddresses] = useState([])
  const [addressForm, setAddressForm] = useState(emptyAddress)
  const [editingAddressId, setEditingAddressId] = useState(null)
  const [editForm, setEditForm] = useState(emptyAddress)
  const [error, setError] = useState('')
  const [message, setMessage] = useState('')

  function updateForm(setter, field, value) {
    setter((form) => ({ ...form, [field]: value }))
  }

  async function loadAddresses() {
    setError('')

    try {
      setAddresses(await getAddresses())
    } catch (error) {
      setError(error.message)
    }
  }

  async function handleCreateAddress(event) {
    event.preventDefault()
    setError('')
    setMessage('')

    try {
      await createAddress(addressForm)
      setAddressForm(emptyAddress)
      setMessage('Adres başarıyla oluşturuldu.')
      await loadAddresses()
    } catch (error) {
      setError(error.message)
    }
  }

  async function handleDeleteAddress(id) {
    setError('')
    setMessage('')

    try {
      await deleteAddress(id)
      setMessage('Adres başarıyla silindi.')
      await loadAddresses()
    } catch (error) {
      setError(error.message)
    }
  }

  function handleEditClick(address) {
    setEditingAddressId(address.id)
    setEditForm({
      title: address.title,
      city: address.city,
      district: address.district,
      fullAddress: address.fullAddress
    })
    setError('')
    setMessage('')
  }

  async function handleUpdateAddress(id) {
    setError('')
    setMessage('')

    try {
      await updateAddress(id, editForm)
      setEditingAddressId(null)
      setMessage('Adres başarıyla güncellendi.')
      await loadAddresses()
    } catch (error) {
      setError(error.message)
    }
  }

  useEffect(() => {
    loadAddresses()
  }, [])

  return (
    <div className="page page-wide">
      <h2>Adreslerim</h2>

      {error && <p className="error">{error}</p>}
      {message && <p className="notice">{message}</p>}
      {addresses.length === 0 && !error && <p>Henüz kayıtlı adresiniz yok.</p>}

      <div className="card-list">
        {addresses.map((address) => (
          <div className="card" key={address.id}>
            {editingAddressId === address.id ? (
              <>
                <label>Başlık</label>
                <input
                  value={editForm.title}
                  onChange={(event) => updateForm(setEditForm, 'title', event.target.value)}
                />

                <label>Şehir</label>
                <input
                  value={editForm.city}
                  onChange={(event) => updateForm(setEditForm, 'city', event.target.value)}
                />

                <label>İlçe</label>
                <input
                  value={editForm.district}
                  onChange={(event) => updateForm(setEditForm, 'district', event.target.value)}
                />

                <label>Açık adres</label>
                <input
                  value={editForm.fullAddress}
                  onChange={(event) => updateForm(setEditForm, 'fullAddress', event.target.value)}
                />

                <div className="actions">
                  <button onClick={() => handleUpdateAddress(address.id)}>Kaydet</button>
                  <button onClick={() => setEditingAddressId(null)}>İptal</button>
                </div>
              </>
            ) : (
              <>
                <h3>{address.title}</h3>
                <p><span className="meta">Şehir:</span> {address.city}</p>
                <p><span className="meta">İlçe:</span> {address.district}</p>
                <p><span className="meta">Adres:</span> {address.fullAddress}</p>

                <div className="actions">
                  <button onClick={() => handleEditClick(address)}>Düzenle</button>
                  <button onClick={() => handleDeleteAddress(address.id)}>Sil</button>
                </div>
              </>
            )}
          </div>
        ))}
      </div>

      <form onSubmit={handleCreateAddress}>
        <h2>Yeni Adres Ekle</h2>

        <label>Başlık</label>
        <input
          value={addressForm.title}
          onChange={(event) => updateForm(setAddressForm, 'title', event.target.value)}
          required
        />

        <label>Şehir</label>
        <input
          value={addressForm.city}
          onChange={(event) => updateForm(setAddressForm, 'city', event.target.value)}
          required
        />

        <label>İlçe</label>
        <input
          value={addressForm.district}
          onChange={(event) => updateForm(setAddressForm, 'district', event.target.value)}
          required
        />

        <label>Açık adres</label>
        <input
          value={addressForm.fullAddress}
          onChange={(event) => updateForm(setAddressForm, 'fullAddress', event.target.value)}
          required
        />

        <button type="submit">Adres Ekle</button>
      </form>
    </div>
  )
}

export default AddressPage
