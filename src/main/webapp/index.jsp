<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    if (session.getAttribute("usuario") == null) {
        response.sendRedirect("login");
        return;
    }
    com.example.catedrapoo.model.Usuario usuario = (com.example.catedrapoo.model.Usuario) session.getAttribute("usuario");
%>
<!DOCTYPE html>
<html>
<head>
  <title>Sistema de Cotizaciones</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 2em; }
    h1 { color: #007bff; }
    ul { list-style: none; padding: 0; }
    li { margin: 1em 0; }
    a { text-decoration: none; color: #007bff; font-size: 1.2em; }
    a:hover { text-decoration: underline; }
    .desc { color: #555; font-size: 0.95em; }
    .userbar { float: right; color: #333; font-size: 1em; }
  </style>
</head>
<body>
  <div class="userbar">
    Bienvenido, <b><%= usuario.getNombre() %></b> | <a href="logout">Cerrar sesión</a>
  </div>
<h1>Sistema de Cotizaciones</h1>
<ul>
  <li>
    <a href="clientes">Módulo de Clientes</a><br>
    <span class="desc">Registrar y gestionar clientes (personas naturales o jurídicas).</span>
  </li>
  <li>
    <a href="empleados">Módulo de Empleados</a><br>
    <span class="desc">Administrar trabajadores y empresas subcontratadas.</span>
  </li>
  <li>
    <a href="cotizaciones">Módulo de Cotizaciones</a><br>
    <span class="desc">Crear cotizaciones, asignar actividades y gestionar subtareas.</span>
  </li>
</ul>
<hr>
<p style="color:#888;">Proyecto POO 404 &copy; 2025</p>
</body>
</html>