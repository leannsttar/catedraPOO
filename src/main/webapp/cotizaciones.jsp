<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<%
    if (session.getAttribute("usuario") == null) {
        response.sendRedirect("login");
        return;
    }
    com.example.catedrapoo.model.Usuario usuario = (com.example.catedrapoo.model.Usuario) session.getAttribute("usuario");
    java.util.List<com.example.catedrapoo.model.Cotizacion> cotizaciones = (java.util.List<com.example.catedrapoo.model.Cotizacion>) request.getAttribute("cotizaciones");
    Map<Integer, String> clienteMap = (Map<Integer, String>) request.getAttribute("clienteMap");
    Map<Integer, List<com.example.catedrapoo.model.Asignacion>> asignacionesPorCotizacion =
            (Map<Integer, List<com.example.catedrapoo.model.Asignacion>>) request.getAttribute("asignacionesPorCotizacion");
    if (asignacionesPorCotizacion == null) {
        asignacionesPorCotizacion = new HashMap<>();
    }
    Map<Integer, String> empleadoMap = (Map<Integer, String>) request.getAttribute("empleadoMap");
%>
<html>
<head>
    <title>Cotizaciones</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%@ include file="components/navbar.jsp" %>
<div class="container">
    <h3>Agregar Cotización</h3>
    <form method="post" action="cotizaciones">
        <input type="text" name="titulo" placeholder="Título de la Cotización" required/>
        <select name="clienteId" required>
            <option value="">Seleccione un cliente</option>
            <% if (clienteMap != null) for (Map.Entry<Integer, String> entry : clienteMap.entrySet()) { %>
            <option value="<%= entry.getKey() %>"><%= entry.getValue() %>
            </option>
            <% } %>
        </select>
        <input type="hidden" name="estado" value="En proceso"/>
        <button class="btn" type="submit">Crear Cotización</button>
    </form>
    <h2>Lista de Cotizaciones</h2>
    <table>
        <tr>
            <th>ID</th>
            <th>Título</th>
            <th>Cliente</th>
            <th>Horas Totales</th>
            <th>Fecha Inicio</th>
            <th>Fecha Fin</th>
            <th>Costo Asignaciones</th>
            <th>Costos Adicionales</th>
            <th>Total</th>
            <th>Estado</th>
            <th>Acciones</th>
        </tr>
        <% if (cotizaciones != null) for (com.example.catedrapoo.model.Cotizacion cotizacion : cotizaciones) { %>
        <tr>
            <td><%= cotizacion.getId() %>
            </td>
            <td><%= cotizacion.getTituloCotizacion() != null ? cotizacion.getTituloCotizacion() : "-" %>
            </td>
            <td><%= (cotizacion.getIdCliente() != null && clienteMap != null && clienteMap.get(cotizacion.getIdCliente()) != null) ? clienteMap.get(cotizacion.getIdCliente()) : "Sin cliente" %>
            </td>
            <td><%= cotizacion.getCantidadHorasTotales() > 0 ? cotizacion.getCantidadHorasTotales() : "No se han añadido actividades" %>
            </td>
            <td><%= cotizacion.getFechaInicio() != null ? cotizacion.getFechaInicio() : "-" %>
            </td>
            <td><%= cotizacion.getFechaFin() != null ? cotizacion.getFechaFin() : "-" %>
            </td>
            <td><%= cotizacion.getCostoAsignaciones() > 0 ? cotizacion.getCostoAsignaciones() : "No se han añadido actividades" %>
            </td>
            <td><%= cotizacion.getCostosAdicionales() > 0 ? cotizacion.getCostosAdicionales() : "-" %>
            </td>
            <td><%= cotizacion.getTotalCotizacion() > 0 ? cotizacion.getTotalCotizacion() : "No se han añadido actividades" %>
            </td>
            <td><%= cotizacion.getEstado() != null ? cotizacion.getEstado() : "-" %>
            </td>
            <td>
                <a href="detalleCotizacion?id=<%= cotizacion.getId() %>">Ver Detalle</a>
                <form method="post" action="cotizaciones" style="display:inline;">
                    <input type="hidden" name="action" value="updateCotizacion"/>
                    <input type="hidden" name="id" value="<%= cotizacion.getId() %>"/>
                    <select name="estado">
                        <option value="En proceso" <%= "En proceso".equals(cotizacion.getEstado()) ? "selected" : "" %>>
                            En proceso
                        </option>
                        <option value="Finalizada" <%= "Finalizada".equals(cotizacion.getEstado()) ? "selected" : "" %>>
                            Finalizada
                        </option>
                        <option value="Inactiva" <%= "Inactiva".equals(cotizacion.getEstado()) ? "selected" : "" %>>
                            Inactiva
                        </option>
                    </select>
                    <button class="btn" type="submit">Actualizar</button>
                </form>
            </td>
        </tr>
        <% } %>
    </table>


</div>

</body>
</html>
