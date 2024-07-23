package org.application.dbairline.model.data.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Rifornimento class represents a refueling operation for an aircraft.
 * It contains information such as the flight ID, fuel type, fuel quantity, and cost.
 */
public class Rifornimento {
    public static List<String> FUEL_TYPES = Arrays.asList("Jet A-1", "Jet A", "AvGas", "Biocarburante");

    private int idVolo;
    private String tipoCarburante;
    private float quantita;
    private float costo;

    /**
     * Creates a new instance of the {@code Rifornimento} class.
     *
     * @param idVolo         the flight ID.
     * @param tipoCarburante the fuel type. Must be one of "Jet A-1", "Jet A", "AvGas", or "Biocarburante".
     * @param quantita       the fuel quantity.
     * @param costo          the fuel cost.
     */
    public Rifornimento(int idVolo, String tipoCarburante, float quantita, float costo) {
        this.idVolo = idVolo;
        this.tipoCarburante = tipoCarburante;
        this.quantita = quantita;
        this.costo = costo;
    }

    /**
     * Maps a ResultSet to a List of Rifornimento objects.
     *
     * @param resultSet the ResultSet to map.
     * @return a List of Rifornimento objects mapped from the ResultSet.
     */
    public static List<Rifornimento> mapTo(ResultSet resultSet) throws SQLException {
        List<Rifornimento> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(new Rifornimento(
                    resultSet.getInt("IdVolo"),
                    resultSet.getString("TipoCarburante"),
                    resultSet.getFloat("Quantita"),
                    resultSet.getFloat("Costo")));
        }
        return list;
    }

    public int getIdVolo() {
        return idVolo;
    }

    public void setIdVolo(int idVolo) {
        this.idVolo = idVolo;
    }

    public String getTipoCarburante() {
        return tipoCarburante;
    }

    public void setTipoCarburante(String tipoCarburante) {
        this.tipoCarburante = tipoCarburante;
    }

    public float getQuantita() {
        return quantita;
    }

    public void setQuantita(float quantita) {
        this.quantita = quantita;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }
}