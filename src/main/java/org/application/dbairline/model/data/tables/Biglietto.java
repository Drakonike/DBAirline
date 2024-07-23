package org.application.dbairline.model.data.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Biglietto class represents a ticket object with various properties such as ticket id, flight number, passenger name,
 * purchase id, price, flight id, configuration id, and seat number.
 */
public class Biglietto {
    private int idBiglietto;
    private int numeroDiVolo;
    private String nomePasseggero;
    private int idAcquisto;
    private float prezzo;
    private int idVolo;
    private int idConfigurazione;
    private String numeroPosto;
    private String nomeClasse;

    /**
     * The Biglietto class represents a ticket object with various properties such as ticket id, flight number, passenger name,
     * purchase id, price, flight id, configuration id, and seat number.
     */
    public Biglietto(int idBiglietto, int numeroDiVolo, String nomePasseggero, int idAcquisto, float prezzo,
                     int idVolo, int idConfigurazione, String numeroPosto) {
        this.idBiglietto = idBiglietto;
        this.numeroDiVolo = numeroDiVolo;
        this.nomePasseggero = nomePasseggero;
        this.idAcquisto = idAcquisto;
        this.prezzo = prezzo;
        this.idVolo = idVolo;
        this.idConfigurazione = idConfigurazione;
        this.numeroPosto = numeroPosto;
        this.nomeClasse = null;
    }

    public Biglietto(int idBiglietto, int numeroDiVolo, String nomePasseggero, int idAcquisto, float prezzo,
                     int idVolo, int idConfigurazione, String numeroPosto, String nomeCLasse) {
        this.idBiglietto = idBiglietto;
        this.numeroDiVolo = numeroDiVolo;
        this.nomePasseggero = nomePasseggero;
        this.idAcquisto = idAcquisto;
        this.prezzo = prezzo;
        this.idVolo = idVolo;
        this.idConfigurazione = idConfigurazione;
        this.numeroPosto = numeroPosto;
        this.nomeClasse = nomeCLasse;
    }

    /**
     * Maps a ResultSet to a list of Biglietto objects.
     *
     * @param resultSet the ResultSet object containing the data to be mapped
     * @return a list of Biglietto objects
     */
    public static List<Biglietto> mapTo(ResultSet resultSet) {
        List<Biglietto> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                list.add(new Biglietto(
                        resultSet.getInt("IdBiglietto"),
                        resultSet.getInt("NumeroDiVolo"),
                        resultSet.getString("NomePasseggero"),
                        resultSet.getInt("IdAcquisto"),
                        resultSet.getFloat("Prezzo"),
                        resultSet.getInt("IdVolo"),
                        resultSet.getInt("IdConfigurazione"),
                        resultSet.getString("NumeroPosto")));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Biglietto> mapToWithFlightClassName(ResultSet resultSet) throws SQLException {
        List<Biglietto> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(new Biglietto(
                    resultSet.getInt("IdBiglietto"),
                    resultSet.getInt("NumeroDiVolo"),
                    resultSet.getString("NomePasseggero"),
                    resultSet.getInt("IdAcquisto"),
                    resultSet.getFloat("Prezzo"),
                    resultSet.getInt("IdVolo"),
                    resultSet.getInt("IdConfigurazione"),
                    resultSet.getString("NumeroPosto"),
                    resultSet.getString("NomeClasse")));
        }
        return list;
    }

    public int getIdBiglietto() {
        return idBiglietto;
    }

    public int getNumeroDiVolo() {
        return numeroDiVolo;
    }

    public String getNomePasseggero() {
        return nomePasseggero;
    }

    public int getIdAcquisto() {
        return idAcquisto;
    }

    public float getPrezzo() {
        return prezzo;
    }

    public int getIdVolo() {
        return idVolo;
    }

    public int getIdConfigurazione() {
        return idConfigurazione;
    }

    public String getNumeroPosto() {
        return numeroPosto;
    }

    public String getNomeClasse() {
        return nomeClasse;
    }
}