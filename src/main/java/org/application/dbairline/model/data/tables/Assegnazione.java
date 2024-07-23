package org.application.dbairline.model.data.tables;

/**
 * The Assegnazione class represents an assignment between a dipendente (employee) and a volo (flight).
 * It contains the id of the dipendente and the id of the volo.
 */
public class Assegnazione {
    private int idDipendente;
    private int idVolo;

    public Assegnazione(int idDipendente, int idVolo) {
        this.idDipendente = idDipendente;
        this.idVolo = idVolo;
    }

    public int getIdDipendente() {
        return idDipendente;
    }

    public int getIdVolo() {
        return idVolo;
    }
}