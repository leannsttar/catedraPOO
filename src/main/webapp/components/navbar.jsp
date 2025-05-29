<%@ page language="java" %>
<nav class="navbar">
    <div class="navbar-brand">
        <a href="${pageContext.request.contextPath}/index.jsp">Sistema de Cotizaciones</a>
    </div>
    <div class="navbar-menu">
        <a href="clientes" class="nav-item ${pageContext.request.servletPath.contains('clientes') ? 'active' : ''}">
            <i class="fas fa-users"></i> Clientes
        </a>
        <a href="empleados" class="nav-item ${pageContext.request.servletPath.contains('empleados') ? 'active' : ''}">
            <i class="fas fa-user-tie"></i> Empleados
        </a>
        <a href="cotizaciones" class="nav-item ${pageContext.request.servletPath.contains('cotizaciones') ? 'active' : ''}">
            <i class="fas fa-file-invoice-dollar"></i> Cotizaciones
        </a>
    </div>
    <div class="navbar-end">
        <span class="user-info">
            <i class="fas fa-user"></i> <%= ((com.example.catedrapoo.model.Usuario)session.getAttribute("usuario")).getNombre() %>
        </span>
        <a href="logout" class="nav-item logout">
            <i class="fas fa-sign-out-alt"></i> Cerrar sesion
        </a>
    </div>
</nav>