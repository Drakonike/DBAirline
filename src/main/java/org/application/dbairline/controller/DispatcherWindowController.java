package org.application.dbairline.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.application.dbairline.App;
import org.application.dbairline.model.data.DAUtility;
import org.application.dbairline.model.data.Queries;
import org.application.dbairline.model.data.tables.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class DispatcherWindowController extends WindowController {

    private GUIManager manager;

    @FXML
    private TableColumn<?, ?> airportsCityColumn;
    @FXML
    private TableColumn<?, ?> airportsCommercialNameColumn;
    @FXML
    private TableColumn<?, ?> airportsCountryColumn;
    @FXML
    private TableColumn<?, ?> airportsFaxNumberColumn;
    @FXML
    private TableColumn<?, ?> airportsIATACodeColumn;
    @FXML
    private TableColumn<?, ?> airportsICAOCodeColumn;
    @FXML
    private TableColumn<?, ?> airportsManagerColumn;
    @FXML
    private TableColumn<?, ?> airportsManagerContactColumn;
    @FXML
    private TableColumn<?, ?> airportsTelephoneColumn;
    @FXML
    private TableView<Aeroporto> airportsTable;

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

    // Itineraries Table
    @FXML
    private TableColumn<?, ?> itinerariesFlightNumCol;
    @FXML
    private TableColumn<?, ?> itinerariesFlightTypeCol;
    @FXML
    private TableView<Itinerario> itinerariesTable;

    // Flight Legs Table
    @FXML
    private TableColumn<?, ?> flightLegsArrivalColumn;
    @FXML
    private TableColumn<?, ?> flightLegsDepartureColumn;
    @FXML
    private TableColumn<?, ?> flightLegslegNumberColumn;
    @FXML
    private TableView<Tratta> flightLegsTable;

    // table
    @FXML
    private TableColumn<?, ?> refuelsCostColumn;
    @FXML
    private TableColumn<?, ?> refuelsFuelTypeColumn;
    @FXML
    private TableColumn<?, ?> refuelsFlightIdColumn;
    @FXML
    private TableColumn<?, ?> refuelsQuantityColumn;
    @FXML
    private TableView<Rifornimento> refuelsTable;

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
    private void insertAirport(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/InsertAirportWindow.fxml"));
        Stage stage = manager.createPopup(loader);
        InsertAirportWindowController controller = loader.getController();
        controller.prepare(this);
        stage.showAndWait();
    }

    @FXML
    private void insertFlight(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/InsertFlightWindow.fxml"));
        Stage stage = manager.createPopup(loader);
        InsertFlightWindowController controller = loader.getController();
        controller.prepare(this);
        stage.showAndWait();
    }

    @FXML
    private void insertItinerary(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/InsertItineraryWindow.fxml"));
        Stage stage = manager.createPopup(loader);
        InsertItineraryWindowController controller = loader.getController();
        controller.prepare(this);
        stage.showAndWait();
    }

    @FXML
    private void insertLeg(MouseEvent event) {
        Itinerario clickedRow = itinerariesTable.getSelectionModel().getSelectedItem();
        if (clickedRow != null) {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/InsertLegWindow.fxml"));
            Stage stage = manager.createPopup(loader);
            InsertLegWindowController controller = loader.getController();
            controller.prepare(this, clickedRow.getNumeroDiVolo());
            stage.showAndWait();
        }
    }

    @FXML
    private void insertRefuel(MouseEvent event) {
        Volo clickedRow = flightsTable.getSelectionModel().getSelectedItem();
        if (clickedRow != null) {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/InsertRefuelWindow.fxml"));
            Stage stage = manager.createPopup(loader);
            InsertRefuelWindowController controller = loader.getController();
            controller.prepare(this, clickedRow.getIdVolo());
            stage.showAndWait();
        }
    }

    @FXML
    private void modifyCabinCrew(MouseEvent event) {
        Volo clickedRow = flightsTable.getSelectionModel().getSelectedItem();
        if (clickedRow != null) {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/ModifyCrewWindow.fxml"));
            Stage stage = manager.createPopup(loader);
            ModifyCrewWindowController controller = loader.getController();
            controller.prepare(this, clickedRow.getIdVolo());
            stage.showAndWait();
        }
    }

    @FXML
    private void modifyFlight(MouseEvent event) {
        Volo clickedRow = flightsTable.getSelectionModel().getSelectedItem();
        if (clickedRow != null) {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/ModifyFlightWindow.fxml"));
            Stage stage = manager.createPopup(loader);
            ModifyFlightWindowController controller = loader.getController();
            controller.prepare(this, clickedRow.getIdVolo());
            stage.showAndWait();
        }
    }

    private void setupAirportTable() {
        airportsCityColumn.setCellValueFactory(new PropertyValueFactory<>("citta"));
        airportsCommercialNameColumn.setCellValueFactory(new PropertyValueFactory<>("nomeCommerciale"));
        airportsCountryColumn.setCellValueFactory(new PropertyValueFactory<>("nazione"));
        airportsFaxNumberColumn.setCellValueFactory(new PropertyValueFactory<>("fax"));
        airportsIATACodeColumn.setCellValueFactory(new PropertyValueFactory<>("iata"));
        airportsICAOCodeColumn.setCellValueFactory(new PropertyValueFactory<>("icao"));
        airportsManagerColumn.setCellValueFactory(new PropertyValueFactory<>("gestore"));
        airportsManagerContactColumn.setCellValueFactory(new PropertyValueFactory<>("contattoGestore"));
        airportsTelephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));
    }

    private void setupFlightLegsTable() {
        flightLegsArrivalColumn.setCellValueFactory(new PropertyValueFactory<>("destinazione"));
        flightLegsDepartureColumn.setCellValueFactory(new PropertyValueFactory<>("partenza"));
        flightLegslegNumberColumn.setCellValueFactory(new PropertyValueFactory<>("numeroTratta"));
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

    private void setupItinerariesTable() {
        itinerariesFlightNumCol.setCellValueFactory(new PropertyValueFactory<>("numeroDiVolo"));
        itinerariesFlightTypeCol.setCellValueFactory(new PropertyValueFactory<>("tipoVolo"));
    }

    private void setupRefuelsTable() {
        refuelsCostColumn.setCellValueFactory(new PropertyValueFactory<>("costo"));
        refuelsFuelTypeColumn.setCellValueFactory(new PropertyValueFactory<>("tipoCarburante"));
        refuelsFlightIdColumn.setCellValueFactory(new PropertyValueFactory<>("idVolo"));
        refuelsQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantita"));
    }

    private void configureTables() {
        setupAirportTable();
        setupFlightLegsTable();
        setupFlightsTable();
        setupItinerariesTable();
        setupRefuelsTable();

        itinerariesTable.setRowFactory(tv -> {
            ObservableList<Tratta> legData = FXCollections.observableArrayList();
            TableRow<Itinerario> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    flightLegsTable.getItems().clear();
                    Itinerario rowData = row.getItem();
                    try {
                        Connection conn = DAUtility.getConnection();
                        String query = "SELECT * FROM tratte WHERE NumeroDiVolo = ?";
                        legData.addAll(Tratta.mapTo(DAUtility.executeQuery(conn, query, rowData.getNumeroDiVolo())));
                        conn.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    flightLegsTable.setItems(legData);
                }
            });
            return row;
        });
    }

    private void populateTables(Connection conn) {

        ObservableList<Aeroporto> airportsData = FXCollections.observableArrayList();
        airportsData.addAll(fetchAirportsFromDatabase(conn));
        if (!airportsData.isEmpty()) {
            airportsTable.setItems(airportsData);
        }

        ObservableList<Itinerario> itinerariesData = FXCollections.observableArrayList();
        itinerariesData.addAll(fetchItinerariFromDatabase(conn));
        if (!itinerariesData.isEmpty()) {

            itinerariesTable.setItems(itinerariesData);
        }

        if (itinerariesTable.isFocused() && itinerariesTable.getFocusModel().getFocusedItem() != null) {
            ObservableList<Tratta> legsData = FXCollections.observableArrayList();
            legsData.addAll(fetchLegsFromDatabase(conn, itinerariesTable.getFocusModel().getFocusedItem().getNumeroDiVolo()));
            if (!legsData.isEmpty()) {
                flightLegsTable.setItems(legsData);
            }
        }

        ObservableList<Volo> flightsData = FXCollections.observableArrayList();
        flightsData.addAll(fetchFlightsFromDatabase(conn));
        if (!flightsData.isEmpty()) {
            flightsTable.setItems(flightsData);
        }

        ObservableList<Rifornimento> refuelsData = FXCollections.observableArrayList();
        refuelsData.addAll(fetchRefuelingFromDatabase(conn));
        if (!refuelsData.isEmpty()) {
            refuelsTable.setItems(refuelsData);
        }

    }


    /////////////////////////////////////// Data fetching for tables /////////////////////////////////////////////////

    private List<Aeroporto> fetchAirportsFromDatabase(Connection conn) {
        List<Aeroporto> aeroporti;
        try {
            aeroporti = Aeroporto.mapTo(DAUtility.executeQuery(conn, Queries.SelectAll.AEROPORTI));
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return aeroporti;
    }

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

    private List<Rifornimento> fetchRefuelingFromDatabase(Connection conn) {
        List<Rifornimento> rifornimenti;
        try {
            rifornimenti = Rifornimento.mapTo(DAUtility.executeQuery(conn, Queries.SelectAll.RIFORNIMENTI));
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return rifornimenti;
    }

    private List<Tratta> fetchLegsFromDatabase(Connection conn, int selectedItem) {
        List<Tratta> tratte;
        try {
            String query = "SELECT * FROM tratte WHERE NumeroDiVolo = ?";
            tratte = Tratta.mapTo(DAUtility.executeQuery(conn, query, selectedItem));
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return tratte;
    }

    private List<Itinerario> fetchItinerariFromDatabase(Connection conn) {
        List<Itinerario> itinerari;
        try {
            itinerari = Itinerario.mapTo(DAUtility.executeQuery(conn, Queries.SelectAll.ITINERARI));
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return itinerari;
    }
}
