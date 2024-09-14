package org.application.dbairline.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.application.dbairline.model.data.DAUtility;
import org.application.dbairline.model.data.Queries;
import org.application.dbairline.model.data.tables.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class ModifyFlightWindowController implements Initializable {

    public static final String NO_VALUE = "Nessuno";

    private WindowController parentController;
    private Integer flightId;
    private Volo flight;
    private List<Configurazione> seatConfigurations;
    private List<Tratta> legs;
    private List<Aereo> aicrafts;

    @FXML
    private ChoiceBox<String> aircraftCB;
    @FXML
    private ChoiceBox<String> aircraftConfigCB;
    @FXML
    private ComboBox<String> altCB;
    @FXML
    private DatePicker arrivalDatePicker;
    @FXML
    private CheckBox cancelFlightCB;
    @FXML
    private Button closeButton;
    @FXML
    private DatePicker departureDatePicker;
    @FXML
    private ChoiceBox<Integer> effArrivalHourCB;
    @FXML
    private ChoiceBox<Integer> effArrivalMinuteCB;
    @FXML
    private ChoiceBox<Integer> effDepartureHourCB;
    @FXML
    private ChoiceBox<Integer> effDepartureMinuteCB;
    @FXML
    private ChoiceBox<Integer> flightNumberCB;
    @FXML
    private Button insertButton;
    @FXML
    private ChoiceBox<Integer> legNumberCB;
    @FXML
    private ChoiceBox<Integer> progArrivalMinuteCB;
    @FXML
    private ChoiceBox<Integer> progArrivalHourCB;
    @FXML
    private ChoiceBox<Integer> progDepartureHourCB;
    @FXML
    private ChoiceBox<Integer> progDepartureMinuteCB;


    @FXML
    void CloseWindow(MouseEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void modifyFlight(MouseEvent event) {
        Timestamp effDeparture;
        if (effDepartureHourCB.getSelectionModel().getSelectedItem() == null || effDepartureMinuteCB.getSelectionModel().getSelectedItem() == null) {
            effDeparture = null;
        } else {
            effDeparture = Timestamp.valueOf(departureDatePicker.getValue().atTime(effDepartureHourCB.getSelectionModel().getSelectedItem(), effDepartureMinuteCB.getSelectionModel().getSelectedItem()));
        }

        Timestamp effArrival;
        if (effArrivalHourCB.getSelectionModel().getSelectedItem() == null || effArrivalMinuteCB.getSelectionModel().getSelectedItem() == null) {
            effArrival = null;
        } else {
            effArrival = Timestamp.valueOf(arrivalDatePicker.getValue().atTime(effArrivalHourCB.getSelectionModel().getSelectedItem(), effArrivalMinuteCB.getSelectionModel().getSelectedItem()));
        }

        String aircraft;
        if(aircraftCB.getSelectionModel().getSelectedItem().equals(NO_VALUE)){
            aircraft = null;
        } else {
            aircraft = aircraftCB.getSelectionModel().getSelectedItem();
        }

        String alternativeAirport;
        if(altCB.getSelectionModel().getSelectedItem().equals(NO_VALUE)){
            alternativeAirport = null;
        } else {
            alternativeAirport = altCB.getSelectionModel().getSelectedItem();
        }

        try {
            Connection conn = DAUtility.getConnection();
            int res = DAUtility.executeUpdate(conn, "UPDATE VOLI SET " +
                                                    "NumeroDiVolo = ?, " +
                                                    "NumeroTratta = ?, " +
                                                    "DataOraPartenzaProg  = ?, " +
                                                    "DataOraArrivoProg = ?, " +
                                                    "DataOraPartenzaEff = ?, " +
                                                    "DataOraArrivoEff = ?, " +
                                                    "Aereo = ?, " +
                                                    "AeroportoAlternativo = ?, " +
                                                    "VoloCancellato = ?, " +
                                                    "IdConfigurazione = ? " +
                                                    "WHERE IdVolo = " + flight.getIdVolo(),
                    flightNumberCB.getSelectionModel().getSelectedItem(),
                    legNumberCB.getSelectionModel().getSelectedItem(),
                    Timestamp.valueOf(departureDatePicker.getValue().atTime(progDepartureHourCB.getSelectionModel().getSelectedItem(), progDepartureMinuteCB.getSelectionModel().getSelectedItem())),
                    Timestamp.valueOf(arrivalDatePicker.getValue().atTime(progArrivalHourCB.getSelectionModel().getSelectedItem(), progArrivalMinuteCB.getSelectionModel().getSelectedItem())),
                    effDeparture,
                    effArrival,
                    aircraft,
                    alternativeAirport,
                    cancelFlightCB.isSelected(),
                    seatConfigurations.stream()
                            .filter(conf -> conf.getNomeConfigurazione().equals(aircraftConfigCB.getSelectionModel().getSelectedItem()))
                            .map(Configurazione::getIdConfigurazione)
                            .findFirst()
                            .orElse(null));

            if (res == 0){
                throw new SQLException();
            }

            conn.close();
            parentController.refreshTableViews();
            Stage stage = (Stage) insertButton.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            showErrorAlert("Errore del database", "Inserzione fallita.");
        }
    }

    public int prepare(WindowController Controller, Integer idVolo) {
        if (Controller == null || idVolo == null) {
            return 1;
        } else {
            this.parentController = Controller;
            this.flightId = idVolo;

            try {
                Connection conn = DAUtility.getConnection();
                ResultSet rs = DAUtility.executeQuery(conn, Queries.SelectAll.VOLI + " WHERE IdVolo = " + flightId);
                if (rs.next()) {
                    flight = new Volo(
                            rs.getInt("IdVolo"),
                            rs.getTimestamp("DataOraPartenzaProg"),
                            rs.getTimestamp("DataOraArrivoProg"),
                            rs.getTimestamp("DataOraPartenzaEff"),
                            rs.getTimestamp("DataOraArrivoEff"),
                            rs.getBoolean("VoloCancellato"),
                            rs.getInt("NumeroDiVolo"),
                            rs.getInt("NumeroTratta"),
                            rs.getString("Aereo"),
                            rs.getString("AeroportoAlternativo"),
                            rs.getInt("IdConfigurazione"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            flightNumberCB.setValue(flight.getNumeroDiVolo());
            legNumberCB.setValue(flight.getNumeroTratta());

            arrivalDatePicker.setValue(LocalDate.from(flight.getDataOraArrivoProg().toLocalDateTime()));
            departureDatePicker.setValue(LocalDate.from(flight.getDataOraPartenzaProg().toLocalDateTime()));

            progArrivalHourCB.setValue(flight.getDataOraArrivoProg().toLocalDateTime().getHour());
            progArrivalMinuteCB.setValue(flight.getDataOraArrivoProg().toLocalDateTime().getMinute());
            progDepartureHourCB.setValue(flight.getDataOraPartenzaProg().toLocalDateTime().getHour());
            progDepartureMinuteCB.setValue(flight.getDataOraPartenzaProg().toLocalDateTime().getMinute());

            if (flight.getDataOraArrivoEff() != null) {
                effArrivalHourCB.setValue(flight.getDataOraArrivoEff().toLocalDateTime().getHour());
                effArrivalMinuteCB.setValue(flight.getDataOraArrivoEff().toLocalDateTime().getMinute());
            }
            if (flight.getDataOraPartenzaEff() != null) {
                effDepartureHourCB.setValue(flight.getDataOraPartenzaEff().toLocalDateTime().getHour());
                effDepartureMinuteCB.setValue(flight.getDataOraPartenzaEff().toLocalDateTime().getMinute());
            }

            if (flight.getAereo() != null) {
                aircraftCB.setValue(flight.getAereo());
            } else {
                aircraftCB.setValue(NO_VALUE);
            }

            aircraftConfigCB.setValue(seatConfigurations.stream()
                    .filter(conf -> conf.getIdConfigurazione() == flight.getConfigurazione())
                    .map(Configurazione::getNomeConfigurazione)
                    .toList().getFirst());

            if (flight.getAeroportoAlternativo() != null) {
                altCB.setValue(flight.getAeroportoAlternativo());
            } else {
                altCB.setValue(NO_VALUE);
            }

            if (flight.isVoloCancellato()) {
                cancelFlightCB.setSelected(true);
            } else {
                cancelFlightCB.setSelected(false);
            }

            return 0;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        progDepartureHourCB.setItems(FXCollections.observableList(IntStream.range(0, 24).boxed().toList()));
        progArrivalHourCB.setItems(FXCollections.observableList(IntStream.range(0, 24).boxed().toList()));
        progDepartureMinuteCB.setItems(FXCollections.observableList(IntStream.range(0, 60).boxed().toList()));
        progArrivalMinuteCB.setItems(FXCollections.observableList(IntStream.range(0, 60).boxed().toList()));

        effDepartureHourCB.setItems(FXCollections.observableList(IntStream.range(0, 24).boxed().toList()));
        effArrivalHourCB.setItems(FXCollections.observableList(IntStream.range(0, 24).boxed().toList()));
        effDepartureMinuteCB.setItems(FXCollections.observableList(IntStream.range(0, 60).boxed().toList()));
        effArrivalMinuteCB.setItems(FXCollections.observableList(IntStream.range(0, 60).boxed().toList()));


        List<Aeroporto> airports;
        try {
            List<Integer> flightNumbers = new ArrayList<>();
            Connection conn = DAUtility.getConnection();
            ResultSet rs = DAUtility.executeQuery(conn, Queries.SelectAll.ITINERARI);
            while (rs.next()) {
                flightNumbers.add(rs.getInt("NumeroDiVolo"));
            }
            flightNumberCB.setItems(FXCollections.observableList(flightNumbers));
            this.seatConfigurations = Configurazione.mapTo(DAUtility.executeQuery(conn, Queries.SelectAll.CONFIGURAZIONI));
            this.legs = Tratta.mapTo(DAUtility.executeQuery(conn, Queries.SelectAll.TRATTE));
            this.aicrafts = Aereo.mapTo(DAUtility.executeQuery(conn, Queries.SelectAll.AEREI));
            airports = Aeroporto.mapTo(DAUtility.executeQuery(conn, Queries.SelectAll.AEROPORTI));
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        altCB.setItems(FXCollections.observableList(airports.stream().map(Aeroporto::getIcao).toList()));
        List<String> aircraftRegistrationCodeList = new ArrayList<>(aicrafts.stream().map(Aereo::getCodiceRegistrazione).toList());
        aircraftRegistrationCodeList.addFirst(NO_VALUE);
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

    private void showErrorAlert(String header, String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(header);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
