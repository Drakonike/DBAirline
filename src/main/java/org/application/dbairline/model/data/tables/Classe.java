package org.application.dbairline.model.data.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This Class represents a Classe object.
 */
public class Classe {
    private int idClasse;
    private String nomeClasse;

    /**
     * Constructs a Classe object with the given id and name.
     *
     * @param idClasse   the id of the Classe
     * @param nomeClasse the name of the Classe
     */
    public Classe(int idClasse, String nomeClasse) {
        this.idClasse = idClasse;
        this.nomeClasse = nomeClasse;
    }

    /**
     * Maps a ResultSet to a list of Classe objects.
     *
     * @param resultSet the ResultSet to map
     * @return a list of Classe objects mapped from the ResultSet
     */
    public static List<Classe> mapTo(ResultSet resultSet) throws SQLException {
        List<Classe> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(new Classe(
                    resultSet.getInt("IdClasse"),
                    resultSet.getString("NomeClasse")));
        }
        return list;
    }

    public int getIdClasse() {
        return idClasse;
    }

    public String getNomeClasse() {
        return nomeClasse;
    }
}