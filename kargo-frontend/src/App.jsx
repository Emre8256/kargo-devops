import { useState } from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'

import Header from './components/Header.jsx'
import ProtectedRoute from './components/ProtectedRoute.jsx'
import AddressPage from './pages/AddressPage.jsx'
import CreateShipmentPage from './pages/CreateShipmentPage.jsx'
import LoginPage from './pages/LoginPage.jsx'
import RegisterPage from './pages/RegisterPage.jsx'
import ShipmentDetailPage from './pages/ShipmentDetailPage.jsx'
import ShipmentListPage from './pages/ShipmentListPage.jsx'
import TrackingPage from './pages/TrackingPage.jsx'
import { clearAuth, getRole, isLoggedIn as hasToken } from './utils/auth.js'

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(hasToken())
  const [role, setRole] = useState(getRole())

  function handleLogout() {
    clearAuth()
    setIsLoggedIn(false)
    setRole(null)
  }

  function homePath(userRole = role) {
    return userRole === 'ADMIN' ? '/admin' : '/customer'
  }

  return (
    <>
      <Header isLoggedIn={isLoggedIn} role={role} onLogout={handleLogout} />

      <Routes>
        <Route path="/" element={<TrackingPage />} />

        <Route
          path="/login"
          element={
            isLoggedIn ? (
              <Navigate to={homePath()} />
            ) : (
              <LoginPage
                onLoginSuccess={(userRole) => {
                  setIsLoggedIn(true)
                  setRole(userRole)
                }}
              />
            )
          }
        />

        <Route
          path="/register"
          element={isLoggedIn ? <Navigate to={homePath()} /> : <RegisterPage />}
        />

        <Route
          path="/customer"
          element={
            <ProtectedRoute isLoggedIn={isLoggedIn}>
              {role === 'CUSTOMER' ? (
                <div className="page">
                  <h2>Hoş geldiniz</h2>
                  <p>Adreslerinizi ve kargolarınızı buradan yönetebilirsiniz.</p>
                </div>
              ) : (
                <Navigate to="/admin" />
              )}
            </ProtectedRoute>
          }
        />

        <Route
          path="/shipments/my"
          element={
            <ProtectedRoute isLoggedIn={isLoggedIn}>
              {role === 'CUSTOMER' ? (
                <ShipmentListPage title="Kargolarım" emptyMessage="Henüz kargonuz yok." />
              ) : (
                <Navigate to="/admin" />
              )}
            </ProtectedRoute>
          }
        />

        <Route
          path="/addresses"
          element={
            <ProtectedRoute isLoggedIn={isLoggedIn}>
              {role === 'CUSTOMER' ? <AddressPage /> : <Navigate to="/admin" />}
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin"
          element={
            <ProtectedRoute isLoggedIn={isLoggedIn}>
              {role === 'ADMIN' ? (
                <div className="page">
                  <h2>Admin paneli</h2>
                  <p>Yeni kargo oluşturabilir ve kargoları yönetebilirsiniz.</p>
                </div>
              ) : (
                <Navigate to="/customer" />
              )}
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/shipments"
          element={
            <ProtectedRoute isLoggedIn={isLoggedIn}>
              {role === 'ADMIN' ? (
                <ShipmentListPage title="Kargo Yönetimi" emptyMessage="Henüz kargo yok." />
              ) : (
                <Navigate to="/shipments/my" />
              )}
            </ProtectedRoute>
          }
        />

        <Route path="/shipments" element={<Navigate to="/admin/shipments" />} />

        <Route
          path="/admin/shipments/create"
          element={
            <ProtectedRoute isLoggedIn={isLoggedIn}>
              {role === 'ADMIN' ? <CreateShipmentPage /> : <Navigate to="/customer" />}
            </ProtectedRoute>
          }
        />

        <Route
          path="/shipments/:id"
          element={
            <ProtectedRoute isLoggedIn={isLoggedIn}>
              <ShipmentDetailPage />
            </ProtectedRoute>
          }
        />
      </Routes>
    </>
  )
}

export default App
