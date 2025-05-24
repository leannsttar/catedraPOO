package com.example.catedrapoo;

import com.example.catedrapoo.dao.ClienteDAO;
import com.example.catedrapoo.model.Cliente;
import com.example.catedrapoo.utils.ConexionDB;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/clientes")
public class ClienteServlet extends HttpServlet {
    private ClienteDAO clienteDAO;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = ConexionDB.getConnection();
            clienteDAO = new ClienteDAO(conn);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Cliente> clientes = clienteDAO.listarTodos();
            req.setAttribute("clientes", clientes);
            req.getRequestDispatcher("/clientes.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String action = req.getParameter("action");
            if ("updateCliente".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                String estado = req.getParameter("estado");
                if ("Inactivo".equals(estado)) {
                    clienteDAO.actualizarInactivacion(id, true);
                } else {
                    clienteDAO.actualizarInactivacion(id, false);
                }
                resp.sendRedirect("clientes");
                return;
            }
            Cliente cliente = new Cliente();
            cliente.setNombre(req.getParameter("nombre"));
            cliente.setDocumentoIdentificacion(req.getParameter("documento"));
            cliente.setTipoPersona(req.getParameter("tipoPersona"));
            cliente.setTelefono(req.getParameter("telefono"));
            cliente.setCorreo(req.getParameter("correo"));
            cliente.setDireccion(req.getParameter("direccion"));
            cliente.setEstado(req.getParameter("estado"));
            cliente.setFechaCreacion(LocalDateTime.now());
            cliente.setFechaActualizacion(LocalDateTime.now());
            cliente.setFechaInactivacion(null);
            clienteDAO.crear(cliente);
            resp.sendRedirect("clientes");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
