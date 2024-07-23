package org.application.dbairline.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.application.dbairline.model.data.DAUtility;
import org.application.dbairline.model.data.Queries;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class InsertLegWindowController implements Initializable {

    private int flightNumber;
    private int legNumber;

    @FXML
    private Button closeButton;

    @FXML
    private Button insertButton;

    @FXML
    private ChoiceBox<String> legArrCB;

    @FXML
    private ChoiceBox<String> legDepCB;
    private MainWindowController parentController;

    @FXML
    void exitWindow(MouseEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void insert(MouseEvent event) {
        String departure = legDepCB.getValue();
        String arrival = legArrCB.getValue();
        if (departure != null && arrival != null) {
            try {
                Connection conn = DAUtility.getConnection();
                int res = DAUtility.executeUpdate(conn, Queries.Insertions.TRATTE, flightNumber, legNumber, departure, arrival);
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
                throw new RuntimeException(e);
            }
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            List<String> airports = getAirports();

            if (!airports.isEmpty()) {
                legDepCB.setItems(FXCollections.observableArrayList(airports));
                legArrCB.setItems(FXCollections.observableArrayList(airports));
            }
        } catch (SQLException e) {
            showErrorAlert("Errore di caricamento", "Impossibile caricare finestra.");
            throw new RuntimeException(e);
        }
    }

    private List<String> getAirports() throws SQLException {
        List<String> airports = new ArrayList<>();
        Connection conn = DAUtility.getConnection();
        ResultSet resultSet = DAUtility.executeQuery(conn, "SELECT DISTINCT ICAO FROM aeroporti");
        while (resultSet.next()) {
            airports.add(resultSet.getString("ICAO"));
        }
        conn.close();
        return airports;
    }

    public int prepare(MainWindowController mainWindowController, Integer flightNumber) {
        if (mainWindowController != null && flightNumber != null) {
            this.parentController = mainWindowController;
            this.flightNumber = flightNumber;
            this.getLegNumbers();
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

    private void getLegNumbers() {
        List<Integer> legNumbers = new ArrayList<>();

        try {
            Connection conn = DAUtility.getConnection();
            ResultSet resultSet = DAUtility.executeQuery(conn, "SELECT NumeroTratta FROM tratte WHERE NumeroDiVolo = ?", flightNumber);
            while (resultSet.next()) {
                legNumbers.add(resultSet.getInt("NumeroTratta"));
            }
            conn.close();
            this.legNumber = legNumbers.stream().max(Comparator.naturalOrder()).orElse(0) + 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
