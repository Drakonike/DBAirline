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

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static org.application.dbairline.model.data.tables.Itinerario.ITINERARY_TYPES;

public class InsertItineraryWindowController implements Initializable {

    public static final int ZERO = 0;
    public static final int LIMIT = 10000;

    private WindowController parentController;

    @FXML
    private Button closeButton = new Button();
    @FXML
    private Button insertButton = new Button();
    @FXML
    private TextField flightNumberTF = new TextField();
    @FXML
    private ChoiceBox<String> itineraryTypeCB = new ChoiceBox<>();

    @FXML
    void closeWindow(MouseEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void insert(MouseEvent event) {
        int flightNumber;
        try {
            flightNumber = Integer.parseInt(flightNumberTF.getText());
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            showErrorAlert("Formato non valido", "Formato non valido per il numero di volo.\n" +
                                                 "Assicurarsi di utilizzare solo numeri interi.");
            return;
        }

        if (flightNumber < ZERO || flightNumber >= LIMIT) {
            showErrorAlert("Formato non valido", "Formato non valido per il numero di volo.\n" +
                                                 "Assicurarsi di utilizzare solo numeri di massimo QUATTRO cifre.");
            return;
        }

        if (itineraryTypeCB == null || itineraryTypeCB.getValue() == null) {
            showErrorAlert("Campi non validi", "Numero e/o tipologia di volo invalido.\n");
            return;
        }

        try (Connection conn = DAUtility.getConnection()) {
            DAUtility.executeUpdate(conn, Queries.Insertions.ITINERARI, flightNumber, itineraryTypeCB.getValue());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showErrorAlert("Errore del database", "Inserzione fallita.");
            return;
        }

        parentController.refreshTableViews();
        Stage stage = (Stage) insertButton.getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        itineraryTypeCB.setItems(FXCollections.observableList(ITINERARY_TYPES));
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
