package org.application.dbairline.model.data.tables;

/**
 * The Equipaggio class represents an assignment between a dipendente (employee) and a volo (flight).
 * It contains the id of the dipendente and the id of the volo.
 */
public class Equipaggio {
    private int idDipendente;
    private int idVolo;

    public Equipaggio(int idDipendente, int idVolo) {
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