package org.application.dbairline.model.data.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Class representing a flight segment.
 */
public class Tratta {
    private int numeroDiVolo;
    private int numeroTratta;
    private String partenza;
    private String destinazione;
    private int tempoDiPercorrenza;

    /**
     * Class representing a flight segment.
     */
    public Tratta(int numeroDiVolo, int numeroTratta, String partenza, String destinazione, int tempoDiPercorrenza) {
        this.numeroDiVolo = numeroDiVolo;
        this.numeroTratta = numeroTratta;
        this.partenza = partenza;
        this.destinazione = destinazione;
        this.tempoDiPercorrenza = tempoDiPercorrenza;
    }

    /**
     * Maps the ResultSet to a list of Tratta objects.
     *
     * @param resultSet the ResultSet to be mapped
     * @return a list of Tratta objects
     */
    public static List<Tratta> mapTo(ResultSet resultSet) throws SQLException {
        List<Tratta> list = new java.util.ArrayList<>();
        while (resultSet.next()) {
            list.add(new Tratta(
                    resultSet.getInt("numeroDiVolo"),
                    resultSet.getInt("numeroTratta"),
                    resultSet.getString("partenza"),
                    resultSet.getString("destinazione"),
                    resultSet.getInt("tempoDiPercorrenza")));
        }
        return list;
    }

    public int getNumeroDiVolo() {
        return numeroDiVolo;
    }

    public void setNumeroDiVolo(int numeroDiVolo) {
        this.numeroDiVolo = numeroDiVolo;
    }

    public int getNumeroTratta() {
        return numeroTratta;
    }

    public void setNumeroTratta(int numeroTratta) {
        this.numeroTratta = numeroTratta;
    }

    public String getPartenza() {
        return partenza;
    }

    public void setPartenza(String partenza) {
        this.partenza = partenza;
    }

    public String getDestinazione() {
        return destinazione;
    }

    public void setDestinazione(String destinazione) {
        this.destinazione = destinazione;
    }

    public int getTempoDiPercorrenza() {
        return tempoDiPercorrenza;
    }

    public void setTempoDiPercorrenza(int tempoDiPercorrenza) {
        this.tempoDiPercorrenza = tempoDiPercorrenza;
    }
}