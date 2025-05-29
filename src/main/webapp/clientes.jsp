<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    if (session.getAttribute("usuario") == null) {
        response.sendRedirect("login");
        return;
    }
    com.example.catedrapoo.model.Usuario usuario = (com.example.catedrapoo.model.Usuario) session.getAttribute("usuario");
    java.util.List<com.example.catedrapoo.model.Cliente> clientes = (java.util.List<com.example.catedrapoo.model.Cliente>) request.getAttribute("clientes");
%>
<html>
<head>
    <title>Clientes</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%@ include file="components/navbar.jsp" %>
<div class="container">


    <h3>Agregar Cliente</h3>
    <form method="post" action="clientes">
        <input type="text" name="nombre" placeholder="Nombre" required/>
        <input type="text" name="documento" placeholder="Documento" required/>
        <select name="tipoPersona" required>
            <option value="">Tipo Persona</option>
            <option value="Natural">Natural</option>
            <option value="Juridica">Jurídica</option>
        </select>
        <input type="text" name="telefono" placeholder="Teléfono" required/>
        <input type="email" name="correo" placeholder="Correo" required/>
        <input type="text" name="direccion" placeholder="Dirección" required/>
        <select name="estado" required>
            <option value="">Estado</option>
            <option value="Activo">Activo</option>
            <option value="Inactivo">Inactivo</option>
        </select>
        <button class="btn" type="submit">Agregar</button>
    </form>

    <h2>Lista de Clientes</h2>
    <table>
        <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Documento</th>
            <th>Tipo Persona</th>
            <th>Teléfono</th>
            <th>Correo</th>
            <th>Dirección</th>
            <th>Estado</th>
            <th>Acciones</th>
        </tr>
        <% if (clientes != null) for (com.example.catedrapoo.model.Cliente cliente : clientes) { %>
        <tr>
            <td><%= cliente.getId() %>
            </td>
            <td><%= cliente.getNombre() %>
            </td>
            <td><%= cliente.getDocumentoIdentificacion() %>
            </td>
            <td><%= cliente.getTipoPersona() %>
            </td>
            <td><%= cliente.getTelefono() %>
            </td>
            <td><%= cliente.getCorreo() %>
            </td>
            <td><%= cliente.getDireccion() %>
            </td>
            <td><%= cliente.getEstado() %>
            </td>
            <td>
                <form method="post" action="clientes" style="display:inline;">
                    <input type="hidden" name="action" value="updateCliente"/>
                    <input type="hidden" name="id" value="<%= cliente.getId() %>"/>
                    <select name="estado">
                        <option value="Activo" <%= "Activo".equals(cliente.getEstado()) ? "selected" : "" %>>Activo
                        </option>
                        <option value="Inactivo" <%= "Inactivo".equals(cliente.getEstado()) ? "selected" : "" %>>
                            Inactivo
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
