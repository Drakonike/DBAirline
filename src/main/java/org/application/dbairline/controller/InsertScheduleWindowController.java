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
import org.application.dbairline.model.data.tables.Configurazione;
import org.application.dbairline.model.data.tables.Tratta;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.application.dbairline.model.data.tables.Programma.DAYS;

public class InsertScheduleWindowController implements Initializable {

    private WindowController parentController;
    private List<Tratta> legs;
    private List<Configurazione> configurazioni;

    @FXML
    private Button closeButton;
    @FXML
    private Button insertButton;
    @FXML
    private ChoiceBox<Integer> FlightNumCB;
    @FXML
    private ChoiceBox<Integer> legNumCB;
    @FXML
    private ChoiceBox<String> dayCB;
    @FXML
    private ChoiceBox<Integer> departureHourCB;
    @FXML
    private ChoiceBox<Integer> departureMinuteCB;
    @FXML
    private ChoiceBox<String> seatConfigCB;

    @FXML
    void exitWindow(MouseEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void insert(MouseEvent event) {
        Integer departureHour = departureHourCB.getSelectionModel().getSelectedItem();
        Integer departureMinute = departureMinuteCB.getSelectionModel().getSelectedItem();
        String seatConfig = seatConfigCB.getValue();
        Integer flightNumber = FlightNumCB.getSelectionModel().getSelectedItem();
        Integer legNumber = legNumCB.getSelectionModel().getSelectedItem();
        String day = dayCB.getSelectionModel().getSelectedItem();

        if (departureHour == null || departureMinute == null || seatConfig == null ||
            flightNumber == null || legNumber == null || day == null) {

            showErrorAlert("Errore di input",
                    "Si prega di compilare tutti i campi prima di procedere.");
            return;
        }


        Time departureTime = Time.valueOf(LocalTime.of(departureHour, departureMinute));

        int idConfigurazione = configurazioni.stream()
                .filter(config -> config.getNomeConfigurazione().equals(seatConfig))
                .findFirst()
                .map(Configurazione::getIdConfigurazione)
                .orElseThrow(() -> new RuntimeException("No matching seat config found"));

        try {
            Connection conn = DAUtility.getConnection();
            int res = DAUtility.executeUpdate(conn, Queries.Insertions.PROGRAMMI,
                    flightNumber,
                    legNumber,
                    day,
                    departureTime,
                    idConfigurazione);

            conn.close();

            if (res == 0) {
                showErrorAlert("Errore del database", "Inserzione fallita.");
                return;
            }

            parentController.refreshTableViews();
            Stage stage = (Stage) insertButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            List<Integer> flightNumbers = new ArrayList<>();
            Connection conn = DAUtility.getConnection();
            ResultSet rs = DAUtility.executeQuery(conn, "SELECT NumeroDiVolo FROM Itinerari");
            while (rs.next()) {
                flightNumbers.add(rs.getInt("NumeroDiVolo"));
            }
            FlightNumCB.getItems().addAll(FXCollections.observableArrayList(flightNumbers));
            this.configurazioni = Configurazione.mapTo(DAUtility.executeQuery(conn, Queries.SelectAll.CONFIGURAZIONI));
            this.legs = Tratta.mapTo(DAUtility.executeQuery(conn, Queries.SelectAll.TRATTE));
            conn.close();


            dayCB.setItems(FXCollections.observableList(DAYS));
            departureHourCB.setItems(FXCollections.observableList(IntStream.range(0, 24).boxed().toList()));
            departureMinuteCB.setItems(FXCollections.observableList(IntStream.range(0, 60).boxed().toList()));
            seatConfigCB.setItems(FXCollections.observableList(configurazioni.stream().map(Configurazione::getNomeConfigurazione).toList()));

            FlightNumCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    List<Integer> legNumbers = legs.stream()
                            .filter(leg -> leg.getNumeroDiVolo() == newValue)
                            .map(Tratta::getNumeroTratta)
                            .collect(Collectors.toList());
                    legNumCB.setItems(FXCollections.observableList(legNumbers));
                }
            });
        } catch (Exception e) {
            showErrorAlert("Errore di caricamento",
                    "Errore durante l'inizializzazione della finestra.");
            throw new RuntimeException(e);
        }
    }

    private void showErrorAlert(String header, String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
