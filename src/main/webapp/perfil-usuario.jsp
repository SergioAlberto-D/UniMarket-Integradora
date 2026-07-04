<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Perfil de usuario - MUA</title>

    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/perfil-usuario.css">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
</head>

<body>

<header class="mua-navbar">
    <div class="navbar-content">

        <a class="brand" href="#">
            <img src="<%= request.getContextPath() %>/static/img/logoMUA.png" alt="Logo MUA" class="brand-logo">
            <span>Mua</span>
        </a>

        <div class="search-box">
            <i class="bi bi-search"></i>
            <input type="text" placeholder="Buscar artículos...">
        </div>

        <nav class="nav-actions">
            <a href="#">
                <i class="bi bi-search"></i>
                Buscar
            </a>

            <a href="#" class="muted">
                <i class="bi bi-basket"></i>
                Mis artículos
            </a>

            <a href="#">
                <i class="bi bi-plus-lg"></i>
                Publicar
            </a>

            <div class="mini-user">JD</div>
        </nav>

    </div>
</header>

<main class="profile-page">

    <section class="page-title">
        <h1>Perfil de usuario</h1>
        <p>Consulta y administra tu información personal</p>
    </section>

    <section class="profile-card">

        <div class="profile-left">
            <div class="avatar">JD</div>

            <div class="profile-data">
                <h2>Juan Rodriguez Duran</h2>

                <p>
                    <i class="bi bi-mortarboard"></i>
                    TSU en Desarrollo de Software
                </p>

                <p>
                    <i class="bi bi-telephone"></i>
                    777 123 4567
                </p>

                <p>
                    <i class="bi bi-envelope"></i>
                    juan.rodriguez@utez.edu.mx
                </p>
            </div>
        </div>

        <div class="stats-boxes">

            <div class="stat-card">
                <i class="bi bi-arrow-left-right"></i>
                <strong>12</strong>
                <span>Compras<br>Completadas</span>
            </div>

            <div class="stat-card">
                <i class="bi bi-chat-left-text"></i>
                <strong>6</strong>
                <span>Comentarios<br>Realizados</span>
            </div>

        </div>

    </section>

    <section class="content-grid">

        <section class="offers-card">

            <div class="section-header">
                <div class="section-icon">
                    <i class="bi bi-bell"></i>
                </div>

                <div>
                    <h3>Ofertas Realizadas</h3>
                    <p>Revisa y responde las propuestas de compradores.</p>
                </div>
            </div>

            <article class="offer-item">
                <div class="product-img ball"></div>

                <div class="offer-info">
                    <h4>Balón de fútbol</h4>
                    <p>Oferta de Franco Escamilla</p>
                    <strong>$320.50</strong>
                </div>

                <div class="offer-buttons">
                    <button class="btn-edit">Editar</button>
                    <button class="btn-cancel">Cancelar</button>
                </div>
            </article>

            <article class="offer-item">
                <div class="product-img phone"></div>

                <div class="offer-info">
                    <h4>Iphone 17 pro</h4>
                    <p>Oferta de Carl Cortes</p>
                    <strong>$17,500.50</strong>
                </div>

                <div class="offer-buttons">
                    <button class="btn-edit">Editar</button>
                    <button class="btn-cancel">Cancelar</button>
                </div>
            </article>

            <article class="offer-item">
                <div class="product-img ball"></div>

                <div class="offer-info">
                    <h4>Balón de fútbol</h4>
                    <p>Oferta de Franco Escamilla</p>
                    <strong>$320.50</strong>
                </div>

                <div class="offer-buttons">
                    <button class="btn-edit">Editar</button>
                    <button class="btn-cancel">Cancelar</button>
                </div>
            </article>

            <article class="offer-item">
                <div class="product-img phone"></div>

                <div class="offer-info">
                    <h4>Iphone 17 pro</h4>
                    <p>Oferta de Carl Cortes</p>
                    <strong>$17,500.50</strong>
                </div>

                <div class="offer-buttons">
                    <button class="btn-edit">Editar</button>
                    <button class="btn-cancel">Cancelar</button>
                </div>
            </article>

        </section>

        <section class="settings-card">

            <div class="section-header">
                <div class="section-icon">
                    <i class="bi bi-gear"></i>
                </div>

                <div>
                    <h3>Configuración de cuenta</h3>
                    <p>Acciones rápidas para administrar tu cuenta.</p>
                </div>
            </div>

            <button class="setting-option">
                <div class="setting-left">
                    <i class="bi bi-clock-history"></i>
                    <div>
                        <span>Historial de Actividad</span>
                        <small>Revisa tus últimas compras</small>
                    </div>
                </div>
                <i class="bi bi-arrow-right"></i>
            </button>

            <button class="setting-option">
                <div class="setting-left">
                    <i class="bi bi-telephone"></i>
                    <div>
                        <span>Cambiar teléfono</span>
                        <small>Actualiza tu número de contacto</small>
                    </div>
                </div>
                <i class="bi bi-arrow-right"></i>
            </button>

            <button class="setting-option">
                <div class="setting-left">
                    <i class="bi bi-lock"></i>
                    <div>
                        <span>Cambiar contraseña</span>
                        <small>Mantén tu cuenta segura</small>
                    </div>
                </div>
                <i class="bi bi-arrow-right"></i>
            </button>

            <button class="setting-option">
                <div class="setting-left">
                    <i class="bi bi-box-arrow-right"></i>
                    <div>
                        <span>Cerrar sesión</span>
                        <small>Finaliza tu sesión actual</small>
                    </div>
                </div>
                <i class="bi bi-arrow-right"></i>
            </button>

            <button class="setting-option danger">
                <div class="setting-left">
                    <i class="bi bi-trash"></i>
                    <div>
                        <span>Eliminar cuenta</span>
                        <small>Esta acción no se puede deshacer</small>
                    </div>
                </div>
                <i class="bi bi-arrow-right"></i>
            </button>

        </section>

    </section>

</main>

</body>
</html>