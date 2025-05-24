package com.example.catedrapoo.dao;

import com.example.catedrapoo.model.Trabajador;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TrabajadorDAO {
    private final Connection connection;

    public TrabajadorDAO(Connection connection) {
        this.connection = connection;
    }

    public void crear(Trabajador trabajador) throws SQLException {
        String sql = "INSERT INTO EMPLEADO (Nombre_Empleado, Documento_Identificacion, Tipo_Persona, Tipo_Contratacion, Telefono, Correo_Electronico, Direccion, Estado_Empleado, Fecha_Creacion, Fecha_Actualizacion, Fecha_Inactivacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, trabajador.getNombre());
            stmt.setString(2, trabajador.getDocumentoIdentificacion());
            stmt.setString(3, trabajador.getTipoPersona());
            stmt.setString(4, trabajador.getTipoContratacion());
            stmt.setString(5, trabajador.getTelefono());
            stmt.setString(6, trabajador.getCorreo());
            stmt.setString(7, trabajador.getDireccion());
            stmt.setString(8, trabajador.getEstado());
            stmt.setTimestamp(9, trabajador.getFechaCreacion() != null ? Timestamp.valueOf(trabajador.getFechaCreacion()) : null);
            stmt.setTimestamp(10, trabajador.getFechaActualizacion() != null ? Timestamp.valueOf(trabajador.getFechaActualizacion()) : null);
            stmt.setTimestamp(11, trabajador.getFechaInactivacion() != null ? Timestamp.valueOf(trabajador.getFechaInactivacion()) : null);
            stmt.executeUpdate();
        }
    }

    public Trabajador obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM EMPLEADO WHERE ID_Empleado = ?";
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

    public List<Trabajador> listarTodos() throws SQLException {
        List<Trabajador> trabajadores = new ArrayList<>();
        String sql = "SELECT * FROM EMPLEADO";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                trabajadores.add(mapRow(rs));
            }
        }
        return trabajadores;
    }

    public void actualizar(Trabajador trabajador) throws SQLException {
        String sql = "UPDATE EMPLEADO SET Nombre_Empleado=?, Documento_Identificacion=?, Tipo_Persona=?, Tipo_Contratacion=?, Telefono=?, Correo_Electronico=?, Direccion=?, Estado_Empleado=?, Fecha_Creacion=?, Fecha_Actualizacion=?, Fecha_Inactivacion=? WHERE ID_Empleado=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, trabajador.getNombre());
            stmt.setString(2, trabajador.getDocumentoIdentificacion());
            stmt.setString(3, trabajador.getTipoPersona());
            stmt.setString(4, trabajador.getTipoContratacion());
            stmt.setString(5, trabajador.getTelefono());
            stmt.setString(6, trabajador.getCorreo());
            stmt.setString(7, trabajador.getDireccion());
            stmt.setString(8, trabajador.getEstado());
            stmt.setTimestamp(9, trabajador.getFechaCreacion() != null ? Timestamp.valueOf(trabajador.getFechaCreacion()) : null);
            stmt.setTimestamp(10, trabajador.getFechaActualizacion() != null ? Timestamp.valueOf(trabajador.getFechaActualizacion()) : null);
            stmt.setTimestamp(11, trabajador.getFechaInactivacion() != null ? Timestamp.valueOf(trabajador.getFechaInactivacion()) : null);
            stmt.setInt(12, trabajador.getId());
            stmt.executeUpdate();
        }
    }

    public void actualizarEstado(int id, String nuevoEstado) throws SQLException {
        String sql = "UPDATE EMPLEADO SET Estado_Empleado = ?, Fecha_Actualizacion = NOW() WHERE ID_Empleado = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    public void actualizarInactivacion(int id, boolean inactivar) throws SQLException {
        String sql = inactivar ?
            "UPDATE EMPLEADO SET Estado_Empleado = 'Inactivo', Fecha_Inactivacion = NOW(), Fecha_Actualizacion = NOW() WHERE ID_Empleado = ?" :
            "UPDATE EMPLEADO SET Estado_Empleado = 'Activo', Fecha_Inactivacion = NULL, Fecha_Actualizacion = NOW() WHERE ID_Empleado = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM EMPLEADO WHERE ID_Empleado=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Trabajador mapRow(ResultSet rs) throws SQLException {
        return new Trabajador(
            rs.getInt("ID_Empleado"),
            rs.getString("Nombre_Empleado"),
            rs.getString("Documento_Identificacion"),
            rs.getString("Tipo_Persona"),
            rs.getString("Tipo_Contratacion"),
            rs.getString("Telefono"),
            rs.getString("Correo_Electronico"),
            rs.getString("Direccion"),
            rs.getString("Estado_Empleado"),
            rs.getTimestamp("Fecha_Creacion") != null ? rs.getTimestamp("Fecha_Creacion").toLocalDateTime() : null,
            rs.getTimestamp("Fecha_Actualizacion") != null ? rs.getTimestamp("Fecha_Actualizacion").toLocalDateTime() : null,
            rs.getTimestamp("Fecha_Inactivacion") != null ? rs.getTimestamp("Fecha_Inactivacion").toLocalDateTime() : null
        );
    }
}
