package org.application.dbairline.model.data.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an airport.
 */
public class Aeroporto {
    private String icao;
    private String iata;
    private String nomeCommerciale;
    private String citta;
    private String nazione;
    private String gestore;
    private String contattoGestore;
    private String telefono;
    private String fax;

    /**
     * Represents an airport.
     *
     * @param icao            the ICAO code of the airport
     * @param iata            the IATA code of the airport
     * @param nomeCommerciale the commercial name of the airport
     * @param citta           the city where the airport is located
     * @param nazione         the country where the airport is located
     * @param gestore         the operator of the airport
     * @param contattoGestore the contact information of the operator of the airport
     * @param telefono        the telephone number of the airport
     * @param fax             the fax number of the airport
     */
    public Aeroporto(String icao, String iata, String nomeCommerciale, String citta, String nazione, String gestore,
                     String contattoGestore, String telefono, String fax) {
        this.icao = icao;
        this.iata = iata;
        this.nomeCommerciale = nomeCommerciale;
        this.citta = citta;
        this.nazione = nazione;
        this.gestore = gestore;
        this.contattoGestore = contattoGestore;
        this.telefono = telefono;
        this.fax = fax;
    }

    /**
     * Maps the ResultSet to a list of Aeroporto objects.
     *
     * @param resultSet the ResultSet containing the data to be mapped
     * @return a list of Aeroporto objects
     */
    public static List<Aeroporto> mapTo(ResultSet resultSet) throws SQLException {
        List<Aeroporto> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(new Aeroporto(
                    resultSet.getString("ICAO"),
                    resultSet.getString("IATA"),
                    resultSet.getString("NomeCommerciale"),
                    resultSet.getString("Citta"),
                    resultSet.getString("Nazione"),
                    resultSet.getString("Gestore"),
                    resultSet.getString("ContattoGestore"),
                    resultSet.getString("Telefono"),
                    resultSet.getString("Fax")));
        }
        return list;
    }

    public String getIcao() {
        return icao;
    }

    public String getIata() {
        return iata;
    }

    public String getNomeCommerciale() {
        return nomeCommerciale;
    }

    public String getCitta() {
        return citta;
    }

    public String getNazione() {
        return nazione;
    }

    public String getGestore() {
        return gestore;
    }

    public String getContattoGestore() {
        return contattoGestore;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getFax() {
        return fax;
    }
}