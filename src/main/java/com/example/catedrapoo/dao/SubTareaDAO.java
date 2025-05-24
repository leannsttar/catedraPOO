package com.example.catedrapoo.dao;

import com.example.catedrapoo.model.SubTarea;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubTareaDAO {
    private final Connection connection;

    public SubTareaDAO(Connection connection) {
        this.connection = connection;
    }

    public void crear(SubTarea subTarea) throws SQLException {
        String sql = "INSERT INTO SUBTAREA (Titulo_Subtarea, Descripcion_Subtarea) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, subTarea.getTitulo());
            stmt.setString(2, subTarea.getDescripcion());
            stmt.executeUpdate();
        }
    }

    public void crear(SubTarea subTarea, int idAsignacion) throws SQLException {
        String sql = "INSERT INTO SUBTAREA (Titulo_SubTarea, Descripcion_SubTarea, ID_Asignacion) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, subTarea.getTitulo());
            stmt.setString(2, subTarea.getDescripcion());
            stmt.setInt(3, idAsignacion);
            stmt.executeUpdate();
        }
    }

    public SubTarea obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM SUBTAREA WHERE ID_Subtarea = ?";
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

    public List<SubTarea> listarTodos() throws SQLException {
        List<SubTarea> subTareas = new ArrayList<>();
        String sql = "SELECT * FROM SUBTAREA";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                subTareas.add(mapRow(rs));
            }
        }
        return subTareas;
    }

    public List<SubTarea> listarPorAsignacion(int idAsignacion) throws SQLException {
        List<SubTarea> subTareas = new ArrayList<>();
        String sql = "SELECT * FROM SUBTAREA WHERE ID_Asignacion = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAsignacion);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    subTareas.add(mapRow(rs));
                }
            }
        }
        return subTareas;
    }

    public void actualizar(SubTarea subTarea) throws SQLException {
        String sql = "UPDATE SUBTAREA SET Titulo_Subtarea=?, Descripcion_Subtarea=? WHERE ID_Subtarea=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, subTarea.getTitulo());
            stmt.setString(2, subTarea.getDescripcion());
            stmt.setInt(3, subTarea.getId());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM SUBTAREA WHERE ID_Subtarea=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private SubTarea mapRow(ResultSet rs) throws SQLException {
        return new SubTarea(
            rs.getInt("ID_Subtarea"),
            rs.getString("Titulo_Subtarea"),
            rs.getString("Descripcion_Subtarea")
        );
    }
}
