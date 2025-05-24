package com.example.catedrapoo;

import com.example.catedrapoo.dao.TrabajadorDAO;
import com.example.catedrapoo.model.Trabajador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/empleados")
public class EmpleadoServlet extends HttpServlet {
    private TrabajadorDAO trabajadorDAO;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/SistemaCotizaciones", "root", "");
            trabajadorDAO = new TrabajadorDAO(conn);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Trabajador> empleados = trabajadorDAO.listarTodos();
            req.setAttribute("empleados", empleados);
            req.getRequestDispatcher("/empleados.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String action = req.getParameter("action");
            if ("updateEmpleado".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                String estado = req.getParameter("estado");
                if ("Inactivo".equals(estado)) {
                    trabajadorDAO.actualizarInactivacion(id, true);
                } else {
                    trabajadorDAO.actualizarInactivacion(id, false);
                }
                resp.sendRedirect("empleados");
                return;
            }
            Trabajador empleado = new Trabajador();
            empleado.setNombre(req.getParameter("nombre"));
            empleado.setDocumentoIdentificacion(req.getParameter("documento"));
            empleado.setTipoPersona(req.getParameter("tipoPersona"));
            empleado.setTipoContratacion(req.getParameter("tipoContratacion"));
            empleado.setTelefono(req.getParameter("telefono"));
            empleado.setCorreo(req.getParameter("correo"));
            empleado.setDireccion(req.getParameter("direccion"));
            empleado.setEstado(req.getParameter("estado"));
            empleado.setFechaCreacion(LocalDateTime.now());
            empleado.setFechaActualizacion(LocalDateTime.now());
            empleado.setFechaInactivacion(null);
            trabajadorDAO.crear(empleado);
            resp.sendRedirect("empleados");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
