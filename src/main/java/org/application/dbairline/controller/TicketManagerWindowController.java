package org.application.dbairline.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.application.dbairline.App;
import org.application.dbairline.model.data.DAUtility;
import org.application.dbairline.model.data.Queries;
import org.application.dbairline.model.data.tables.Acquisto;
import org.application.dbairline.model.data.tables.Biglietto;
import org.application.dbairline.model.data.tables.Volo;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class TicketManagerWindowController extends WindowController {

    private GUIManager manager;

    // Flights Table
    @FXML
    private TableColumn<?, ?> flightAircraftCol;
    @FXML
    private TableColumn<?, ?> flightAircraftConfigCol;
    @FXML
    private TableColumn<?, ?> flightAltAirportCol;
    @FXML
    private TableColumn<?, ?> flightArrCol;
    @FXML
    private TableColumn<?, ?> flightCancelledCol;
    @FXML
    private TableColumn<?, ?> flightDepCol;
    @FXML
    private TableColumn<?, ?> flightEffDepTimeCol;
    @FXML
    private TableColumn<?, ?> flightEffDesTimeCol;
    @FXML
    private TableColumn<?, ?> flightFlightNumCol;
    @FXML
    private TableColumn<?, ?> flightIdCol;
    @FXML
    private TableColumn<?, ?> flightProgDepTimeCol;
    @FXML
    private TableColumn<?, ?> flightProgDesTimeCol;
    @FXML
    private TableView<Volo> flightsTable;

    // table
    @FXML
    private TableColumn<?, ?> purchasesDateColumn;
    @FXML
    private TableColumn<?, ?> purchasesPurchaserEmailColumn;
    @FXML
    private TableColumn<?, ?> purchasesPurchaserNameColumn;
    @FXML
    private TableColumn<?, ?> purchasesPurchaserTelephoneNumberColumn;
    @FXML
    private TableColumn<?, ?> purchasesIdColumn;
    @FXML
    private TableColumn<?, ?> purchasesImportColumn;
    @FXML
    private TableView<Acquisto> purchasesTable;

    // table
    @FXML
    private TableColumn<?, ?> ticketsPurchaseIdColumn;
    @FXML
    private TableColumn<?, ?> ticketsFlightClassNameColumn;
    @FXML
    private TableColumn<?, ?> ticketsFlightIdColumn;
    @FXML
    private TableColumn<?, ?> ticketsIdColumn;
    @FXML
    private TableColumn<?, ?> ticketsSeatNumberColumn;
    @FXML
    private TableColumn<?, ?> ticketsPriceColumn;
    @FXML
    private TableColumn<?, ?> ticketsPaxNameColumn;
    @FXML
    private TableView<Biglietto> ticketsTable;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.configureTables();

        try {
            Connection conn = DAUtility.getConnection();
            this.populateTables(conn);
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshTableViews() {
        try {
            Connection conn = DAUtility.getConnection();
            this.populateTables(conn);
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setManager(GUIManager GUIManager) {
        this.manager = GUIManager;
    }

    @FXML
    private void insertPurchase(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/InsertPurchaseWindow.fxml"));
        Stage stage = manager.createPopup(loader);
        InsertPurchaseWindowController controller = loader.getController();
        controller.prepare(this);
        stage.showAndWait();
    }

    private void setupFlightsTable() {
        flightAircraftCol.setCellValueFactory(new PropertyValueFactory<>("aereo"));
        flightAircraftConfigCol.setCellValueFactory(new PropertyValueFactory<>("configurazione"));
        flightAltAirportCol.setCellValueFactory(new PropertyValueFactory<>("aeroportoAlternativo"));
        flightArrCol.setCellValueFactory(new PropertyValueFactory<>("arrAirport"));
        flightCancelledCol.setCellValueFactory(new PropertyValueFactory<>("voloCancellato"));
        flightDepCol.setCellValueFactory(new PropertyValueFactory<>("depAirport"));
        flightEffDepTimeCol.setCellValueFactory(new PropertyValueFactory<>("dataOraPartenzaEff"));
        flightEffDesTimeCol.setCellValueFactory(new PropertyValueFactory<>("dataOraArrivoEff"));
        flightFlightNumCol.setCellValueFactory(new PropertyValueFactory<>("numeroDiVolo"));
        flightIdCol.setCellValueFactory(new PropertyValueFactory<>("idVolo"));
        flightProgDepTimeCol.setCellValueFactory(new PropertyValueFactory<>("dataOraPartenzaProg"));
        flightProgDesTimeCol.setCellValueFactory(new PropertyValueFactory<>("dataOraArrivoProg"));
    }

    private void setupPurchasesTable() {
        purchasesDateColumn.setCellValueFactory(new PropertyValueFactory<>("dataAcquisto"));
        purchasesPurchaserEmailColumn.setCellValueFactory(new PropertyValueFactory<>("acqEmail"));
        purchasesPurchaserNameColumn.setCellValueFactory(new PropertyValueFactory<>("acqNomeCompleto"));
        purchasesPurchaserTelephoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("acqTelefono"));
        purchasesIdColumn.setCellValueFactory(new PropertyValueFactory<>("idAcquisto"));
        purchasesImportColumn.setCellValueFactory(new PropertyValueFactory<>("importo"));
    }

    private void setupTicketsTable() {
        ticketsPurchaseIdColumn.setCellValueFactory(new PropertyValueFactory<>("idAcquisto"));
        ticketsFlightClassNameColumn.setCellValueFactory(new PropertyValueFactory<>("nomeClasse"));
        ticketsFlightIdColumn.setCellValueFactory(new PropertyValueFactory<>("idVolo"));
        ticketsIdColumn.setCellValueFactory(new PropertyValueFactory<>("idBiglietto"));
        ticketsSeatNumberColumn.setCellValueFactory(new PropertyValueFactory<>("numeroPosto"));
        ticketsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("prezzo"));
        ticketsPaxNameColumn.setCellValueFactory(new PropertyValueFactory<>("nomePasseggero"));
    }

    private void configureTables() {
        setupFlightsTable();
        setupPurchasesTable();
        setupTicketsTable();
    }

    private void populateTables(Connection conn) {

        ObservableList<Acquisto> acquistiData = FXCollections.observableArrayList();
        acquistiData.addAll(fetchAcquistiFromDatabase(conn));
        if (!acquistiData.isEmpty()) {
            purchasesTable.setItems(acquistiData);
        }

        ObservableList<Volo> flightsData = FXCollections.observableArrayList();
        flightsData.addAll(fetchFlightsFromDatabase(conn));
        if (!flightsData.isEmpty()) {
            flightsTable.setItems(flightsData);
        }

        ObservableList<Biglietto> ticketsData = FXCollections.observableArrayList();
        ticketsData.addAll(fetchTicketsFromDatabase(conn));
        if (!ticketsData.isEmpty()) {
            ticketsTable.setItems(ticketsData);
        }
    }

    /////////////////////////////////////// Data fetching for tables /////////////////////////////////////////////////

    private List<Volo> fetchFlightsFromDatabase(Connection conn) {
        List<Volo> flights;
        try {
            String query = "SELECT v.IdVolo, v.NumeroDiVolo, v.NumeroTratta, t.Partenza, t.Destinazione, \n" +
                           "v.DataOraPartenzaProg, v.DataOraArrivoProg, v.DataOraPartenzaEff, \n" +
                           "v.DataOraArrivoEff, v.Aereo, v.IdConfigurazione, v.VoloCancellato, \n" +
                           "v.AeroportoAlternativo\n" +
                           "FROM voli v, tratte t\n" +
                           "WHERE v.NumeroDiVolo = t.NumeroDiVolo \n" +
                           "AND v.NumeroTratta = t.NumeroTratta";
            flights = Volo.mapTo(DAUtility.executeQuery(conn, query));
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return flights;
    }

    private List<Biglietto> fetchTicketsFromDatabase(Connection conn) {
        List<Biglietto> biglietti;
        try {
            String selectTicketsWithFlightClassNames =
                    "SELECT B.IdBiglietto, B.NomePasseggero, B.IdAcquisto, B.Prezzo, B.IdVolo, B.IdConfigurazione, B.NumeroPosto, C.NomeClasse " +
                    "FROM BIGLIETTI B " +
                    "JOIN POSTI P ON B.IdConfigurazione = P.IdConfigurazione AND B.NumeroPosto = P.NumeroPosto " +
                    "JOIN CLASSI C ON P.IdClasse = C.IdClasse";

            biglietti = Biglietto.mapToWithFlightClassName(DAUtility.executeQuery(conn, selectTicketsWithFlightClassNames));
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return biglietti;
    }

    private List<Acquisto> fetchAcquistiFromDatabase(Connection conn) {
        List<Acquisto> acquisti;
        try {
            acquisti = Acquisto.mapTo(DAUtility.executeQuery(conn, Queries.SelectAll.ACQUISTI));
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return acquisti;
    }
}
