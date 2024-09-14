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
import org.application.dbairline.model.data.tables.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InsertPurchaseWindowController implements Initializable {

    @FXML
    private TableColumn<?, ?> flightClassesColumn;
    @FXML
    private TableColumn<?, ?> flightIdColumn;
    @FXML
    private TableColumn<?, ?> flightNumberColumn;
    @FXML
    private TableColumn<?, ?> paxNameColumn;
    @FXML
    private TableColumn<?, ?> seatNumberColumn;
    @FXML
    private TableColumn<?, ?> seatConfigurationColumn;
    @FXML
    private TableView<Biglietto> ticketTable;

    @FXML
    private ComboBox<Integer> flightIdComboBox;
    @FXML
    private ComboBox<String> flightClassComboBox;
    @FXML
    private ComboBox<String> seatComboBox;
    @FXML
    private Button closeButton;
    @FXML
    private Button insertButton;
    @FXML
    private Button insertFlightClassButton;
    @FXML
    private TextField paxNameTextField;
    @FXML
    private TextField buyerEmailTextField;
    @FXML
    private TextField buyerNameTextField;
    @FXML
    private TextField buyerTelephoneTextField;

    private WindowController parentController;
    private List<Volo> flights;
    private List<Classe> flightClasses;
    private List<Posto> seats;
    private List<Biglietto> tickets;
    private List<Tariffa> fares;

    @FXML
    void CloseWindow(MouseEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void insert(MouseEvent event) {
        if (tickets.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Insertion Error");
            alert.setContentText("No tickets to insert.");
            alert.showAndWait();
            return;
        }

        String buyerEmail = buyerEmailTextField.getText();
        String buyerName = buyerNameTextField.getText();
        String buyerTelephone = buyerTelephoneTextField.getText();
        float amount = (float) tickets.stream()
                .mapToDouble(Biglietto::getPrezzo)
                .sum();

        if (buyerEmail == null || buyerName == null || buyerTelephone == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Input Error");
            alert.setContentText("Fields must be filled out.");
            alert.showAndWait();
            return;
        }

        Connection conn = null;
        try {
            conn = DAUtility.getConnection(false, Connection.TRANSACTION_SERIALIZABLE);

            ResultSet res = DAUtility.executeUpdateWithGeneratedKeys(conn, Queries.Insertions.ACQUISTI,
                    Date.valueOf(LocalDate.now().toString()),
                    buyerName,
                    buyerEmail,
                    buyerTelephone,
                    amount);

            Integer purchaseId = -1;
            if (res.next()) {
                purchaseId = res.getInt(1);
            }

            if (purchaseId < 0) {
                showErrorAlert("Errore di input",
                        "Si prega di compilare tutti i campi prima di procedere.");
                throw new Exception("ResultSet has not returned a valid id.");
            }
            res.close();

            List<Biglietto> distinctTickets = tickets.stream().distinct().toList();
            for (Biglietto ticket : distinctTickets) {
                Integer flightNumber = flights.stream()
                        .filter(f -> f.getIdVolo() == ticket.getIdVolo())
                        .map(Volo::getNumeroDiVolo)
                        .findFirst()
                        .orElse(null);

                Integer legNumber = flights.stream()
                        .filter(f -> f.getIdVolo() == ticket.getIdVolo())
                        .map(Volo::getNumeroTratta)
                        .findFirst()
                        .orElse(null);

                Integer flightClassId = flightClasses.stream()
                        .filter(c -> c.getNomeClasse().equals(ticket.getNomeClasse()))
                        .map(Classe::getIdClasse)
                        .findFirst()
                        .orElse(null);

                if (flightNumber == null || legNumber == null || flightClassId == null) {
                    showErrorAlert("Errore del database",
                            "Numero di volo, numero tratta o classe di volo potrebbero non essere presenti nel database.");
                    return;
                }

                Integer fareId = fares.stream()
                        .filter(f -> f.getNumeroVolo() == flightNumber
                                     && f.getNumeroTratta() == legNumber
                                     && f.getIdClasse() == flightClassId)
                        .map(Tariffa::getIdTariffa)
                        .findFirst()
                        .orElse(null);

                if (fareId == null) {
                    showErrorAlert("Errore del database", "Inserzione fallita.");
                    throw new IllegalArgumentException("Fare ID is invalid.");
                }

                int result = DAUtility.executeUpdate(conn, Queries.Insertions.BIGLIETTI,
                        ticket.getNomePasseggero(),
                        purchaseId,
                        ticket.getPrezzo(),
                        ticket.getIdVolo(),
                        ticket.getIdConfigurazione(),
                        ticket.getNumeroPosto());
                if (result == 0) {
                    throw new IllegalStateException("Failed to insert ticket");
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
                    throw new RuntimeException("Transaction rollback failed", se);
                }
            }
            showErrorAlert("Errore del database", "Inserzione fallita.");
        }
    }

    @FXML
    void insertNewTicket(MouseEvent event) {
        String paxName = paxNameTextField.getText();
        Integer flightId = flightIdComboBox.getSelectionModel().getSelectedItem();
        String flightClassName = flightClassComboBox.getSelectionModel().getSelectedItem();
        String seat = seatComboBox.getSelectionModel().getSelectedItem();

        if (paxName == null || flightId == null || flightClassName == null || seat == null) {
            showErrorAlert("Errore di input",
                    "Si prega di compilare tutti i campi prima di procedere.");
            return;
        }

        Volo flight = flights.stream()
                .filter(f -> f.getIdVolo() == flightId)
                .findFirst()
                .orElse(null);

        Integer flightNumber = flight.getNumeroDiVolo();
        Integer legNumber = flight.getNumeroTratta();
        Integer configId = flight.getConfigurazione();
        Integer flightClassId = flightClasses.stream()
                .filter(fc -> fc.getNomeClasse().equals(flightClassName))
                .map(Classe::getIdClasse)
                .findFirst()
                .orElse(null);

        if (flightNumber == null || legNumber == null || configId == null || flightClassId == null) {
            showErrorAlert("Errore di inserzione",
                    "Qualcosa è andato storto.");
            return;
        }

        Float fare = 0f;
        try {
            Connection conn = DAUtility.getConnection();
            String selectFareQuery = "SELECT Tariffa FROM TARIFFE WHERE NumeroDiVolo = ? AND NumeroTratta = ? AND IdClasse = ?";
            ResultSet rs = DAUtility.executeQuery(conn, selectFareQuery, flightNumber, legNumber, flightClassId);
            if (rs.next()) {
                fare = rs.getFloat("Tariffa");
            }
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        tickets.add(new Biglietto(
                0,
                paxName,
                0,
                fare,
                flightId,
                configId,
                seat,
                flightClassName));

        ticketTable.setItems(FXCollections.observableList(tickets));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        flightClassesColumn.setCellValueFactory(new PropertyValueFactory<>("nomeClasse"));
        flightIdColumn.setCellValueFactory(new PropertyValueFactory<>("idVolo"));
        flightNumberColumn.setCellValueFactory(new PropertyValueFactory<>("numeroDiVolo"));
        paxNameColumn.setCellValueFactory(new PropertyValueFactory<>("nomePasseggero"));
        seatNumberColumn.setCellValueFactory(new PropertyValueFactory<>("numeroPosto"));
        seatConfigurationColumn.setCellValueFactory(new PropertyValueFactory<>("idConfigurazione"));

        flights = new ArrayList<>();
        flightClasses = new ArrayList<>();
        seats = new ArrayList<>();
        tickets = new ArrayList<>();
        fares = new ArrayList<>();

        try {
            Connection conn = DAUtility.getConnection();
            String selectSQL = "SELECT * FROM VOLI WHERE DataOraPartenzaProg > NOW()";
            this.flights.addAll(Volo.mapToNotExtended(DAUtility.executeQuery(conn, selectSQL)));
            conn.close();
        } catch (SQLException e) {
            showErrorAlert("Errore di caricamento", "Impossibile caricare la finestra.");
            throw new RuntimeException(e);
        }

        flightIdComboBox.setItems(FXCollections.observableList(flights.stream()
                .map(Volo::getIdVolo)
                .toList()));


        flightIdComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                flightClassComboBox.setItems(FXCollections.observableArrayList());
                seatComboBox.setItems(FXCollections.observableArrayList());
                flightClasses.clear();
                seats.clear();

                Integer flightNumber = flights.stream()
                        .filter(f -> f.getIdVolo() == newValue)
                        .map(Volo::getNumeroDiVolo)
                        .findFirst()
                        .orElse(null);

                Integer legNumber = flights.stream()
                        .filter(f -> f.getIdVolo() == newValue)
                        .map(Volo::getNumeroTratta)
                        .findFirst()
                        .orElse(null);

                try {
                    Connection conn = DAUtility.getConnection();
                    String selectFlightClassesFromFlight = "SELECT DISTINCT C.IdClasse, C.NomeClasse FROM VOLI V " +
                                                           "JOIN POSTI P ON V.IdConfigurazione = P.IdConfigurazione " +
                                                           "JOIN CLASSI C ON P.IdClasse = C.IdClasse " +
                                                           "WHERE V.IdVolo = ?";
                    this.flightClasses.addAll(Classe.mapTo(DAUtility.executeQuery(conn, selectFlightClassesFromFlight, newValue)));
                    String selectSeatsFromFlight = "SELECT P.IdConfigurazione, P.NumeroPosto, P.IdClasse, C.NomeClasse " +
                                                   "FROM POSTI P " +
                                                   "JOIN CLASSI C ON P.IdClasse = C.IdClasse " +
                                                   "WHERE P.IdConfigurazione = " +
                                                   "(SELECT IdConfigurazione FROM VOLI WHERE IdVolo = ?)";
                    this.seats.addAll(Posto.mapTo(DAUtility.executeQuery(conn, selectSeatsFromFlight, newValue)));
                    String selectNonExpiredFaresSQL = "SELECT * FROM TARIFFE " +
                                                      "WHERE DataInizio <= NOW() " +
                                                      "AND (DataFine IS NULL OR DataFine >= NOW())";
                    this.fares.addAll(Tariffa.mapTo(DAUtility.executeQuery(conn, selectNonExpiredFaresSQL)));
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                List<Tariffa> validFares = fares.stream()
                        .filter(f -> f.getNumeroVolo() == flightNumber && f.getNumeroTratta() == legNumber)
                        .toList();

                List<Classe> filteredFlightClasses = flightClasses.stream()
                        .filter(c -> validFares.stream().map(Tariffa::getIdClasse).toList().contains(c.getIdClasse()))
                        .toList();

                flightClassComboBox.setItems(FXCollections.observableList(filteredFlightClasses.stream()
                        .map(Classe::getNomeClasse)
                        .toList()));
            }
        });

        flightClassComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && flightIdComboBox.getSelectionModel().getSelectedItem() != null) {

                Integer flightNumber = flights.stream()
                        .filter(f -> f.getIdVolo() == flightIdComboBox.getSelectionModel().getSelectedItem())
                        .map(Volo::getNumeroDiVolo)
                        .findFirst()
                        .orElse(null);

                try {
                    Connection conn = DAUtility.getConnection();
                    String selectUnoccupiedSeats = "SELECT P.* " +
                                                   "FROM POSTI P " +
                                                   "JOIN VOLI V ON P.IdConfigurazione = V.IdConfigurazione " +
                                                   "WHERE V.IdVolo = ? " +
                                                   "AND P.NumeroPosto NOT IN ( " +
                                                   "    SELECT B.NumeroPosto " +
                                                   "    FROM BIGLIETTI B " +
                                                   "    WHERE B.IdVolo = ? " +
                                                   ")";
                    this.seats.addAll(Posto.mapTo(DAUtility.executeQuery(conn, selectUnoccupiedSeats, flightNumber, flightNumber)));
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                seatComboBox.setItems(FXCollections.observableList(seats.stream()
                        .filter(seat -> seat.getNomeClasse().equals(newValue))
                        .map(Posto::getNumeroPosto)
                        .toList()));
            }
        });
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
