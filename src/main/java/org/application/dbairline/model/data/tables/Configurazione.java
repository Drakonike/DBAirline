package org.application.dbairline.model.data.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a configuration of seats for an aircraft.
 */
public class Configurazione {
    private int idConfigurazione;
    private String nomeConfigurazione;
    private int numMaxPosti;

    /**
     * Constructor for Configurazione class.
     * Creates an instance with the specified configuration details.
     *
     * @param idConfigurazione   the ID of the configuration
     * @param nomeConfigurazione the name of the configuration
     * @param numMaxPosti        the maximum number of seats for the configuration
     */
    public Configurazione(int idConfigurazione, String nomeConfigurazione, int numMaxPosti) {
        this.idConfigurazione = idConfigurazione;
        this.nomeConfigurazione = nomeConfigurazione;
        this.numMaxPosti = numMaxPosti;

    }

    /**
     * Maps a ResultSet to a List of Configurazione objects.
     *
     * @param resultSet the ResultSet to be mapped
     * @return a List of Configurazione objects representing the data in the ResultSet
     */
    public static List<Configurazione> mapTo(ResultSet resultSet) throws SQLException {
        List<Configurazione> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(new Configurazione(
                    resultSet.getInt("IdConfigurazione"),
                    resultSet.getString("NomeConfigurazione"),
                    resultSet.getInt("NumMaxPosti")));
        }
        return list;
    }

    public int getIdConfigurazione() {
        return idConfigurazione;
    }

    public String getNomeConfigurazione() {
        return nomeConfigurazione;
    }

    public int getNumMaxPosti() {
        return numMaxPosti;
    }
}