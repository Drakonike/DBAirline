package org.application.dbairline.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.application.dbairline.model.data.Queries;
import org.application.dbairline.model.data.DAUtility;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static org.application.dbairline.model.data.tables.Rifornimento.FUEL_TYPES;

public class InsertRefuelWindowController implements Initializable {

    @FXML
    private Button closeButton;

    @FXML
    private TextField costTF;

    @FXML
    private ChoiceBox<String> fuelTypeCB;

    @FXML
    private Button insertButton;

    @FXML
    private Label idFlightLabel;

    @FXML
    private TextField quantityTF;
    private WindowController parentController;
    private int flightId;

    @FXML
    void exitWindow(MouseEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void insert(MouseEvent event) {
        try {
            String chosenFuelType = fuelTypeCB.getSelectionModel().getSelectedItem();
            Float quantity = Float.parseFloat(quantityTF.getText());
            Float cost = Float.parseFloat(costTF.getText());

            if (chosenFuelType != null) {
                try {
                    Connection conn = DAUtility.getConnection();
                    int res = DAUtility.executeUpdate(conn, Queries.Insertions.RIFORNIMENTI,
                            flightId,
                            chosenFuelType,
                            quantity,
                            cost);
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
            } else {
                showErrorAlert("Errore di input", "Si prega di scegliere il tipo di carburante.");
            }
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            showErrorAlert("Formato non valido", "Formato non valido per il numero di volo.");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fuelTypeCB.setItems(FXCollections.observableList(FUEL_TYPES));

    }

    public int prepare(WindowController Controller, Integer flightId) {
        if (Controller != null && flightId != null) {
            this.parentController = Controller;
            this.flightId = flightId;
            idFlightLabel.setText(String.valueOf(flightId));
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
