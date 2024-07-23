package org.application.dbairline.model.data.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Volo represents a flight entity with various attributes such as id, departure and arrival timestamps,
 * flight cancellation status, flight number, aircraft information, and configuration.
 */
public class Volo {
    private int idVolo;
    private Timestamp dataOraPartenzaProg;
    private Timestamp dataOraArrivoProg;
    private Timestamp dataOraPartenzaEff;
    private Timestamp dataOraArrivoEff;
    private String depAirport;
    private String arrAirport;
    private boolean voloCancellato;
    private int numeroDiVolo;
    private int numeroTratta;
    private String aereo;
    private String aeroportoAlternativo;
    private int configurazione;

    /**
     * Constructs a Volo object with the given parameters.
     *
     * @param idVolo               the ID of the flight
     * @param dataOraPartenzaProg  the scheduled departure date and time
     * @param dataOraArrivoProg    the scheduled arrival date and time
     * @param dataOraPartenzaEff   the actual departure date and time
     * @param dataOraArrivoEff     the actual arrival date and time
     * @param voloCancellato       indicates if the flight was cancelled (true) or not (false)
     * @param numeroDiVolo         the flight number
     * @param numeroTratta         the route number
     * @param aereo                the airplane model
     * @param aeroportoAlternativo the alternative airport
     * @param configurazione       the configuration identifier
     */
    public Volo(int idVolo, Timestamp dataOraPartenzaProg, Timestamp dataOraArrivoProg, Timestamp dataOraPartenzaEff,
                Timestamp dataOraArrivoEff, boolean voloCancellato, int numeroDiVolo, int numeroTratta, String aereo,
                String aeroportoAlternativo, int configurazione) {
        this.idVolo = idVolo;
        this.dataOraPartenzaProg = dataOraPartenzaProg;
        this.dataOraArrivoProg = dataOraArrivoProg;
        this.dataOraPartenzaEff = dataOraPartenzaEff;
        this.dataOraArrivoEff = dataOraArrivoEff;
        this.voloCancellato = voloCancellato;
        this.numeroDiVolo = numeroDiVolo;
        this.numeroTratta = numeroTratta;
        this.aereo = aereo;
        this.aeroportoAlternativo = aeroportoAlternativo;
        this.configurazione = configurazione;
    }

    /**
     * Constructs a Volo object with the given parameters.
     *
     * @param idVolo               the ID of the flight
     * @param dataOraPartenzaProg  the scheduled departure date and time
     * @param dataOraArrivoProg    the scheduled arrival date and time
     * @param dataOraPartenzaEff   the actual departure date and time
     * @param dataOraArrivoEff     the actual arrival date and time
     * @param depAirport           the departure airport
     * @param arrAirport           the arrival airport
     * @param voloCancellato       indicates if the flight was cancelled (true) or not (false)
     * @param numeroDiVolo         the flight number
     * @param numeroTratta         the route number
     * @param aereo                the airplane model
     * @param aeroportoAlternativo the alternative airport
     * @param configurazione       the configuration identifier
     */
    public Volo(int idVolo, Timestamp dataOraPartenzaProg, Timestamp dataOraArrivoProg, Timestamp dataOraPartenzaEff,
                Timestamp dataOraArrivoEff, String depAirport, String arrAirport, boolean voloCancellato,
                int numeroDiVolo, int numeroTratta, String aereo, String aeroportoAlternativo, int configurazione) {
        this.idVolo = idVolo;
        this.dataOraPartenzaProg = dataOraPartenzaProg;
        this.dataOraArrivoProg = dataOraArrivoProg;
        this.dataOraPartenzaEff = dataOraPartenzaEff;
        this.dataOraArrivoEff = dataOraArrivoEff;
        this.depAirport = depAirport;
        this.arrAirport = arrAirport;
        this.voloCancellato = voloCancellato;
        this.numeroDiVolo = numeroDiVolo;
        this.numeroTratta = numeroTratta;
        this.aereo = aereo;
        this.aeroportoAlternativo = aeroportoAlternativo;
        this.configurazione = configurazione;
    }

    /**
     * Maps the ResultSet to a List of Volo objects.
     *
     * @param rs the ResultSet to map
     * @return a List of Volo objects mapped from the ResultSet
     */
    public static List<Volo> mapTo(ResultSet rs) throws SQLException {
        List<Volo> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new Volo(
                    rs.getInt("IdVolo"),
                    rs.getTimestamp("DataOraPartenzaProg"),
                    rs.getTimestamp("DataOraArrivoProg"),
                    rs.getTimestamp("DataOraPartenzaEff"),
                    rs.getTimestamp("DataOraArrivoEff"),
                    rs.getString("Partenza"),
                    rs.getString("Destinazione"),
                    rs.getBoolean("VoloCancellato"),
                    rs.getInt("NumeroDiVolo"),
                    rs.getInt("NumeroTratta"),
                    rs.getString("Aereo"),
                    rs.getString("AeroportoAlternativo"),
                    rs.getInt("IdConfigurazione")));
        }
        return list;
    }

    public static List<Volo> mapToNotExtended(ResultSet rs) throws SQLException {
        List<Volo> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new Volo(
                    rs.getInt("IdVolo"),
                    rs.getTimestamp("DataOraPartenzaProg"),
                    rs.getTimestamp("DataOraArrivoProg"),
                    rs.getTimestamp("DataOraPartenzaEff"),
                    rs.getTimestamp("DataOraArrivoEff"),
                    null,
                    null,
                    rs.getBoolean("VoloCancellato"),
                    rs.getInt("NumeroDiVolo"),
                    rs.getInt("NumeroTratta"),
                    rs.getString("Aereo"),
                    rs.getString("AeroportoAlternativo"),
                    rs.getInt("IdConfigurazione")));
        }
        return list;
    }

    public int getIdVolo() {
        return idVolo;
    }

    public Timestamp getDataOraPartenzaProg() {
        return dataOraPartenzaProg;
    }

    public Timestamp getDataOraArrivoProg() {
        return dataOraArrivoProg;
    }

    public Timestamp getDataOraPartenzaEff() {
        return dataOraPartenzaEff;
    }

    public Timestamp getDataOraArrivoEff() {
        return dataOraArrivoEff;
    }

    public String getDepAirport() {
        return depAirport;
    }

    public String getArrAirport() {
        return arrAirport;
    }

    public boolean isVoloCancellato() {
        return voloCancellato;
    }

    public int getNumeroDiVolo() {
        return numeroDiVolo;
    }

    public int getNumeroTratta() {
        return numeroTratta;
    }

    public String getAereo() {
        return aereo;
    }

    public String getAeroportoAlternativo() {
        return aeroportoAlternativo;
    }

    public int getConfigurazione() {
        return configurazione;
    }
}