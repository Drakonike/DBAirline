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

public class InsertAirportWindowController {

    private WindowController parentController;

    @FXML
    private TextField cityTF;

    @FXML
    private Button closeButton;

    @FXML
    private TextField commercialNameTF;

    @FXML
    private TextField countryTF;

    @FXML
    private TextField faxTF;

    @FXML
    private TextField iataTF;

    @FXML
    private TextField icaoTF;

    @FXML
    private Button insertButton;

    @FXML
    private TextField managerContactTF;

    @FXML
    private TextField managerTF;

    @FXML
    private TextField telTF;


    @FXML
    void CloseWindow(MouseEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void insert(MouseEvent event) {
        String icao = icaoTF.getText();
        String iata = iataTF.getText();
        String commercialName = commercialNameTF.getText();
        String city = cityTF.getText();
        String country = countryTF.getText();
        String manager = managerTF.getText();
        String managerContact = managerContactTF.getText();
        String tel = telTF.getText();
        String fax = faxTF.getText();

        if (icao == null || icao.isEmpty() ||
            iata == null || iata.isEmpty() ||
            commercialName == null || commercialName.isEmpty() ||
            city == null || city.isEmpty() ||
            country == null || country.isEmpty() ||
            manager == null || manager.isEmpty() ||
            managerContact == null || managerContact.isEmpty() ||
            tel == null || tel.isEmpty() ||
            fax == null || fax.isEmpty()) {

            showErrorAlert("Errore di input",
                    "Si prega di compilare tutti i campi prima di procedere.");
            return;
        }

        try {
            Connection conn = DAUtility.getConnection();
            int res = DAUtility.executeUpdate(conn, Queries.Insertions.AEROPORTI,
                    icao, iata, commercialName, city, country, manager, managerContact, tel, fax);

            conn.close();
            if (res == 0) {
                throw new SQLException("No insertion executed");
            }

            parentController.refreshTableViews();
            Stage stage = (Stage) insertButton.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showErrorAlert("Errore del database", "Inserzione fallita.");
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
