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
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%@ include file="components/navbar.jsp" %>
<div class="container">
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
</div>
</body>
</html>