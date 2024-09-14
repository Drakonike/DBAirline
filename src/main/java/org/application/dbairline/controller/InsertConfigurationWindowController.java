package org.application.dbairline.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.application.dbairline.model.data.DAUtility;
import org.application.dbairline.model.data.Queries;
import org.application.dbairline.model.data.tables.Classe;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class InsertConfigurationWindowController implements Initializable {
    private final List<String> SEAT_LETTERS = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");

    private WindowController parentController;
    private List<Classe> flightClasses;
    private List<FlightClassConfig> savedFlightClassConfigurations;

    public class FlightClassConfig {
        private int numConfigClass;
        private int idFlightClass;
        private String nameFlightClass;
        private int numCols;
        private int numRows;
        private int numOfSeats;

        public FlightClassConfig(int numConfigClass, int idFlightClass, String nameFlightClass, int numCols, int numRows) {
            this.numConfigClass = numConfigClass;
            this.idFlightClass = idFlightClass;
            this.nameFlightClass = nameFlightClass;
            this.numCols = numCols;
            this.numRows = numRows;
            this.numOfSeats = numCols * numRows;
        }

        public int getNumConfigClass() {
            return numConfigClass;
        }

        public int getIdFlightClass() {
            return idFlightClass;
        }

        public String getNameFlightClass() {
            return nameFlightClass;
        }

        public int getNumCols() {
            return numCols;
        }

        public int getNumRows() {
            return numRows;
        }

        public int getNumOfSeats() {
            return numOfSeats;
        }
    }

    @FXML
    private TableView<FlightClassConfig> flightClassConfigTable;
    @FXML
    private TableColumn<FlightClassConfig, Integer> numConfigClassCol;
    @FXML
    private TableColumn<FlightClassConfig, String> flightClassNameCol;
    @FXML
    private TableColumn<FlightClassConfig, Integer> flightClassSeatsCol;
    @FXML
    private Button closeButton;
    @FXML
    private TextField columnTF;
    @FXML
    private TextField rowTF;
    @FXML
    private TextField configNameTF;
    @FXML
    private ChoiceBox<String> flightClassesCB;
    @FXML
    private Button insertButton;


    @FXML
    void CloseWindow(MouseEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void insert(MouseEvent event) {
        String configName = configNameTF.getText();
        if (configName == null || configName.isEmpty()) {
            showErrorAlert("Errore di input",
                    "Il nome della configurazione deve essere compilato.");
            throw new RuntimeException("Nome Configurazione è invalido.");
        }

        if (savedFlightClassConfigurations.isEmpty()) {
            showErrorAlert("Errore di input",
                    "Inserire almeno una configurazione di classe di volo.");
            throw new RuntimeException("");
        }

        int totalSeats = savedFlightClassConfigurations.stream()
                .mapToInt(FlightClassConfig::getNumOfSeats).sum();

        Connection conn = null;
        try {
            conn = DAUtility.getConnection(false, Connection.TRANSACTION_READ_UNCOMMITTED);

            ResultSet res = DAUtility.executeUpdateWithGeneratedKeys(conn, Queries.Insertions.CONFIGURAZIONI, configName, totalSeats);

            int configID = -1;
            if (res.next()) {
                configID = res.getInt(1);
            } else{
                showErrorAlert("Errore del database", "Inserzione della configurazione fallita.");
                throw new Exception();
            }
            res.close();

            int seatIndex = 1;
            for (FlightClassConfig flightClassConfiguration : savedFlightClassConfigurations) {
                if (flightClasses.stream()
                        .map(Classe::getIdClasse)
                        .toList()
                        .contains(flightClassConfiguration.idFlightClass)) {
                    for (int c = 0; c < flightClassConfiguration.getNumCols() && seatIndex < SEAT_LETTERS.size(); c++) {
                        for (int r = 0; r < flightClassConfiguration.getNumRows(); r++) {
                            try {
                                DAUtility.executeUpdate(conn, Queries.Insertions.POSTI,
                                        configID,
                                        r + SEAT_LETTERS.get(seatIndex),
                                        flightClassConfiguration.getIdFlightClass());
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        seatIndex++;
                    }
                } else {
                    showErrorAlert("Errore di input",
                            "La classe di volo selezionata non esiste.");
                    throw new RuntimeException("Invalid flight class ID");
                }
            }

            conn.commit();
            conn.close();
            parentController.refreshTableViews();
            Stage stage = (Stage) insertButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    conn.close();
                } catch (SQLException se) {
                    System.out.println("Transaction rollback failed");
                    System.out.println(se.getMessage());
                }
            }
            showErrorAlert("Errore del database", "Inserzione fallita.");
        }
    }

    @FXML
    void InsertFlightClassInConfiguration(MouseEvent event) {
        int columns = 0;
        int rows = 0;
        try {
            columns = Integer.parseInt(columnTF.getText());
            if (columns >= SEAT_LETTERS.size()) {
                showErrorAlert("Errore di input", "Il numero massimo di colonne è 26.");
                return;
            }
            rows = Integer.parseInt(rowTF.getText());
        } catch (NumberFormatException e) {
            showErrorAlert("Errore di input", "I campi colonna e righe devono contenere solo un numero.");
        }
        String flightClassName = flightClassesCB.getSelectionModel().getSelectedItem();

        if (columns == 0 || rows == 0 || flightClassName == null) {
            showErrorAlert("Errore di input", "Riempire tutti i campi con valori validi.");

        } else {
            int flightCLassID = flightClasses.stream()
                    .filter(c -> c.getNomeClasse().equals(flightClassName))
                    .map(Classe::getIdClasse)
                    .toList()
                    .getFirst();
            savedFlightClassConfigurations.add(new FlightClassConfig(
                    savedFlightClassConfigurations.size() + 1,
                    flightCLassID,
                    flightClassName,
                    columns,
                    rows));

            flightClassConfigTable.setItems(FXCollections.observableList(savedFlightClassConfigurations));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            flightClassNameCol.setCellValueFactory(new PropertyValueFactory<>("nameFlightClass"));
            flightClassSeatsCol.setCellValueFactory(new PropertyValueFactory<>("numOfSeats"));

            Connection conn = DAUtility.getConnection();
            this.flightClasses = Classe.mapTo(DAUtility.executeQuery(conn, Queries.SelectAll.CLASSI));
            conn.close();

            flightClassesCB.setItems(FXCollections.observableList(
                    flightClasses.stream().map(Classe::getNomeClasse).toList()));
            savedFlightClassConfigurations = new ArrayList<>();

        } catch (Exception e) {
            showErrorAlert("Errore di caricamento",
                    "Errore durante l'inizializzazione della finestra.");
            throw new RuntimeException(e);
        }
    }

    public int prepare(WindowController Controller) {
        if (Controller != null) {
            this.parentController = Controller;
            return 0;
        } else {
            return 1;
        }
    }

    private void showErrorAlert(String header, String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(header);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}

