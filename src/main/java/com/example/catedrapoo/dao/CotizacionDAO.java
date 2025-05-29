package com.example.catedrapoo.dao;

import com.example.catedrapoo.model.Cotizacion;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CotizacionDAO {
    private final Connection connection;

    public CotizacionDAO(Connection connection) {
        this.connection = connection;
    }

    public void crear(Cotizacion cotizacion) throws SQLException {
        // Guardar el ID del usuario logeado en el campo ID_Empleado_Elabora (aunque sea un usuario, no un empleado)
        String sql = "INSERT INTO COTIZACION (Estado_Cotizacion, Titulo_Cotizacion, Fecha_Tentativa_Fin, Fecha_Tentativa_Inicio, Cantidad_Horas_Del_Proyecto, Costo_De_Asignaciones, Costos_Adicionales, Total_Cotizacion, ID_Cliente, ID_Empleado_Elabora) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cotizacion.getEstado());
            stmt.setString(2, cotizacion.getTituloCotizacion());
            stmt.setTimestamp(3, cotizacion.getFechaFin() != null ? Timestamp.valueOf(cotizacion.getFechaFin()) : null);
            stmt.setTimestamp(4, cotizacion.getFechaInicio() != null ? Timestamp.valueOf(cotizacion.getFechaInicio()) : null);
            stmt.setInt(5, cotizacion.getCantidadHorasTotales());
            stmt.setDouble(6, cotizacion.getCostoAsignaciones());
            stmt.setDouble(7, cotizacion.getCostosAdicionales());
            stmt.setDouble(8, cotizacion.getTotalCotizacion());
            stmt.setInt(9, cotizacion.getIdCliente());
            // Aquí se guarda el ID del usuario logeado (de la tabla USUARIO)
            if (cotizacion.getIdEmpleadoElabora() != null) {
                stmt.setInt(10, cotizacion.getIdEmpleadoElabora());
            } else {
                stmt.setNull(10, java.sql.Types.INTEGER);
            }
            stmt.executeUpdate();
        }
    }

    public Cotizacion obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM COTIZACION WHERE ID_Cotizacion = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public List<Cotizacion> listarTodos() throws SQLException {
        List<Cotizacion> cotizaciones = new ArrayList<>();
        String sql = "SELECT * FROM COTIZACION";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cotizaciones.add(mapRow(rs));
            }
        }
        return cotizaciones;
    }

    public void actualizar(Cotizacion cotizacion) throws SQLException {
        String sql = "UPDATE COTIZACION SET Estado_Cotizacion=?, Titulo_Cotizacion=?, Fecha_Tentativa_Fin=?, Fecha_Tentativa_Inicio=?, Cantidad_Horas_Del_Proyecto=?, Costo_De_Asignaciones=?, Costos_Adicionales=?, Total_Cotizacion=? WHERE ID_Cotizacion=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cotizacion.getEstado());
            stmt.setString(2, cotizacion.getTituloCotizacion());
            stmt.setTimestamp(3, cotizacion.getFechaFin() != null ? Timestamp.valueOf(cotizacion.getFechaFin()) : null);
            stmt.setTimestamp(4, cotizacion.getFechaInicio() != null ? Timestamp.valueOf(cotizacion.getFechaInicio()) : null);
            stmt.setInt(5, cotizacion.getCantidadHorasTotales());
            stmt.setDouble(6, cotizacion.getCostoAsignaciones());
            stmt.setDouble(7, cotizacion.getCostosAdicionales());
            stmt.setDouble(8, cotizacion.getTotalCotizacion());
            stmt.setInt(9, cotizacion.getId());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM COTIZACION WHERE ID_Cotizacion=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void recalcularValores(int cotizacionId) throws SQLException {
        // Calcular horas, costos y fechas de actividades
        String sql = "SELECT SUM(Cantidad_Horas_Aproximadas) AS TotalHoras, SUM(Total_Asignacion) AS TotalCostos, MIN(Fecha_Hora_Inicio) AS FechaInicio, MAX(Fecha_Hora_Fin) AS FechaFin FROM ASIGNACION_ACTIVIDAD WHERE ID_Cotizacion = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cotizacionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int totalHoras = rs.getInt("TotalHoras");
                    double totalCostos = rs.getDouble("TotalCostos");
                    java.sql.Timestamp fechaInicio = rs.getTimestamp("FechaInicio");
                    java.sql.Timestamp fechaFin = rs.getTimestamp("FechaFin");

                    // Sumar costos adicionales
                    double costosAdicionales = 0.0;
                    String sqlCostos = "SELECT SUM(Monto) AS TotalAdicional FROM COSTO_ADICIONAL WHERE ID_Cotizacion = ?";
                    try (PreparedStatement stmtCostos = connection.prepareStatement(sqlCostos)) {
                        stmtCostos.setInt(1, cotizacionId);
                        try (ResultSet rsCostos = stmtCostos.executeQuery()) {
                            if (rsCostos.next()) {
                                costosAdicionales = rsCostos.getDouble("TotalAdicional");
                            }
                        }
                    }

                    String updateSql = "UPDATE COTIZACION SET Cantidad_Horas_Del_Proyecto = ?, Costo_De_Asignaciones = ?, Costos_Adicionales = ?, Total_Cotizacion = ? + ?, Fecha_Tentativa_Inicio = ?, Fecha_Tentativa_Fin = ? WHERE ID_Cotizacion = ?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, totalHoras);
                        updateStmt.setDouble(2, totalCostos);
                        updateStmt.setDouble(3, costosAdicionales);
                        updateStmt.setDouble(4, totalCostos);
                        updateStmt.setDouble(5, costosAdicionales);
                        updateStmt.setTimestamp(6, fechaInicio);
                        updateStmt.setTimestamp(7, fechaFin);
                        updateStmt.setInt(8, cotizacionId);
                        updateStmt.executeUpdate();
                    }
                }
            }
        }
    }

    public void actualizarEstado(int id, String nuevoEstado) throws SQLException {
        String sql = "UPDATE COTIZACION SET Estado_Cotizacion = ?, Fecha_Actualizacion = NOW() WHERE ID_Cotizacion = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    public void actualizarFechasInactivacion(int id, boolean inactivar) throws SQLException {
        String sql = inactivar ?
            "UPDATE COTIZACION SET Estado_Cotizacion = 'Inactiva', Fecha_Inactivacion = NOW(), Fecha_Actualizacion = NOW() WHERE ID_Cotizacion = ?" :
            "UPDATE COTIZACION SET Estado_Cotizacion = 'En proceso', Fecha_Inactivacion = NULL, Fecha_Actualizacion = NOW() WHERE ID_Cotizacion = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Cotizacion mapRow(ResultSet rs) throws SQLException {
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setId(rs.getInt("ID_Cotizacion"));
        cotizacion.setTituloCotizacion(rs.getString("Titulo_Cotizacion"));
        cotizacion.setEstado(rs.getString("Estado_Cotizacion"));
        cotizacion.setFechaFin(rs.getTimestamp("Fecha_Tentativa_Fin") != null ? rs.getTimestamp("Fecha_Tentativa_Fin").toLocalDateTime() : null);
        cotizacion.setFechaInicio(rs.getTimestamp("Fecha_Tentativa_Inicio") != null ? rs.getTimestamp("Fecha_Tentativa_Inicio").toLocalDateTime() : null);
        cotizacion.setCantidadHorasTotales(rs.getInt("Cantidad_Horas_Del_Proyecto"));
        cotizacion.setCostoAsignaciones(rs.getDouble("Costo_De_Asignaciones"));
        cotizacion.setCostosAdicionales(rs.getDouble("Costos_Adicionales"));
        cotizacion.setTotalCotizacion(rs.getDouble("Total_Cotizacion"));
        cotizacion.setIdCliente(rs.getObject("ID_Cliente") != null ? rs.getInt("ID_Cliente") : null);
        return cotizacion;
    }
}
