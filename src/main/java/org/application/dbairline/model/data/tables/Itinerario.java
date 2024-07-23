package org.application.dbairline.model.data.tables;

import java.security.InvalidParameterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * The Itinerario class represents an itinerary.
 */
public class Itinerario {
    public static final List<String> ITINERARY_TYPES = Arrays.asList("Periodico", "Charter", "Rilocazione");

    private int numeroDiVolo;
    private String tipoVolo;

    /**
     * Creates a new Itinerario object with the specified flight number and flight type.
     *
     * @param numeroDiVolo the flight number for the itinerary
     * @param tipoVolo     the flight type for the itinerary
     */
    public Itinerario(int numeroDiVolo, String tipoVolo) {
        this.numeroDiVolo = numeroDiVolo;
        this.tipoVolo = tipoVolo;
    }

    /**
     * Maps the ResultSet to a list of Itinerario objects.
     *
     * @param resultSet the ResultSet containing the data to be mapped
     * @return a list of Itinerario objects
     */
    public static List<Itinerario> mapTo(ResultSet resultSet) throws SQLException {
        List<Itinerario> list = new java.util.ArrayList<>();
        while (resultSet.next()) {
            list.add(new Itinerario(
                    resultSet.getInt("NumeroDiVolo"),
                    resultSet.getString("TipoVolo")));
        }
        return list;
    }

    public int getNumeroDiVolo() {
        return numeroDiVolo;
    }

    public void setNumeroDiVolo(int numeroDiVolo) {
        this.numeroDiVolo = numeroDiVolo;
    }

    public String getTipoVolo() {
        return tipoVolo;
    }

    public void setTipoVolo(String tipoVolo) {
        this.tipoVolo = tipoVolo;
    }
}