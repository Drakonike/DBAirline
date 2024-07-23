package org.application.dbairline.model.data.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Programma class represents a schedule program.
 */
public class Programma {
    public static final List<String> DAYS = Arrays.asList(
            "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato", "Domenica");

    private int numeroDiVolo;
    private int numeroTratta;
    private String giorno;
    private Time oraPartenza;
    private Time tempoDIPercorrenza;
    private int idConfigurazione;


    /**
     * Creates a new instance of the Programma class with the given parameters.
     *
     * @param numeroDiVolo       The flight number.
     * @param numeroTratta       The leg number.
     * @param giorno             The day of the week for the schedule.
     * @param oraPartenza        The departure time.
     * @param tempoDIPercorrenza The travel time.
     * @param idConfigurazione   The configuration id.
     */
    public Programma(int numeroDiVolo, int numeroTratta, String giorno, Time oraPartenza, Time tempoDIPercorrenza,
                     int idConfigurazione) {
        this.numeroDiVolo = numeroDiVolo;
        this.numeroTratta = numeroTratta;
        this.giorno = giorno;
        this.oraPartenza = oraPartenza;
        this.tempoDIPercorrenza = tempoDIPercorrenza;
        this.idConfigurazione = idConfigurazione;
    }

    /**
     * Maps a ResultSet to a List of Programma objects.
     *
     * @param resultSet The ResultSet object to map.
     * @return A List of Programma objects.
     */
    public static List<Programma> mapTo(ResultSet resultSet) throws SQLException {
        List<Programma> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(new Programma(
                    resultSet.getInt("NumeroDiVolo"),
                    resultSet.getInt("NumeroTratta"),
                    resultSet.getString("Giorno"),
                    resultSet.getTime("OraPartenza"),
                    resultSet.getTime("TempoDiPercorrenza"),
                    resultSet.getInt("IdConfigurazione")));
        }
        return list;
    }

    public int getNumeroDiVolo() {
        return numeroDiVolo;
    }

    public int getNumeroTratta() {
        return numeroTratta;
    }

    public String getGiorno() {
        return giorno;
    }

    public Time getOraPartenza() {
        return oraPartenza;
    }

    public Time getTempoDIPercorrenza() {
        return tempoDIPercorrenza;
    }

    public int getIdConfigurazione() {
        return idConfigurazione;
    }
}