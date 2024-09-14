package org.application.dbairline.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.application.dbairline.model.data.DAUtility;
import org.application.dbairline.model.data.Queries;
import org.application.dbairline.model.data.tables.Configurazione;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class InsertAircraftWindowController implements Initializable {

    private WindowController parentController;
    private List<Configurazione> seatConfigurationsCache;

    @FXML
    private TextField aircraftBuilderTextField;

    @FXML
    private TextField aircraftTypeTextField;

    @FXML
    private Button closeButton;

    @FXML
    private Button insertButton;

    @FXML
    private TextField registrationCodeTextField;

    @FXML
    private ChoiceBox<String> seatConfigChoiceBox;

    @FXML
    void closeWindow(MouseEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void insert(MouseEvent event) {
        String selectedRegistrationCode = registrationCodeTextField.getText();
        String selectedAircraftType = aircraftTypeTextField.getText();
        String SelectedAircraftBuilder = aircraftBuilderTextField.getText();
        String selectedSeatConfig = seatConfigChoiceBox.getSelectionModel().getSelectedItem();

        if (selectedRegistrationCode == null || selectedRegistrationCode.isEmpty() ||
            selectedAircraftType == null || selectedAircraftType.isEmpty() ||
            SelectedAircraftBuilder == null || SelectedAircraftBuilder.isEmpty() ||
            selectedSeatConfig == null || selectedSeatConfig.isEmpty()) {

            showErrorAlert("Errore di input",
                    "Si prega di compilare tutti i campi prima di procedere.");
            return;
        }

        int seatConfigId = seatConfigurationsCache.stream()
                .filter(configuration -> configuration.getNomeConfigurazione().equals(selectedSeatConfig))
                .findFirst()
                .map(Configurazione::getIdConfigurazione)
                .orElse(-1);

        if (seatConfigId == -1) {
            showErrorAlert("Errore di input",
                    "Nessuna configurazione del posto corrispondente trovata.");
            return;
        }

        try {
            Connection conn = DAUtility.getConnection();
            int res = DAUtility.executeUpdate(conn, Queries.Insertions.AEREI,
                    selectedRegistrationCode,
                    selectedAircraftType,
                    SelectedAircraftBuilder,
                    seatConfigId);
            conn.close();

            if (res == 0) {
                showErrorAlert("Errore del database", "Inserzione fallita.");
                return;
            }

            parentController.refreshTableViews();
            Stage stage = (Stage) insertButton.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showErrorAlert("Errore del database", "Inserzione fallita.");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Connection conn = DAUtility.getConnection();
            this.seatConfigurationsCache = Configurazione.mapTo(
                    DAUtility.executeQuery(conn, Queries.SelectAll.CONFIGURAZIONI));
            conn.close();

            seatConfigChoiceBox.setItems(FXCollections.observableList(
                    seatConfigurationsCache.stream()
                            .map(Configurazione::getNomeConfigurazione)
                            .toList()));

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
