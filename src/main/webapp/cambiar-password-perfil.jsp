<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!doctype html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Cambiar contraseña - MUA</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/cambiar-password-perfil.css?v=4">
</head>

<body>

<main class="password-page">

  <section class="password-card">

    <div class="password-form">

      <img class="mua-logo"
           src="<%= request.getContextPath() %>/static/img/logoMUA.png"
           alt="Logo MUA">

      <h1>Cambiar contraseña</h1>

      <form action="#" method="post">

        <div class="form-group">
          <label for="passwordActual">Antigua contraseña</label>
          <input id="passwordActual"
                 name="passwordActual"
                 type="password"
                 placeholder="1233">
        </div>

        <div class="form-group">
          <label for="passwordNueva">Nueva Contraseña</label>
          <input id="passwordNueva"
                 name="passwordNueva"
                 type="password"
                 placeholder="1233">
        </div>

        <div class="form-group">
          <label for="passwordConfirmar">Confirmar la nueva Contraseña</label>
          <input id="passwordConfirmar"
                 name="passwordConfirmar"
                 type="password"
                 placeholder="1233">
        </div>

        <div class="password-actions">
          <a href="perfil-usuario.jsp" class="cancel-password-button">
            Cancelar
          </a>

          <button type="submit" class="restore-password-button">
            Restablecer
          </button>
        </div>

      </form>

    </div>

    <div class="password-image">
      <img src="<%= request.getContextPath() %>/static/img/aguilaMUA.png"
           alt="Águila MUA">
    </div>

  </section>

  <p class="responsible-text">
    Al continuar, aceptas usar MUA como marketplace universitario responsable.
  </p>

</main>

</body>
</html>