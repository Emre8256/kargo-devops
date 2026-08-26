import { Link, useNavigate } from 'react-router-dom'

function Header({ isLoggedIn, role, onLogout }) {
  const navigate = useNavigate()

  function handleLogout() {
    onLogout()
    navigate('/login')
  }

  return (
    <header>
      <h1>Kargo Takip Sistemi</h1>

      <nav>
        {!isLoggedIn && (
          <>
            <Link to="/">Kargo Takip</Link>
            <Link to="/login">Giriş</Link>
            <Link to="/register">Kayıt Ol</Link>
          </>
        )}

        {isLoggedIn && role === 'CUSTOMER' && (
          <>
            <Link to="/customer">Ana Sayfa</Link>
            <Link to="/shipments/my">Kargolarım</Link>
            <Link to="/">Kargo Takip</Link>
            <Link to="/addresses">Adreslerim</Link>
            <button onClick={handleLogout}>Çıkış Yap</button>
          </>
        )}

        {isLoggedIn && role === 'ADMIN' && (
          <>
            <Link to="/admin">Dashboard</Link>
            <Link to="/admin/shipments/create">Yeni Kargo</Link>
            <Link to="/admin/shipments">Kargo Yönetimi</Link>
            <Link to="/">Kargo Takip</Link>
            <button onClick={handleLogout}>Çıkış Yap</button>
          </>
        )}
      </nav>
    </header>
  )
}

export default Header
