package com.example.catedrapoo;

import com.example.catedrapoo.dao.CotizacionDAO;
import com.example.catedrapoo.dao.ClienteDAO;
import com.example.catedrapoo.model.Cotizacion;
import com.example.catedrapoo.model.Cliente;

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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/cotizaciones")
public class CotizacionServlet extends HttpServlet {
    private CotizacionDAO cotizacionDAO;
    private ClienteDAO clienteDAO;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/SistemaCotizaciones", "root", "");
            cotizacionDAO = new CotizacionDAO(conn);
            clienteDAO = new ClienteDAO(conn);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Cotizacion> cotizaciones = cotizacionDAO.listarTodos();
            List<Cliente> clientes = clienteDAO.listarTodos();
            Map<Integer, String> clienteMap = new HashMap<>();
            for (Cliente c : clientes) {
                clienteMap.put(c.getId(), c.getNombre());
            }
            req.setAttribute("cotizaciones", cotizaciones);
            req.setAttribute("clienteMap", clienteMap);
            req.getRequestDispatcher("/cotizaciones.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String action = req.getParameter("action");
            if ("updateCotizacion".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Cotizacion cotizacion = cotizacionDAO.obtenerPorId(id);
                if (cotizacion != null) {
                    cotizacion.setEstado(req.getParameter("estado"));
                    cotizacionDAO.actualizar(cotizacion);
                }
                resp.sendRedirect("cotizaciones");
                return;
            }
            Cotizacion cotizacion = new Cotizacion();
            cotizacion.setEstado(req.getParameter("estado"));
            cotizacion.setTituloCotizacion(req.getParameter("titulo"));
            
            // Obtener el usuario logeado y setear como ID_Empleado_Elabora
            com.example.catedrapoo.model.Usuario usuario = (com.example.catedrapoo.model.Usuario) req.getSession().getAttribute("usuario");
            if (usuario != null) {
                cotizacion.setIdEmpleadoElabora(usuario.getId());
            }
            cotizacion.setIdCliente(Integer.parseInt(req.getParameter("clienteId")));
            cotizacion.setCantidadHorasTotales(0);
            cotizacion.setCostoAsignaciones(0.0);
            cotizacion.setCostosAdicionales(0.0);
            cotizacion.setTotalCotizacion(0.0);
            cotizacion.setFechaInicio(null);
            cotizacion.setFechaFin(null);
            cotizacionDAO.crear(cotizacion);
            resp.sendRedirect("cotizaciones");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
