<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    if (session.getAttribute("usuario") == null) {
        response.sendRedirect("login");
        return;
    }
    com.example.catedrapoo.model.Usuario usuario = (com.example.catedrapoo.model.Usuario) session.getAttribute("usuario");
    java.util.List<com.example.catedrapoo.model.Trabajador> empleados = (java.util.List<com.example.catedrapoo.model.Trabajador>) request.getAttribute("empleados");
%>
<html>
<head>
    <title>Empleados</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 2em; }
        table { border-collapse: collapse; width: 100%; margin-bottom: 2em; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background: #f0f0f0; }
        form { margin-bottom: 2em; }
        input, select { padding: 5px; margin: 2px; }
        .btn { background: #007bff; color: #fff; border: none; padding: 6px 12px; cursor: pointer; }
        .btn:hover { background: #0056b3; }
    </style>
</head>
<body>
<div class="userbar" style="float:right; color:#333; font-size:1em;">
    Bienvenido, <b><%= usuario.getNombre() %></b> | <a href="logout">Cerrar sesión</a>
</div>
<h2>Lista de Empleados</h2>
<table>
    <tr>
        <th>ID</th>
        <th>Nombre</th>
        <th>Documento</th>
        <th>Tipo Persona</th>
        <th>Tipo Contratación</th>
        <th>Teléfono</th>
        <th>Correo</th>
        <th>Dirección</th>
        <th>Estado</th>
        <th>Acciones</th>
    </tr>
    <% if (empleados != null) for (com.example.catedrapoo.model.Trabajador empleado : empleados) { %>
        <tr>
            <td><%= empleado.getId() %></td>
            <td><%= empleado.getNombre() %></td>
            <td><%= empleado.getDocumentoIdentificacion() %></td>
            <td><%= empleado.getTipoPersona() %></td>
            <td><%= empleado.getTipoContratacion() %></td>
            <td><%= empleado.getTelefono() %></td>
            <td><%= empleado.getCorreo() %></td>
            <td><%= empleado.getDireccion() %></td>
            <td><%= empleado.getEstado() %></td>
            <td>
                <form method="post" action="empleados" style="display:inline;">
                    <input type="hidden" name="action" value="updateEmpleado" />
                    <input type="hidden" name="id" value="<%= empleado.getId() %>" />
                    <select name="estado">
                        <option value="Activo" <%= "Activo".equals(empleado.getEstado()) ? "selected" : "" %>>Activo</option>
                        <option value="Inactivo" <%= "Inactivo".equals(empleado.getEstado()) ? "selected" : "" %>>Inactivo</option>
                    </select>
                    <button class="btn" type="submit">Actualizar</button>
                </form>
            </td>
        </tr>
    <% } %>
</table>

<h3>Agregar Empleado</h3>
<form method="post" action="empleados">
    <input type="text" name="nombre" placeholder="Nombre" required />
    <input type="text" name="documento" placeholder="Documento" required />
    <select name="tipoPersona" required>
        <option value="">Tipo Persona</option>
        <option value="Natural">Natural</option>
        <option value="Juridica">Jurídica</option>
    </select>
    <select name="tipoContratacion" required>
        <option value="">Tipo Contratación</option>
        <option value="Permanente">Permanente</option>
        <option value="Por Horas">Por Horas</option>
    </select>
    <input type="text" name="telefono" placeholder="Teléfono" required />
    <input type="email" name="correo" placeholder="Correo" required />
    <input type="text" name="direccion" placeholder="Dirección" required />
    <select name="estado" required>
        <option value="">Estado</option>
        <option value="Activo">Activo</option>
        <option value="Inactivo">Inactivo</option>
    </select>
    <button class="btn" type="submit">Agregar</button>
</form>

</body>
</html>
