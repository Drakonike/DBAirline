package org.application.dbairline.model.data.tables;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Tariffa class represents a tariff for a flight route and class.
 * It contains information such as the tariff ID, flight number, route number,
 * class ID, start date, end date, and tariff value.
 */
public class Tariffa {
    private int idTariffa;
    private int numeroVolo;
    private int numeroTratta;
    private int idClasse;
    private Date dataInizio;
    private Date dataFine;
    private float tariffa;

    /**
     * Constructs a new Tariffa object with the given parameters.
     *
     * @param idTariffa    the ID of the tariff
     * @param numeroVolo   the flight number
     * @param numeroTratta the route number
     * @param idClasse     the ID of the class
     * @param dataInizio   the start date
     * @param dataFine     the end date
     * @param tariffa      the tariff value
     */
    public Tariffa(int idTariffa, int numeroVolo, int numeroTratta, int idClasse, Date dataInizio, Date dataFine,
                   float tariffa) {
        this.idTariffa = idTariffa;
        this.numeroVolo = numeroVolo;
        this.numeroTratta = numeroTratta;
        this.idClasse = idClasse;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.tariffa = tariffa;
    }

    /**
     * Maps the contents of a ResultSet to a List of Tariffa objects.
     *
     * @param resultSet the ResultSet containing the data to be mapped
     * @return a List of Tariffa objects with the mapped data, or null if an SQLException occurs
     */
    public static List<Tariffa> mapTo(ResultSet resultSet) throws SQLException {
        List<Tariffa> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(new Tariffa(
                    resultSet.getInt("IdTariffa"),
                    resultSet.getInt("NumeroDiVolo"),
                    resultSet.getInt("NumeroTratta"),
                    resultSet.getInt("IdClasse"),
                    resultSet.getDate("DataInizio"),
                    resultSet.getDate("DataFine"),
                    resultSet.getFloat("Tariffa")));
        }
        return list;
    }

    public int getIdTariffa() {
        return idTariffa;
    }

    public void setIdTariffa(int idTariffa) {
        this.idTariffa = idTariffa;
    }

    public int getNumeroVolo() {
        return numeroVolo;
    }

    public void setNumeroVolo(int numeroVolo) {
        this.numeroVolo = numeroVolo;
    }

    public int getNumeroTratta() {
        return numeroTratta;
    }

    public void setNumeroTratta(int numeroTratta) {
        this.numeroTratta = numeroTratta;
    }

    public int getIdClasse() {
        return idClasse;
    }

    public void setIdClasse(int idClasse) {
        this.idClasse = idClasse;
    }

    public Date getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
    }

    public Date getDataFine() {
        return dataFine;
    }

    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }

    public float getTariffa() {
        return tariffa;
    }

    public void setTariffa(float tariffa) {
        this.tariffa = tariffa;
    }
}