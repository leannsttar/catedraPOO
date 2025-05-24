package com.example.catedrapoo.dao;

import com.example.catedrapoo.model.Cliente;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    private final Connection connection;

    public ClienteDAO(Connection connection) {
        this.connection = connection;
    }

    public void crear(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO CLIENTE (Nombre_Cliente, Documento_Identificacion, Tipo_Persona, Telefono_Contacto, Correo_Contacto, Direccion, Estado_Cliente, Fecha_Creacion, Fecha_Actualizacion, Fecha_Inactivacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getDocumentoIdentificacion());
            stmt.setString(3, cliente.getTipoPersona());
            stmt.setString(4, cliente.getTelefono());
            stmt.setString(5, cliente.getCorreo());
            stmt.setString(6, cliente.getDireccion());
            stmt.setString(7, cliente.getEstado());
            stmt.setTimestamp(8, cliente.getFechaCreacion() != null ? Timestamp.valueOf(cliente.getFechaCreacion()) : null);
            stmt.setTimestamp(9, cliente.getFechaActualizacion() != null ? Timestamp.valueOf(cliente.getFechaActualizacion()) : null);
            stmt.setTimestamp(10, cliente.getFechaInactivacion() != null ? Timestamp.valueOf(cliente.getFechaInactivacion()) : null);
            stmt.executeUpdate();
        }
    }

    public Cliente obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM CLIENTE WHERE ID_Cliente = ?";
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

    public List<Cliente> listarTodos() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM CLIENTE";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clientes.add(mapRow(rs));
            }
        }
        return clientes;
    }

    public void actualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE CLIENTE SET Nombre_Cliente=?, Documento_Identificacion=?, Tipo_Persona=?, Telefono_Contacto=?, Correo_Contacto=?, Direccion=?, Estado_Cliente=?, Fecha_Creacion=?, Fecha_Actualizacion=?, Fecha_Inactivacion=? WHERE ID_Cliente=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getDocumentoIdentificacion());
            stmt.setString(3, cliente.getTipoPersona());
            stmt.setString(4, cliente.getTelefono());
            stmt.setString(5, cliente.getCorreo());
            stmt.setString(6, cliente.getDireccion());
            stmt.setString(7, cliente.getEstado());
            stmt.setTimestamp(8, cliente.getFechaCreacion() != null ? Timestamp.valueOf(cliente.getFechaCreacion()) : null);
            stmt.setTimestamp(9, cliente.getFechaActualizacion() != null ? Timestamp.valueOf(cliente.getFechaActualizacion()) : null);
            stmt.setTimestamp(10, cliente.getFechaInactivacion() != null ? Timestamp.valueOf(cliente.getFechaInactivacion()) : null);
            stmt.setInt(11, cliente.getId());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM CLIENTE WHERE ID_Cliente=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void actualizarEstado(int id, String nuevoEstado) throws SQLException {
        String sql = "UPDATE CLIENTE SET Estado_Cliente = ?, Fecha_Actualizacion = NOW() WHERE ID_Cliente = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    public void actualizarInactivacion(int id, boolean inactivar) throws SQLException {
        String sql = inactivar ?
            "UPDATE CLIENTE SET Estado_Cliente = 'Inactivo', Fecha_Inactivacion = NOW(), Fecha_Actualizacion = NOW() WHERE ID_Cliente = ?" :
            "UPDATE CLIENTE SET Estado_Cliente = 'Activo', Fecha_Inactivacion = NULL, Fecha_Actualizacion = NOW() WHERE ID_Cliente = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Cliente mapRow(ResultSet rs) throws SQLException {
        return new Cliente(
            rs.getInt("ID_Cliente"),
            rs.getString("Nombre_Cliente"),
            rs.getString("Documento_Identificacion"),
            rs.getString("Tipo_Persona"),
            rs.getString("Telefono_Contacto"),
            rs.getString("Correo_Contacto"),
            rs.getString("Direccion"),
            rs.getString("Estado_Cliente"),
            rs.getTimestamp("Fecha_Creacion") != null ? rs.getTimestamp("Fecha_Creacion").toLocalDateTime() : null,
            rs.getTimestamp("Fecha_Actualizacion") != null ? rs.getTimestamp("Fecha_Actualizacion").toLocalDateTime() : null,
            rs.getTimestamp("Fecha_Inactivacion") != null ? rs.getTimestamp("Fecha_Inactivacion").toLocalDateTime() : null
        );
    }
}
