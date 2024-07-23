package org.application.dbairline.model.data.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Posto class represents a seat in a configuration.
 */
public class Posto {
    private int idConfigurazione;
    private String numeroPosto;
    private int idClasse;
    private final String nomeClasse;

    /**
     * Creates a new Posto object with the given parameters.
     *
     * @param idConfigurazione The ID of the configuration.
     * @param numeroPosto      The seat number.
     * @param idClasse         The ID of the class.
     * @param nomeCLasse       The name of the class.
     */
    public Posto(int idConfigurazione, String numeroPosto, int idClasse, String nomeCLasse) {
        this.idConfigurazione = idConfigurazione;
        this.numeroPosto = numeroPosto;
        this.idClasse = idClasse;
        this.nomeClasse = nomeCLasse;
    }

    /**
     * Maps the given ResultSet to a List of Posto objects.
     *
     * @param resultSet The ResultSet to map.
     * @return A List of Posto objects mapped from the ResultSet.
     */
    public static List<Posto> mapTo(ResultSet resultSet) {
        List<Posto> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                list.add(new Posto(
                        resultSet.getInt("IdConfigurazione"),
                        resultSet.getString("NumeroPosto"),
                        resultSet.getInt("IdClasse"),
                        resultSet.getString("NomeClasse")));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Posto> mapToWithoutClassName(ResultSet resultSet) throws SQLException {
        List<Posto> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(new Posto(
                    resultSet.getInt("IdConfigurazione"),
                    resultSet.getString("NumeroPosto"),
                    resultSet.getInt("IdClasse"),
                    null));
        }
        return list;
    }

    public int getIdConfigurazione() {
        return idConfigurazione;
    }

    public String getNumeroPosto() {
        return numeroPosto;
    }

    public int getIdClasse() {
        return idClasse;
    }

    public String getNomeClasse() {
        return nomeClasse;
    }
}