<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!doctype html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Historial de actividad - MUA</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/bootstrap.css">
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/bi_s/bootstrap-icons.css">
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/historial-actividad.css?v=1">
</head>

<body>

<main class="history-page">

  <section class="history-title">
    <h1>Historial de Actividad</h1>
    <p>Todas las ventas hechas por el usuario</p>
  </section>

  <section class="history-list">

    <div class="empty-history">
      <i class="bi bi-bag-check"></i>
      <h2>Aún no hay actividad</h2>
      <p>Cuando realices compras o ventas, aparecerán en esta sección.</p>
    </div>

  </section>

</main>

</body>
</html>