package org.application.dbairline.model.data.tables;

import java.security.InvalidParameterException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Acquisto object which represents a purchase.
 */
public class Acquisto {
    private int idAcquisto;
    private Date dataAcquisto;
    private String acqNomeCompleto;
    private String acqEmail;
    private String acqTelefono;
    private float importo;

    /**
     * Creates a new Acquisto object representing a purchase.
     *
     * @param idAcquisto      the ID of the purchase
     * @param dataAcquisto    the date of the purchase
     * @param acqNomeCompleto the full name of the purchaser
     * @param acqEmail        the email address of the purchaser
     * @param acqTelefono     the phone number of the purchaser
     * @param importo         the amount of the purchase
     */
    public Acquisto(int idAcquisto, Date dataAcquisto, String acqNomeCompleto,
                    String acqEmail, String acqTelefono, float importo) {
        this.idAcquisto = idAcquisto;
        this.dataAcquisto = dataAcquisto;
        this.acqNomeCompleto = acqNomeCompleto;
        this.acqEmail = acqEmail;
        this.acqTelefono = acqTelefono;
        this.importo = importo;
    }

    /**
     * Maps the data from a ResultSet object to a list of Acquisto objects.
     *
     * @param resultSet the ResultSet object containing the data to map
     * @return a list of Acquisto objects mapped from the ResultSet, or null if an exception occurs
     */
    public static List<Acquisto> mapTo(ResultSet resultSet) throws SQLException {
        List<Acquisto> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(new Acquisto(
                    resultSet.getInt("IdAcquisto"),
                    resultSet.getDate("DataAcquisto"),
                    resultSet.getString("Acq_Nome_Completo"),
                    resultSet.getString("Acq_Email"),
                    resultSet.getString("Acq_Telefono"),
                    resultSet.getFloat("Importo")));
        }
        return list;
    }

    public int getIdAcquisto() {
        return idAcquisto;
    }

    public Date getDataAcquisto() {
        return dataAcquisto;
    }

    public String getAcqNomeCompleto() {
        return acqNomeCompleto;
    }

    public String getAcqEmail() {
        return acqEmail;
    }

    public String getAcqTelefono() {
        return acqTelefono;
    }

    public float getImporto() {
        return importo;
    }
}