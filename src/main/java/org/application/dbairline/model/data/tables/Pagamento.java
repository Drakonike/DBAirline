package org.application.dbairline.model.data.tables;

public class Pagamento {
    private int idAcquisto;
    private int idTariffa;

    public Pagamento(int idAcquisto, int idTariffa) {
        this.idAcquisto = idAcquisto;
        this.idTariffa = idTariffa;
    }

    public int getIdAcquisto() {
        return idAcquisto;
    }

    public int getIdTariffa() {
        return idTariffa;
    }
}