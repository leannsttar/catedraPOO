package com.example.catedrapoo.dao;

import com.example.catedrapoo.model.Asignacion;
import com.example.catedrapoo.model.SubTarea;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AsignacionConSubTareasDAO {
    private final Connection connection;

    public AsignacionConSubTareasDAO(Connection connection) {
        this.connection = connection;
    }

    public List<SubTarea> obtenerSubTareasPorAsignacion(int idAsignacion) throws SQLException {
        List<SubTarea> subTareas = new ArrayList<>();
        String sql = "SELECT * FROM SUBTAREA WHERE ID_Asignacion = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAsignacion);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    subTareas.add(new SubTarea(
                        rs.getInt("ID_Subtarea"),
                        rs.getString("Titulo_Subtarea"),
                        rs.getString("Descripcion_Subtarea")
                    ));
                }
            }
        }
        return subTareas;
    }
}
