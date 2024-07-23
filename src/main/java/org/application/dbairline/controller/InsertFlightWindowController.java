package org.application.dbairline.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.application.dbairline.model.data.Queries;
import org.application.dbairline.model.data.DAUtility;
import org.application.dbairline.model.data.tables.Aereo;
import org.application.dbairline.model.data.tables.Configurazione;
import org.application.dbairline.model.data.tables.Tratta;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class InsertFlightWindowController implements Initializable {

    public static final String NO_AIRCRAFT = "Nessuno";

    private MainWindowController parentController;
    private List<Configurazione> seatConfigurations;
    private List<Tratta> legs;
    private List<Aereo> aicrafts;

    @FXML
    private ChoiceBox<Integer> flightNumberCB = new ChoiceBox<>();

    @FXML
    private ChoiceBox<Integer> legNumberCB = new ChoiceBox<>();

    @FXML
    private DatePicker departureDatePicker = new DatePicker();

    @FXML
    private DatePicker arrivalDatePicker = new DatePicker();

    @FXML
    private ChoiceBox<Integer> departureHourCB = new ChoiceBox<>();

    @FXML
    private ChoiceBox<Integer> departureMinuteCB = new ChoiceBox<>();

    @FXML
    private ChoiceBox<Integer> arrivalHourCB = new ChoiceBox<>();

    @FXML
    private ChoiceBox<Integer> arrivalMinuteCB = new ChoiceBox<>();

    @FXML
    private ChoiceBox<String> aircraftCB = new ChoiceBox<>();

    @FXML
    private ChoiceBox<String> aircraftConfigCB = new ChoiceBox<>();

    @FXML
    private Button insertButton;

    @FXML
    private Button closeButton;

    @FXML
    void CloseWindow(MouseEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void insertFlight(MouseEvent event) {
        Integer flightNumber = flightNumberCB.getSelectionModel().getSelectedItem();
        Integer legNumber = legNumberCB.getSelectionModel().getSelectedItem();

        LocalDate departureDatePickerValue = departureDatePicker.getValue();
        Integer departureHour = departureHourCB.getSelectionModel().getSelectedItem();
        Integer departureMinute = departureMinuteCB.getSelectionModel().getSelectedItem();
        LocalDate arrivalDatePickerValue = arrivalDatePicker.getValue();
        Integer arrivalHour = arrivalHourCB.getSelectionModel().getSelectedItem();
        Integer arrivalMinute = arrivalMinuteCB.getSelectionModel().getSelectedItem();

        String aircraft = aircraftCB.getSelectionModel().getSelectedItem();
        String seatConfigName = aircraftConfigCB.getSelectionModel().getSelectedItem();


        if (flightNumber == null || legNumber == null || departureDatePickerValue == null ||
            departureHour == null || departureMinute == null || arrivalDatePickerValue == null ||
            arrivalHour == null || arrivalMinute == null || aircraft == null || seatConfigName == null) {
            showErrorAlert("Errore di input",
                    "Si prega di compilare tutti i campi prima di procedere.");
            return;
        }

        Integer aircraftConfig = seatConfigurations.stream()
                .filter(x -> x.getNomeConfigurazione().equals(seatConfigName))
                .map(Configurazione::getIdConfigurazione)
                .toList()
                .getFirst();
        Timestamp departureDate = Timestamp.valueOf(departureDatePickerValue.atTime(departureHour, departureMinute));
        Timestamp arrivalDate = Timestamp.valueOf(arrivalDatePickerValue.atTime(departureHour, departureMinute));

        if (arrivalDate.before(departureDate)) {
            showErrorAlert("Errore di input",
                    "La data di arrivo non può essere più recente di quella di partenza.");
            return;
        }

        try {
            int res;
            Connection conn = DAUtility.getConnection();

            if (!aircraft.equals(NO_AIRCRAFT)) {
                res = DAUtility.executeUpdate(conn, Queries.Insertions.VOLI,
                        departureDate,
                        arrivalDate,
                        false,
                        flightNumber,
                        legNumber,
                        aircraft,
                        aircraftConfig);
            } else {
                res = DAUtility.executeUpdate(conn, Queries.Insertions.VOLI_SENZA_AEREO,
                        departureDate,
                        arrivalDate,
                        false,
                        flightNumber,
                        legNumber,
                        aircraftConfig);
            }

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
    public void initialize(URL url, ResourceBundle resourceBundle) {
        departureHourCB.setItems(FXCollections.observableList(IntStream.range(0, 24).boxed().toList()));
        arrivalHourCB.setItems(FXCollections.observableList(IntStream.range(0, 24).boxed().toList()));
        departureMinuteCB.setItems(FXCollections.observableList(IntStream.range(0, 60).boxed().toList()));
        arrivalMinuteCB.setItems(FXCollections.observableList(IntStream.range(0, 60).boxed().toList()));

        try {
            List<Integer> flightNumbers = new ArrayList<>();
            Connection conn = DAUtility.getConnection();
            String query = "SELECT NumeroDiVolo FROM Itinerari";
            ResultSet rs = DAUtility.executeQuery(conn, query);
            while (rs.next()) {
                flightNumbers.add(rs.getInt("NumeroDiVolo"));
            }
            flightNumberCB.getItems().addAll(FXCollections.observableArrayList(flightNumbers));
            this.seatConfigurations = Configurazione.mapTo(DAUtility.executeQuery(conn, Queries.SelectAll.CONFIGURAZIONI));
            this.legs = Tratta.mapTo(DAUtility.executeQuery(conn, Queries.SelectAll.TRATTE));
            this.aicrafts = Aereo.mapTo(DAUtility.executeQuery(conn, Queries.SelectAll.AEREI));
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<String> aircraftRegistrationCodeList = new ArrayList<>(aicrafts.stream().map(Aereo::getCodiceRegistrazione).toList());
        aircraftRegistrationCodeList.addFirst(NO_AIRCRAFT);
        aircraftCB.setItems(FXCollections.observableList(aircraftRegistrationCodeList));
        aircraftConfigCB.setItems(FXCollections.observableList(
                seatConfigurations.stream()
                        .map(Configurazione::getNomeConfigurazione)
                        .toList()));

        flightNumberCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                List<Integer> legNumbers = legs.stream()
                        .filter(leg -> leg.getNumeroDiVolo() == newValue)
                        .map(Tratta::getNumeroTratta)
                        .toList();
                legNumberCB.setItems(FXCollections.observableList(legNumbers));
            }
        });
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
