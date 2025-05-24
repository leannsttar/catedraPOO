<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%
    if (session.getAttribute("usuario") == null) {
        response.sendRedirect("login");
        return;
    }
    com.example.catedrapoo.model.Usuario usuario = (com.example.catedrapoo.model.Usuario) session.getAttribute("usuario");
    Integer cotizacionId = null;
    if (request.getParameter("id") != null) {
        cotizacionId = Integer.parseInt(request.getParameter("id"));
    }
    java.util.List<com.example.catedrapoo.model.Asignacion> asignaciones = (java.util.List<com.example.catedrapoo.model.Asignacion>) request.getAttribute("asignaciones");
    Map<Integer, String> empleadoMap = (Map<Integer, String>) request.getAttribute("empleadoMap");
%>
<% boolean cotizacionBloqueada = false;
   com.example.catedrapoo.model.Cotizacion cotizacion = (com.example.catedrapoo.model.Cotizacion) request.getAttribute("cotizacion");
   if (cotizacion != null && ("Finalizada".equalsIgnoreCase(cotizacion.getEstado()) || "Inactiva".equalsIgnoreCase(cotizacion.getEstado()))) {
       cotizacionBloqueada = true;
   }
%>
<html>
<head>
    <title>Detalle de Cotización</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 2em; }
        table { border-collapse: collapse; width: 100%; margin-bottom: 2em; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background: #f0f0f0; }
        form { margin-bottom: 2em; }
        input, select { padding: 5px; margin: 2px; }
        .btn { background: #007bff; color: #fff; border: none; padding: 6px 12px; cursor: pointer; }
        .btn:hover { background: #0056b3; }
        .userbar { float: right; color: #333; font-size: 1em; }
    </style>
</head>
<body>
<div class="userbar">
    Bienvenido, <b><%= usuario.getNombre() %></b> | <a href="logout">Cerrar sesión</a>
</div>
<h2>Detalle de Cotización #<%= cotizacionId != null ? cotizacionId : "" %>
<% String clienteNombre = (String) request.getAttribute("clienteNombre"); %>
<% if (clienteNombre != null && !clienteNombre.isEmpty()) { %>
    del cliente '<%= clienteNombre %>'
<% } %>
</h2>

<h3>Agregar Actividad (Asignación)</h3>
<form method="post" action="detalleCotizacion">
    <input type="hidden" name="action" value="addAsignacion" />
    <input type="hidden" name="cotizacionId" value="<%= cotizacionId %>" />
    <input type="text" name="titulo" placeholder="Título" required <%= cotizacionBloqueada ? "disabled" : "" %> />
    <input type="text" name="area" placeholder="Área" required <%= cotizacionBloqueada ? "disabled" : "" %> />
    <select name="idEmpleado" required <%= cotizacionBloqueada ? "disabled" : "" %>>
        <option value="">Seleccione un empleado</option>
        <% 
            java.util.List<com.example.catedrapoo.model.Trabajador> empleados = (java.util.List<com.example.catedrapoo.model.Trabajador>) request.getAttribute("empleados");
            if (empleados != null) for (com.example.catedrapoo.model.Trabajador emp : empleados) { %>
            <option value="<%= emp.getId() %>"><%= emp.getNombre() %></option>
        <% } %>
    </select>
    <input type="number" step="0.01" name="costoPorHora" placeholder="Costo/Hora" required <%= cotizacionBloqueada ? "disabled" : "" %> />
    <input type="datetime-local" name="fechaInicio" placeholder="Fecha Inicio" required <%= cotizacionBloqueada ? "disabled" : "" %> />
    <input type="datetime-local" name="fechaFin" placeholder="Fecha Fin" required <%= cotizacionBloqueada ? "disabled" : "" %> />
    <input type="number" name="cantidadHoras" placeholder="Horas" required <%= cotizacionBloqueada ? "disabled" : "" %> />
    <input type="number" step="0.01" name="incrementoExtra" placeholder="Incremento Extra (%)" required <%= cotizacionBloqueada ? "disabled" : "" %> />
    <button class="btn" type="submit" <%= cotizacionBloqueada ? "disabled style='background:#888;cursor:not-allowed;'" : "" %>>Agregar Actividad</button>
</form>

<h3>Actividades (Asignaciones)</h3>
<table>
    <tr>
        <th>ID</th>
        <th>Título</th>
        <th>Área</th>
        <th>Empleado</th>
        <th>Costo/Hora</th>
        <th>Fecha Inicio</th>
        <th>Fecha Fin</th>
        <th>Horas</th>
        <th>Incremento Extra</th>
        <th>Costo Base</th>
        <th>Total</th>
        <th>Subtareas</th>
    </tr>
    <% if (asignaciones != null) for (com.example.catedrapoo.model.Asignacion asignacion : asignaciones) { %>
        <tr>
            <td><%= asignacion.getId() %></td>
            <td><%= asignacion.getTitulo() %></td>
            <td><%= asignacion.getArea() %></td>
            <td><%= (asignacion.getIdEmpleado() != null && empleadoMap != null && empleadoMap.get(asignacion.getIdEmpleado()) != null) ? empleadoMap.get(asignacion.getIdEmpleado()) : "" %></td>
            <td><%= asignacion.getCostoPorHora() %></td>
            <td><%= asignacion.getFechaInicio() %></td>
            <td><%= asignacion.getFechaFin() %></td>
            <td><%= asignacion.getCantidadHoras() %></td>
            <td><%= asignacion.getIncrementoExtra() %></td>
            <td><%= asignacion.getCostoBase() %></td>
            <td><%= asignacion.getTotalAsignacion() %></td>
            <td>
                <ul style="margin:0; padding-left:18px;">
                    <% if (asignacion.getSubTareas() != null && !asignacion.getSubTareas().isEmpty()) {
                        for (com.example.catedrapoo.model.SubTarea subTarea : asignacion.getSubTareas()) { %>
                            <li><b><%= subTarea.getTitulo() %></b>: <%= subTarea.getDescripcion() %></li>
                    <%   }
                       } else { %>
                        <li style="color:#888;">Sin subtareas</li>
                    <% } %>
                </ul>
                <form method="post" action="detalleCotizacion" style="margin-top:8px;">
                    <input type="hidden" name="action" value="addSubTarea" />
                    <input type="hidden" name="cotizacionId" value="<%= cotizacionId %>" />
                    <input type="hidden" name="idAsignacion" value="<%= asignacion.getId() %>" />
                    <input type="text" name="tituloSubTarea" placeholder="Título" required <%= cotizacionBloqueada ? "disabled" : "" %> />
                    <input type="text" name="descripcionSubTarea" placeholder="Descripción" required <%= cotizacionBloqueada ? "disabled" : "" %> />
                    <button class="btn" type="submit" <%= cotizacionBloqueada ? "disabled style='background:#888;cursor:not-allowed;'" : "" %>>Agregar Subtarea</button>
                </form>
            </td>
        </tr>
    <% } %>
</table>

<h3>Costos Adicionales</h3>
<table>
    <tr>
        <th>Descripción</th>
        <th>Monto</th>
        <th>Acciones</th>
    </tr>
    <% if (cotizacion != null && cotizacion.getCostosAdicionalesList() != null && !cotizacion.getCostosAdicionalesList().isEmpty()) {
           for (com.example.catedrapoo.model.CostoAdicional costo : cotizacion.getCostosAdicionalesList()) { %>
        <tr>
            <td><%= costo.getDescripcion() %></td>
            <td><%= costo.getMonto() %></td>
            <td>
                <form method="post" action="detalleCotizacion" style="display:inline;">
                    <input type="hidden" name="action" value="deleteCostoAdicional" />
                    <input type="hidden" name="cotizacionId" value="<%= cotizacionId %>" />
                    <input type="hidden" name="idCostoAdicional" value="<%= costo.getId() %>" />
                    <button class="btn" type="submit">Eliminar</button>
                </form>
            </td>
        </tr>
    <%   }
       } else { %>
        <tr><td colspan="3" style="color:#888;">Sin costos adicionales</td></tr>
    <% } %>
</table>

<h4>Agregar Costo Adicional</h4>
<form method="post" action="detalleCotizacion">
    <input type="hidden" name="action" value="addCostoAdicional" />
    <input type="hidden" name="cotizacionId" value="<%= cotizacionId %>" />
    <input type="text" name="descripcionCosto" placeholder="Descripción" required <%= cotizacionBloqueada ? "disabled" : "" %> />
    <input type="number" step="0.01" name="montoCosto" placeholder="Monto" required <%= cotizacionBloqueada ? "disabled" : "" %> />
    <button class="btn" type="submit" <%= cotizacionBloqueada ? "disabled style='background:#888;cursor:not-allowed;'" : "" %>>Agregar Costo</button>
</form>

<a href="cotizaciones">&larr; Volver a Cotizaciones</a>
</body>
</html>
