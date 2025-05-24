package com.example.catedrapoo;

import com.example.catedrapoo.dao.AsignacionDAO;
import com.example.catedrapoo.dao.SubTareaDAO;
import com.example.catedrapoo.dao.TrabajadorDAO;
import com.example.catedrapoo.dao.CotizacionDAO;
import com.example.catedrapoo.dao.ClienteDAO;
import com.example.catedrapoo.dao.CostoAdicionalDAO;
import com.example.catedrapoo.model.Asignacion;
import com.example.catedrapoo.model.SubTarea;
import com.example.catedrapoo.model.Trabajador;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/detalleCotizacion")
public class DetalleCotizacionServlet extends HttpServlet {
    private AsignacionDAO asignacionDAO;
    private SubTareaDAO subTareaDAO;
    private TrabajadorDAO trabajadorDAO;
    private CotizacionDAO cotizacionDAO;
    private ClienteDAO clienteDAO;
    private CostoAdicionalDAO costoAdicionalDAO;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/SistemaCotizaciones", "root", "");
            asignacionDAO = new AsignacionDAO(conn);
            subTareaDAO = new SubTareaDAO(conn);
            trabajadorDAO = new TrabajadorDAO(conn);
            cotizacionDAO = new CotizacionDAO(conn);
            clienteDAO = new ClienteDAO(conn);
            costoAdicionalDAO = new CostoAdicionalDAO(conn);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int cotizacionId = Integer.parseInt(req.getParameter("id"));
        try {
            List<Asignacion> asignaciones = asignacionDAO.listarPorCotizacion(cotizacionId);
            Map<Integer, String> empleadoMap = new HashMap<>();
            for (Asignacion asignacion : asignaciones) {
                asignacion.setSubTareas(subTareaDAO.listarPorAsignacion(asignacion.getId()));
                if (asignacion.getIdEmpleado() != null && !empleadoMap.containsKey(asignacion.getIdEmpleado())) {
                    Trabajador t = trabajadorDAO.obtenerPorId(asignacion.getIdEmpleado());
                    if (t != null) empleadoMap.put(t.getId(), t.getNombre());
                }
            }
            Cotizacion cotizacion = cotizacionDAO.obtenerPorId(cotizacionId);
            if (cotizacion != null) {
                cotizacion.setCostosAdicionalesList(costoAdicionalDAO.listarPorCotizacion(cotizacionId));
            }
            String clienteNombre = null;
            if (cotizacion != null && cotizacion.getIdCliente() != null) {
                Cliente cliente = clienteDAO.obtenerPorId(cotizacion.getIdCliente());
                if (cliente != null) {
                    clienteNombre = cliente.getNombre();
                }
            }
            // Cargar todos los empleados para el select de asignación
            List<Trabajador> empleados = trabajadorDAO.listarTodos();
            req.setAttribute("empleados", empleados);
            req.setAttribute("asignaciones", asignaciones);
            req.setAttribute("cotizacionId", cotizacionId);
            req.setAttribute("empleadoMap", empleadoMap);
            req.setAttribute("clienteNombre", clienteNombre);
            req.setAttribute("cotizacion", cotizacion);
            req.getRequestDispatcher("/detalleCotizacion.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("addAsignacion".equals(action)) {
                Asignacion asignacion = new Asignacion();
                asignacion.setTitulo(req.getParameter("titulo"));
                asignacion.setArea(req.getParameter("area"));
                // Cambiar a ID_Empleado_Asignado para que coincida con la base de datos
                asignacion.setIdEmpleado(Integer.parseInt(req.getParameter("idEmpleado")));
                asignacion.setCostoPorHora(Double.parseDouble(req.getParameter("costoPorHora")));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                asignacion.setFechaInicio(LocalDateTime.parse(req.getParameter("fechaInicio"), formatter));
                asignacion.setFechaFin(LocalDateTime.parse(req.getParameter("fechaFin"), formatter));
                asignacion.setCantidadHoras(Integer.parseInt(req.getParameter("cantidadHoras")));
                asignacion.setIncrementoExtra(Double.parseDouble(req.getParameter("incrementoExtra")));
                // Calcular costo base automáticamente
                double costoPorHora = asignacion.getCostoPorHora();
                int cantidadHoras = asignacion.getCantidadHoras();
                double costoBase = costoPorHora * cantidadHoras;
                asignacion.setCostoBase(costoBase);
                // Calcular el total basado en costo base e incremento extra
                double incremento = asignacion.getIncrementoExtra();
                double totalAsignacion = costoBase + (costoBase * incremento / 100.0);
                asignacion.setTotalAsignacion(totalAsignacion);
                asignacion.setIdCotizacion(Integer.parseInt(req.getParameter("cotizacionId")));
                asignacionDAO.crear(asignacion);

                // Recalculate and update summary values for the quotation
                int cotizacionId = asignacion.getIdCotizacion();
                cotizacionDAO.recalcularValores(cotizacionId);
            } else if ("addSubTarea".equals(action)) {
                int idAsignacion = Integer.parseInt(req.getParameter("idAsignacion"));
                SubTarea subTarea = new SubTarea();
                subTarea.setTitulo(req.getParameter("tituloSubTarea"));
                subTarea.setDescripcion(req.getParameter("descripcionSubTarea"));
                subTareaDAO.crear(subTarea, idAsignacion);
            } else if ("addCostoAdicional".equals(action)) {
                int cotizacionId = Integer.parseInt(req.getParameter("cotizacionId"));
                String descripcion = req.getParameter("descripcionCosto");
                double monto = Double.parseDouble(req.getParameter("montoCosto"));
                com.example.catedrapoo.model.CostoAdicional costo = new com.example.catedrapoo.model.CostoAdicional();
                costo.setIdCotizacion(cotizacionId);
                costo.setDescripcion(descripcion);
                costo.setMonto(monto);
                costoAdicionalDAO.crear(costo);
                cotizacionDAO.recalcularValores(cotizacionId);
            } else if ("deleteCostoAdicional".equals(action)) {
                int idCosto = Integer.parseInt(req.getParameter("idCostoAdicional"));
                int cotizacionId = Integer.parseInt(req.getParameter("cotizacionId"));
                costoAdicionalDAO.eliminar(idCosto);
                cotizacionDAO.recalcularValores(cotizacionId);
            }
            resp.sendRedirect("detalleCotizacion?id=" + req.getParameter("cotizacionId"));
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
