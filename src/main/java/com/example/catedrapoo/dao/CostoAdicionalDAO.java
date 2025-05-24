package com.example.catedrapoo.dao;

import com.example.catedrapoo.model.CostoAdicional;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CostoAdicionalDAO {
    private final Connection connection;

    public CostoAdicionalDAO(Connection connection) {
        this.connection = connection;
    }

    public void crear(CostoAdicional costo) throws SQLException {
        String sql = "INSERT INTO COSTO_ADICIONAL (ID_Cotizacion, Descripcion, Monto) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, costo.getIdCotizacion());
            stmt.setString(2, costo.getDescripcion());
            stmt.setDouble(3, costo.getMonto());
            stmt.executeUpdate();
        }
    }

    public List<CostoAdicional> listarPorCotizacion(int idCotizacion) throws SQLException {
        List<CostoAdicional> lista = new ArrayList<>();
        String sql = "SELECT * FROM COSTO_ADICIONAL WHERE ID_Cotizacion = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCotizacion);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new CostoAdicional(
                        rs.getInt("ID_Costo_Adicional"),
                        rs.getInt("ID_Cotizacion"),
                        rs.getString("Descripcion"),
                        rs.getDouble("Monto")
                    ));
                }
            }
        }
        return lista;
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM COSTO_ADICIONAL WHERE ID_Costo_Adicional = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
