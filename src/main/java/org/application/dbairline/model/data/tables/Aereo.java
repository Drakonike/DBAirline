package org.application.dbairline.model.data.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Aereo class represents an aircraft.
 */
public class Aereo {
    private String codiceRegistrazione;
    private String modello;
    private String costruttore;
    private int idConfigurazione;

    /**
     * Constructs a new instance of the Aereo class with the specified parameters.
     *
     * @param codiceRegistrazione The registration code of the aircraft.
     * @param modello             The model of the aircraft.
     * @param costruttore         The manufacturer of the aircraft.
     * @param idConfigurazione    The configuration ID of the aircraft.
     */
    public Aereo(String codiceRegistrazione, String modello, String costruttore, int idConfigurazione) {
        this.codiceRegistrazione = codiceRegistrazione;
        this.modello = modello;
        this.costruttore = costruttore;
        this.idConfigurazione = idConfigurazione;
    }

    /**
     * Maps the result set to a list of Aereo objects.
     *
     * @param resultSet The result set from the database query.
     * @return A list of Aereo objects.
     * @throws SQLException If an exception occurs while accessing the result set.
     */
    public static List<Aereo> mapTo(ResultSet resultSet) throws SQLException {
        List<Aereo> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(new Aereo(
                    resultSet.getString("codiceRegistrazione"),
                    resultSet.getString("modello"),
                    resultSet.getString("costruttore"),
                    resultSet.getInt("idConfigurazione")));
        }
        return list;
    }

    public String getCodiceRegistrazione() {
        return codiceRegistrazione;
    }

    public String getModello() {
        return modello;
    }

    public String getCostruttore() {
        return costruttore;
    }

    public int getIdConfigurazione() {
        return idConfigurazione;
    }
}