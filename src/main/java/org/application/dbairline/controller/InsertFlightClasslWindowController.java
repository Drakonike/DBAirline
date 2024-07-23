package org.application.dbairline.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.application.dbairline.model.data.DAUtility;
import org.application.dbairline.model.data.Queries;

import java.sql.Connection;
import java.sql.SQLException;

public class InsertFlightClasslWindowController {

    private MainWindowController parentController;

    @FXML
    private Button closeButton;

    @FXML
    private TextField flightClassNameTextField;

    @FXML
    private Button insertButton;

    @FXML
    void closeWindow(MouseEvent event) {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
    }

    @FXML
    void insert(MouseEvent event) {
        try {
            Connection conn = DAUtility.getConnection();
            int res = DAUtility.executeUpdate(conn, Queries.Insertions.CLASSI, flightClassNameTextField.getText());
            conn.close();

            if (res == 0) {
                showErrorAlert("Errore del database", "Inserzione fallita.");
                return;
            }

            parentController.refreshTableViews();
            Stage stage = (Stage) insertButton.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            showErrorAlert("Errore del database", "Inserzione fallita.");
            throw new RuntimeException(e);
        }
    }

    public int prepare(MainWindowController mainWindowController) {
        if (mainWindowController != null) {
            this.parentController = mainWindowController;
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

