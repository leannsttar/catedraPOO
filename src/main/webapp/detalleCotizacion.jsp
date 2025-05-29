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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%@ include file="components/navbar.jsp" %>
<div class="container">
    <div class="section-container">
        <h2> 
            <%= cotizacion != null && cotizacion.getTituloCotizacion() != null ? cotizacion.getTituloCotizacion() : "" %>
            <% String clienteNombre = (String) request.getAttribute("clienteNombre"); %>
            <% if (clienteNombre != null && !clienteNombre.isEmpty()) { %>
            del cliente '<%= clienteNombre %>'
            <% } %>
        </h2>
    </div>

    <div class="section-container">
        <h3>Agregar Actividad (Asignación)</h3>
        <form method="post" action="detalleCotizacion">
            <input type="hidden" name="action" value="addAsignacion"/>
            <input type="hidden" name="cotizacionId" value="<%= cotizacionId %>"/>
            <input type="text" name="titulo" placeholder="Título" required <%= cotizacionBloqueada ? "disabled" : "" %> />
            <input type="text" name="area" placeholder="Área" required <%= cotizacionBloqueada ? "disabled" : "" %> />
            <select name="idEmpleado" required <%= cotizacionBloqueada ? "disabled" : "" %>>
                <option value="">Seleccione un empleado</option>
                <%
                    java.util.List<com.example.catedrapoo.model.Trabajador> empleados = (java.util.List<com.example.catedrapoo.model.Trabajador>) request.getAttribute("empleados");
                    if (empleados != null) for (com.example.catedrapoo.model.Trabajador emp : empleados) { %>
                <option value="<%= emp.getId() %>"><%= emp.getNombre() %>
                </option>
                <% } %>
            </select>
            <input type="number" step="0.01" name="costoPorHora" placeholder="Costo/Hora"
                   required <%= cotizacionBloqueada ? "disabled" : "" %> />
            <input type="datetime-local" name="fechaInicio" placeholder="Fecha Inicio"
                   required <%= cotizacionBloqueada ? "disabled" : "" %> />
            <input type="datetime-local" name="fechaFin" placeholder="Fecha Fin"
                   required <%= cotizacionBloqueada ? "disabled" : "" %> />
            <input type="number" name="cantidadHoras" placeholder="Horas"
                   required <%= cotizacionBloqueada ? "disabled" : "" %> />
            <input type="number" step="0.01" name="incrementoExtra" placeholder="Incremento Extra (%)"
                   required <%= cotizacionBloqueada ? "disabled" : "" %> />
            <button class="btn"
                    type="submit" <%= cotizacionBloqueada ? "disabled style='background:#888;cursor:not-allowed;'" : "" %>>
                Agregar Actividad
            </button>
        </form>
    </div>

    <div class="section-container">
        <h3>Actividades</h3>
        <div class="activities-table">
            <table>
                <tr>
                    <th class="title-column">Título</th>
                    <th class="area-column">Área</th>
                    <th class="employee-column">Empleado</th>
                    <th class="date-column">Fecha Inicio</th>
                    <th class="date-column">Fecha Fin</th>
                    <th class="number-column">Horas</th>
                    <th class="number-column">Costo/Hora</th>
                    <th class="number-column">Total</th>
                    <th class="actions-column">Acciones</th>
                </tr>
                <% if (asignaciones != null) for (com.example.catedrapoo.model.Asignacion asignacion : asignaciones) { %>
                <tr>
                    <td><%= asignacion.getTitulo() %>
                    </td>
                    <td><%= asignacion.getArea() %>
                    </td>
                    <td><%= (asignacion.getIdEmpleado() != null && empleadoMap != null && empleadoMap.get(asignacion.getIdEmpleado()) != null) ? empleadoMap.get(asignacion.getIdEmpleado()) : "" %>
                    </td>
                    <td><%= asignacion.getFechaInicio() %>
                    </td>
                    <td><%= asignacion.getFechaFin() %>
                    </td>
                    <td><%= asignacion.getCantidadHoras() %>
                    </td>
                    <td><%= asignacion.getCostoPorHora() %>
                    </td>
                    <td><%= asignacion.getTotalAsignacion() %>
                    </td>
                    <td>
                        <form method="post" action="detalleCotizacion" style="display:inline;">
                            <input type="hidden" name="action" value="deleteAsignacion"/>
                            <input type="hidden" name="cotizacionId" value="<%= cotizacionId %>"/>
                            <input type="hidden" name="idAsignacion" value="<%= asignacion.getId() %>"/>
                            <button class="btn" type="submit" <%= cotizacionBloqueada ? "disabled style='background:#888;cursor:not-allowed;'" : "" %>>
                                Eliminar
                            </button>
                        </form>
                    </td>
                </tr>
                <tr class="subtasks-row">
                    <td colspan="9">
                        <div class="subtasks-container">
                            <h4>Subtareas de <%= asignacion.getTitulo() %></h4>
                            <table class="subtasks-table">
                                <tr>
                                    <th>Título</th>
                                    <th>Descripción</th>
                                    <th>Acciones</th>
                                </tr>
                                <% if (asignacion.getSubTareas() != null && !asignacion.getSubTareas().isEmpty()) {
                                    for (com.example.catedrapoo.model.SubTarea subTarea : asignacion.getSubTareas()) { %>
                                <tr>
                                    <td><%= subTarea.getTitulo() %></td>
                                    <td><%= subTarea.getDescripcion() %></td>
                                    <td>
                                        <form method="post" action="detalleCotizacion" style="display:inline;">
                                            <input type="hidden" name="action" value="deleteSubTarea"/>
                                            <input type="hidden" name="cotizacionId" value="<%= cotizacionId %>"/>
                                            <input type="hidden" name="idAsignacion" value="<%= asignacion.getId() %>"/>
                                            <input type="hidden" name="idSubTarea" value="<%= subTarea.getId() %>"/>
                                            <button class="btn" type="submit" <%= cotizacionBloqueada ? "disabled style='background:#888;cursor:not-allowed;'" : "" %>>
                                                Eliminar
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                                <% }
                                } else { %>
                                <tr>
                                    <td colspan="3" style="color:#888;">Sin subtareas</td>
                                </tr>
                                <% } %>
                            </table>

                            <!-- Formulario para agregar subtarea -->
                            <form method="post" action="detalleCotizacion" class="subtask-form">
                                <input type="hidden" name="action" value="addSubTarea"/>
                                <input type="hidden" name="cotizacionId" value="<%= cotizacionId %>"/>
                                <input type="hidden" name="idAsignacion" value="<%= asignacion.getId() %>"/>
                                <input type="text" name="tituloSubTarea" placeholder="Título de subtarea" required <%= cotizacionBloqueada ? "disabled" : "" %>/>
                                <input type="text" name="descripcionSubTarea" placeholder="Descripción" required <%= cotizacionBloqueada ? "disabled" : "" %>/>
                                <button class="btn" type="submit" <%= cotizacionBloqueada ? "disabled style='background:#888;cursor:not-allowed;'" : "" %>>
                                    Agregar Subtarea
                                </button>
                            </form>
                        </div>
                    </td>
                </tr>
                <% } %>
            </table>
        </div>
    </div>

    <div class="section-container">
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
                <td><%= costo.getDescripcion() %>
                </td>
                <td><%= costo.getMonto() %>
                </td>
                <td>
                    <form method="post" action="detalleCotizacion" style="display:inline;">
                        <input type="hidden" name="action" value="deleteCostoAdicional"/>
                        <input type="hidden" name="cotizacionId" value="<%= cotizacionId %>"/>
                        <input type="hidden" name="idCostoAdicional" value="<%= costo.getId() %>"/>
                        <button class="btn" type="submit">Eliminar</button>
                    </form>
                </td>
            </tr>
            <% }
            } else { %>
            <tr>
                <td colspan="3" style="color:#888;">Sin costos adicionales</td>
            </tr>
            <% } %>
        </table>

        <h4>Agregar Costo Adicional</h4>
        <form method="post" action="detalleCotizacion">
            <input type="hidden" name="action" value="addCostoAdicional"/>
            <input type="hidden" name="cotizacionId" value="<%= cotizacionId %>"/>
            <input type="text" name="descripcionCosto" placeholder="Descripción"
                   required <%= cotizacionBloqueada ? "disabled" : "" %> />
            <input type="number" step="0.01" name="montoCosto" placeholder="Monto"
                   required <%= cotizacionBloqueada ? "disabled" : "" %> />
            <button class="btn"
                    type="submit" <%= cotizacionBloqueada ? "disabled style='background:#888;cursor:not-allowed;'" : "" %>>
                Agregar Costo
            </button>
        </form>
    </div>

    <a href="cotizaciones">&larr; Volver a Cotizaciones</a>
</div>
</body>
</html>
