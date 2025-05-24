package com.example.catedrapoo.dao;

import com.example.catedrapoo.model.Asignacion;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AsignacionDAO {
    private final Connection connection;

    public AsignacionDAO(Connection connection) {
        this.connection = connection;
    }

    public void crear(Asignacion asignacion) throws SQLException {
        String sql = "INSERT INTO ASIGNACION_ACTIVIDAD (Titulo_Actividad, Area_Asignada, ID_Empleado_Asignado, ID_Cotizacion, Costo_Por_Hora, Fecha_Hora_Inicio, Fecha_Hora_Fin, Cantidad_Horas_Aproximadas, Incremento_Extra_Porcentaje, Costo_Base, Total_Asignacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, asignacion.getTitulo());
            stmt.setString(2, asignacion.getArea());
            stmt.setInt(3, asignacion.getIdEmpleado()); // ID_Empleado_Asignado
            stmt.setInt(4, asignacion.getIdCotizacion());
            stmt.setDouble(5, asignacion.getCostoPorHora());
            stmt.setTimestamp(6, asignacion.getFechaInicio() != null ? Timestamp.valueOf(asignacion.getFechaInicio()) : null);
            stmt.setTimestamp(7, asignacion.getFechaFin() != null ? Timestamp.valueOf(asignacion.getFechaFin()) : null);
            stmt.setInt(8, asignacion.getCantidadHoras());
            stmt.setDouble(9, asignacion.getIncrementoExtra());
            stmt.setDouble(10, asignacion.getCostoBase());
            stmt.setDouble(11, asignacion.getTotalAsignacion());
            stmt.executeUpdate();
        }
    }

    public Asignacion obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM ASIGNACION_ACTIVIDAD WHERE ID_Asignacion = ?";
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

    public List<Asignacion> listarTodos() throws SQLException {
        List<Asignacion> asignaciones = new ArrayList<>();
        String sql = "SELECT * FROM ASIGNACION_ACTIVIDAD";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                asignaciones.add(mapRow(rs));
            }
        }
        return asignaciones;
    }

    public List<Asignacion> listarPorCotizacion(int cotizacionId) throws SQLException {
        List<Asignacion> asignaciones = new ArrayList<>();
        String sql = "SELECT * FROM ASIGNACION_ACTIVIDAD WHERE ID_Cotizacion = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cotizacionId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    asignaciones.add(mapRow(rs));
                }
            }
        }
        return asignaciones;
    }

    public void actualizar(Asignacion asignacion) throws SQLException {
        String sql = "UPDATE ASIGNACION_ACTIVIDAD SET Titulo_Actividad=?, Area_Asignada=?, Costo_Por_Hora=?, Fecha_Hora_Inicio=?, Fecha_Hora_Fin=?, Cantidad_Horas_Aproximadas=?, Incremento_Extra_Porcentaje=?, Costo_Base=?, Total_Asignacion=? WHERE ID_Asignacion=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, asignacion.getTitulo());
            stmt.setString(2, asignacion.getArea());
            stmt.setDouble(3, asignacion.getCostoPorHora());
            stmt.setTimestamp(4, asignacion.getFechaInicio() != null ? Timestamp.valueOf(asignacion.getFechaInicio()) : null);
            stmt.setTimestamp(5, asignacion.getFechaFin() != null ? Timestamp.valueOf(asignacion.getFechaFin()) : null);
            stmt.setInt(6, asignacion.getCantidadHoras());
            stmt.setDouble(7, asignacion.getIncrementoExtra());
            stmt.setDouble(8, asignacion.getCostoBase());
            stmt.setDouble(9, asignacion.getTotalAsignacion());
            stmt.setInt(10, asignacion.getId());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM ASIGNACION_ACTIVIDAD WHERE ID_Asignacion=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Asignacion mapRow(ResultSet rs) throws SQLException {
        Asignacion asignacion = new Asignacion(
            rs.getInt("ID_Asignacion"),
            rs.getString("Titulo_Actividad"),
            rs.getString("Area_Asignada"),
            rs.getDouble("Costo_Por_Hora"),
            rs.getTimestamp("Fecha_Hora_Inicio") != null ? rs.getTimestamp("Fecha_Hora_Inicio").toLocalDateTime() : null,
            rs.getTimestamp("Fecha_Hora_Fin") != null ? rs.getTimestamp("Fecha_Hora_Fin").toLocalDateTime() : null,
            rs.getInt("Cantidad_Horas_Aproximadas"),
            rs.getDouble("Incremento_Extra_Porcentaje"),
            rs.getDouble("Costo_Base"),
            rs.getDouble("Total_Asignacion")
        );
        asignacion.setIdEmpleado(rs.getObject("ID_Empleado_Asignado") != null ? rs.getInt("ID_Empleado_Asignado") : null);
        return asignacion;
    }
}
