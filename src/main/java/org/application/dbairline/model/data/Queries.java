package org.application.dbairline.model.data;

public class Queries {
    public static class Insertions {
        public static final String ACQUISTI =
                """
                        INSERT INTO ACQUISTI (DataAcquisto, Acq_Nome_Completo, Acq_Email, Acq_Telefono, Importo)
                        VALUES (?, ?, ?, ?, ?)
                        """;

        public static final String AEREI =
                """
                        INSERT INTO AEREI (CodiceRegistrazione, Modello, Costruttore, idConfigurazione) 
                        VALUES (?, ?, ?, ?)
                        """;

        public static final String AEROPORTI =
                """
                        INSERT INTO AEROPORTI (ICAO, IATA, NomeCommerciale, Citta, Nazione, Gestore, ContattoGestore, Telefono, Fax) 
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                        """;

        public static final String EQUIPAGGI =
                """
                        INSERT INTO EQUIPAGGI (IdDipendente, IdVolo) 
                        VALUES (?, ?)
                        """;

        public static final String BIGLIETTI =
                """
                        INSERT INTO BIGLIETTI (NomePasseggero, IdAcquisto, Prezzo, IdVolo, IdConfigurazione, NumeroPosto) 
                        VALUES (?, ?, ?, ?, ?, ?)
                        """;

        public static final String CLASSI =
                """
                        INSERT INTO CLASSI (NomeClasse) 
                        VALUES (?)
                        """;

        public static final String CONFIGURAZIONI =
                """
                        INSERT INTO CONFIGURAZIONI (NomeConfigurazione, NumMaxPosti) 
                        VALUES (?, ?)
                        """;

        public static final String DIPENDENTI =
                """
                        INSERT INTO DIPENDENTI (Nome, Cognome, Nazionalita, Ind_Citta, Ind_Via, Ind_Numero, Ind_Cap, Telefono, Ruolo, Grado, Stipendio) 
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                        """;

        public static final String ITINERARI =
                """
                        INSERT INTO ITINERARI (NumeroDiVolo, TipoVolo) 
                        VALUES (?, ?)
                        """;

        public static final String POSTI =
                """
                        INSERT INTO POSTI (IdConfigurazione, NumeroPosto, IdClasse) 
                        VALUES (?, ?, ?)                
                        """;

        public static final String PROGRAMMI =
                """
                        INSERT INTO PROGRAMMI (NumeroDiVolo, NumeroTratta, Giorno, OraPartenza, IdConfigurazione) 
                        VALUES (?, ?, ?, ?, ?)                
                        """;

        public static final String RIFORNIMENTI =
                """
                        INSERT INTO RIFORNIMENTI (IdVolo, TipoCarburante, Quantita, Costo) 
                        VALUES (?, ?, ?, ?)
                        """;

        public static final String TARIFFE_CON_DATAFINE =
                """
                        INSERT INTO TARIFFE (NumeroDiVolo, NumeroTratta, IdClasse, DataInizio, DataFine, Tariffa) 
                        VALUES (?, ?, ?, ?, ?, ?)
                        """;

        public static final String TARIFFE =
                """
                        INSERT INTO TARIFFE (NumeroDiVolo, NumeroTratta, IdClasse, DataInizio, Tariffa) 
                        VALUES (?, ?, ?, ?, ?)
                        """;

        public static final String TRATTE =
                """
                        INSERT INTO TRATTE (NumeroDiVolo, NumeroTratta, Partenza, Destinazione, TempoDiPercorrenza) 
                        VALUES (?, ?, ?, ?)
                        """;

        public static final String VOLI =
                """
                        INSERT INTO VOLI (DataOraPartenzaProg, DataOraArrivoProg, VoloCancellato, NumeroDiVolo, NumeroTratta, Aereo, IdConfigurazione) 
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                        """;
        public static final String VOLI_SENZA_AEREO =
                """
                        INSERT INTO VOLI (DataOraPartenzaProg, DataOraArrivoProg, VoloCancellato, NumeroDiVolo, NumeroTratta, IdConfigurazione) 
                        VALUES (?, ?, ?, ?, ?, ?)
                        """;
    }

    public static class SelectAll {
        public static final String ACQUISTI =
                """
                        SELECT * FROM ACQUISTI
                        """;

        public static final String AEREI =
                """
                        SELECT * FROM AEREI
                        """;

        public static final String AEROPORTI =
                """
                        SELECT * FROM AEROPORTI
                        """;

        public static final String EQUIPAGGI =
                """
                        SELECT * FROM EQUIPAGGI
                        """;

        public static final String BIGLIETTI =
                """
                        SELECT * FROM BIGLIETTI
                        """;

        public static final String CLASSI =
                """
                        SELECT * FROM CLASSI
                        """;

        public static final String CONFIGURAZIONI =
                """
                        SELECT * FROM CONFIGURAZIONI
                        """;

        public static final String DIPENDENTI =
                """
                        SELECT * FROM DIPENDENTI
                        """;

        public static final String ITINERARI =
                """
                        SELECT * FROM ITINERARI
                        """;

        public static final String POSTI =
                """
                        SELECT * FROM POSTI
                        """;

        public static final String PROGRAMMI =
                """
                        SELECT * FROM PROGRAMMI
                        """;

        public static final String RIFORNIMENTI =
                """
                        SELECT * FROM RIFORNIMENTI
                        """;

        public static final String TARIFFE =
                """
                        SELECT * FROM TARIFFE
                        """;

        public static final String TRATTE =
                """
                        SELECT * FROM TRATTE
                        """;

        public static final String VOLI =
                """
                        SELECT * FROM VOLI
                        """;
    }
}
