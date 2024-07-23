package org.application.dbairline.model.data.tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents an employee. Contains information about the employee's personal details, address, contact information, role, rank, and salary.
 */
public class Dipendente {
    public static List<String> ROLES = Arrays.asList("Pilota", "Assistente di Volo");
    public static List<String> RANKS = Arrays.asList("Nessuno", "Capitano", "Primo Ufficiale");

    private int idDipendente;
    private String nome;
    private String cognome;
    private String nazionalita;
    private String indCitta;
    private String indVia;
    private int indNumero;
    private String indCap;
    private String telefono;
    private String ruolo;
    private String grado;
    private int stipendio;

    /**
     * Creates a new instance of the Dipendente class.
     *
     * @param idDipendente the ID of the dipendente
     * @param nome         the nome of the dipendente
     * @param cognome      the cognome of the dipendente
     * @param nazionalita  the nazionalita of the dipendente
     * @param indCitta     the citta of the dipendente's address
     * @param indVia       the via of the dipendente's address
     * @param indNumero    the numero of the dipendente's address
     * @param indCap       the CAP of the dipendente's address
     * @param telefono     the telefono number of the dipendente
     * @param ruolo        the ruolo of the dipendente
     * @param grado        the grado of the dipendente
     * @param stipendio    the stipendio of the dipendente
     */
    public Dipendente(int idDipendente, String nome, String cognome, String nazionalita, String indCitta, String indVia,
                      int indNumero, String indCap, String telefono, String ruolo, String grado, int stipendio) {
        this.idDipendente = idDipendente;
        this.nome = nome;
        this.cognome = cognome;
        this.nazionalita = nazionalita;
        this.indCitta = indCitta;
        this.indVia = indVia;
        this.indNumero = indNumero;
        this.indCap = indCap;
        this.telefono = telefono;
        this.ruolo = ruolo;
        this.grado = grado;
        this.stipendio = stipendio;
    }

    /**
     * Maps a ResultSet to a list of Dipendente objects.
     *
     * @param resultSet the ResultSet to map
     * @return a list of Dipendente objects
     */
    public static List<Dipendente> mapTo(ResultSet resultSet) throws SQLException {
        List<Dipendente> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(new Dipendente(
                    resultSet.getInt("IdDipendente"),
                    resultSet.getString("Nome"),
                    resultSet.getString("Cognome"),
                    resultSet.getString("Nazionalita"),
                    resultSet.getString("Ind_Citta"),
                    resultSet.getString("Ind_Via"),
                    resultSet.getInt("Ind_Numero"),
                    resultSet.getString("Ind_Cap"),
                    resultSet.getString("Telefono"),
                    resultSet.getString("Ruolo"),
                    resultSet.getString("Grado"),
                    resultSet.getInt("Stipendio")));
        }
        return list;
    }

    public int getIdDipendente() {
        return idDipendente;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getNazionalita() {
        return nazionalita;
    }

    public String getIndCitta() {
        return indCitta;
    }

    public String getIndVia() {
        return indVia;
    }

    public int getIndNumero() {
        return indNumero;
    }

    public String getIndCap() {
        return indCap;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getRuolo() {
        return ruolo;
    }

    public String getGrado() {
        return grado;
    }

    public int getStipendio() {
        return stipendio;
    }
}