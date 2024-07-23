package org.application.dbairline.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

public class MainWindowController implements Initializable {

    private WindowManager manager;

    @FXML    private TableColumn<?, ?> aircraftsBuilderColumn;
    @FXML    private TableColumn<?, ?> aircraftsConfigurationIdColumn;
    @FXML    private TableColumn<?, ?> aircraftsRegistrationCodeColumn;
    @FXML    private TableColumn<?, ?> aircraftsTypeColumn;
    @FXML    private TableView<Aereo> aircrafstTable;


    @FXML    private TableColumn<?, ?> airportsCityColumn;
    @FXML    private TableColumn<?, ?> airportsCommercialNameColumn;
    @FXML    private TableColumn<?, ?> airportsCountryColumn;
    @FXML    private TableColumn<?, ?> airportsFaxNumberColumn;
    @FXML    private TableColumn<?, ?> airportsIATACodeColumn;
    @FXML    private TableColumn<?, ?> airportsICAOCodeColumn;
    @FXML    private TableColumn<?, ?> airportsManagerColumn;
    @FXML    private TableColumn<?, ?> airportsManagerContactColumn;
    @FXML    private TableColumn<?, ?> airportsTelephoneColumn;
    @FXML    private TableView<Aeroporto> airportsTable;

    //Travel classes Table
    @FXML    private TableColumn<?, ?> travelClassesIdColumn;
    @FXML    private TableColumn<?, ?> travelClassesNameColumn;
    @FXML    private TableView<Classe> travelClassesTable;

    // Seats Configurations Table
    @FXML    private TableColumn<?, ?> seatConfigurationsIdColumn;
    @FXML    private TableColumn<?, ?> seatConfigurationsCapacityColumn;
    @FXML    private TableColumn<?, ?> seatConfigurationsNameColumn;
    @FXML    private TableView<Configurazione> seatsConfigurationsTable;

    // Employees Table
    @FXML    private TableColumn<?, ?> employeesAddressColumn;
    @FXML    private TableColumn<?, ?> employeesCityColumn;
    @FXML    private TableColumn<?, ?> employeesIdColumn;
    @FXML    private TableColumn<?, ?> employeesNameColumn;
    @FXML    private TableColumn<?, ?> employeesNationalityColumn;
    @FXML    private TableColumn<?, ?> employeesRankColumn;
    @FXML    private TableColumn<?, ?> employeesRoleColumn;
    @FXML    private TableColumn<?, ?> employeesSalaryColumn;
    @FXML    private TableColumn<?, ?> employeesSurnameColumn;
    @FXML    private TableColumn<?, ?> employeesTelephoneNumberColumn;
    @FXML    private TableView<Dipendente> employeesTable;

    // Fares Table
    @FXML    private TableColumn<?, ?> faresFlightClassColumn;
    @FXML    private TableColumn<?, ?> faresEndDateColumn;
    @FXML    private TableColumn<?, ?> faresFlightNumberColumn;
    @FXML    private TableColumn<?, ?> faresIdColumn;
    @FXML    private TableColumn<?, ?> faresLegNumberColumn;
    @FXML    private TableColumn<?, ?> faresStartDateColumn;
    @FXML    private TableColumn<?, ?> faresValueColumn;
    @FXML    private TableView<Tariffa> faresTable;

    // Flights Table
    @FXML    private TableColumn<?, ?> flightAircraftCol;
    @FXML    private TableColumn<?, ?> flightAircraftConfigCol;
    @FXML    private TableColumn<?, ?> flightAltAirportCol;
    @FXML    private TableColumn<?, ?> flightArrCol;
    @FXML    private TableColumn<?, ?> flightCancelledCol;
    @FXML    private TableColumn<?, ?> flightDepCol;
    @FXML    private TableColumn<?, ?> flightEffDepTimeCol;
    @FXML    private TableColumn<?, ?> flightEffDesTimeCol;
    @FXML    private TableColumn<?, ?> flightFlightNumCol;
    @FXML    private TableColumn<?, ?> flightIdCol;
    @FXML    private TableColumn<?, ?> flightProgDepTimeCol;
    @FXML    private TableColumn<?, ?> flightProgDesTimeCol;
    @FXML    private TableView<Volo> flightsTable;

    // Itineraries Table
    @FXML    private TableColumn<?, ?> itinerariesFlightNumCol;
    @FXML    private TableColumn<?, ?> itinerariesFlightTypeCol;
    @FXML    private TableView<Itinerario> itinerariesTable;

    // Flight Legs Table
    @FXML    private TableColumn<?, ?> flightLegsArrivalColumn;
    @FXML    private TableColumn<?, ?> flightLegsDepartureColumn;
    @FXML    private TableColumn<?, ?> flightLegslegNumberColumn;
    @FXML    private TableView<Tratta> flightLegsTable;

    // table
    @FXML    private TableColumn<?, ?> purchasesDateColumn;
    @FXML    private TableColumn<?, ?> purchasesPurchaserEmailColumn;
    @FXML    private TableColumn<?, ?> purchasesPurchaserNameColumn;
    @FXML    private TableColumn<?, ?> purchasesPurchaserTelephoneNumberColumn;
    @FXML    private TableColumn<?, ?> purchasesIdColumn;
    @FXML    private TableColumn<?, ?> purchasesImportColumn;
    @FXML    private TableView<Acquisto> purchasesTable;

    // table
    @FXML    private TableColumn<?, ?> refuelsCostColumn;
    @FXML    private TableColumn<?, ?> refuelsFuelTypeColumn;
    @FXML    private TableColumn<?, ?> refuelsFlightIdColumn;
    @FXML    private TableColumn<?, ?> refuelsQuantityColumn;
    @FXML    private TableView<Rifornimento> refuelsTable;

    // table
    @FXML    private TableColumn<?, ?> schedulesSeatConfigurationIdColumn;
    @FXML    private TableColumn<?, ?> schedulesDayColumn;
    @FXML    private TableColumn<?, ?> schedulesFlightNumberColumn;
    @FXML    private TableColumn<?, ?> schedulesFlightTimeColumn;
    @FXML    private TableColumn<?, ?> schedulesDepartureTimeColumn;
    @FXML    private TableColumn<?, ?> schedulesLegNumberColumn;
    @FXML    private TableView<Programma> schedulesTable;

    // table
    @FXML    private TableColumn<?, ?> seatsClassNameColumn;
    @FXML    private TableColumn<?, ?> seatsNumberColumn;
    @FXML    private TableView<Posto> seatsTable;

    // table
    @FXML    private TableColumn<?, ?> ticketsConfigurationIdColumn;
    @FXML    private TableColumn<?, ?> ticketsFlightClassNameColumn;
    @FXML    private TableColumn<?, ?> ticketsFlightIdColumn;
    @FXML    private TableColumn<?, ?> ticketsFlightNumberColumn;
    @FXML    private TableColumn<?, ?> ticketsIdColumn;
    @FXML    private TableColumn<?, ?> ticketsSeatNumberColumn;
    @FXML    private TableColumn<?, ?> ticketsPriceColumn;
    @FXML    private TableColumn<?, ?> ticketsPaxNameColumn;
    @FXML    private TableView<Biglietto> ticketsTable;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boolean res = DAUtility.setCredentials("jdbc:mysql://localhost:3306/airline", "root", "admin");
        if (!res) {
            System.out.println("Credentials can't be set");
            System.exit(1);
        }

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

    public void setManager(WindowManager windowManager) {
        this.manager = windowManager;
    }

    @FXML
    private void insertAircraft(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/InsertAircraftWindow.fxml"));
        Stage stage = manager.createPopup(loader);
        InsertAircraftWindowController controller = loader.getController();
        controller.prepare(this);
        stage.showAndWait();
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
    private void insertEmployee(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/InsertEmployeeWindow.fxml"));
        Stage stage = manager.createPopup(loader);
        InsertEmployeeWindowController controller = loader.getController();
        controller.prepare(this);
        stage.showAndWait();
    }

    @FXML
    private void insertFare(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/InsertFareWindow.fxml"));
        Stage stage = manager.createPopup(loader);
        InsertFareWindowController controller = loader.getController();
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
    private void insertFlightClass(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/InsertFlightClassWindow.fxml"));
        Stage stage = manager.createPopup(loader);
        InsertFlightClasslWindowController controller = loader.getController();
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
    private void insertPurchase(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/InsertPurchaseWindow.fxml"));
        Stage stage = manager.createPopup(loader);
        InsertPurchaseWindowController controller = loader.getController();
        controller.prepare(this);
        stage.showAndWait();
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
    private void insertSchedule(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/InsertScheduleWindow.fxml"));
        Stage stage = manager.createPopup(loader);
        InsertScheduleWindowController controller = loader.getController();
        controller.prepare(this);
        stage.showAndWait();
    }

    @FXML
    private void insertSeatConfig(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/InsertConfigurationWindow.fxml"));
        Stage stage = manager.createPopup(loader);
        InsertConfigurationWindowController controller = loader.getController();
        controller.prepare(this);
        stage.showAndWait();
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


    private void setupAircraftTable() {
        aircraftsBuilderColumn.setCellValueFactory(new PropertyValueFactory<>("costruttore"));
        aircraftsConfigurationIdColumn.setCellValueFactory(new PropertyValueFactory<>("idConfigurazione"));
        aircraftsRegistrationCodeColumn.setCellValueFactory(new PropertyValueFactory<>("codiceRegistrazione"));
        aircraftsTypeColumn.setCellValueFactory(new PropertyValueFactory<>("modello"));
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

    private void setupEmployeeTable() {
        employeesAddressColumn.setCellValueFactory(new PropertyValueFactory<>("indVia"));
        employeesCityColumn.setCellValueFactory(new PropertyValueFactory<>("indCitta"));
        employeesIdColumn.setCellValueFactory(new PropertyValueFactory<>("idDipendente"));
        employeesNameColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        employeesNationalityColumn.setCellValueFactory(new PropertyValueFactory<>("nazionalita"));
        employeesRankColumn.setCellValueFactory(new PropertyValueFactory<>("grado"));
        employeesRoleColumn.setCellValueFactory(new PropertyValueFactory<>("ruolo"));
        employeesSalaryColumn.setCellValueFactory(new PropertyValueFactory<>("stipendio"));
        employeesSurnameColumn.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        employeesTelephoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));
    }

    private void setupFareTable() {
        faresFlightClassColumn.setCellValueFactory(new PropertyValueFactory<>("idClasse"));
        faresEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("dataFine"));
        faresFlightNumberColumn.setCellValueFactory(new PropertyValueFactory<>("numeroVolo"));
        faresIdColumn.setCellValueFactory(new PropertyValueFactory<>("idTariffa"));
        faresLegNumberColumn.setCellValueFactory(new PropertyValueFactory<>("numeroTratta"));
        faresStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("dataInizio"));
        faresValueColumn.setCellValueFactory(new PropertyValueFactory<>("tariffa"));
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

    private void setupPurchasesTable() {
        purchasesDateColumn.setCellValueFactory(new PropertyValueFactory<>("dataAcquisto"));
        purchasesPurchaserEmailColumn.setCellValueFactory(new PropertyValueFactory<>("acqEmail"));
        purchasesPurchaserNameColumn.setCellValueFactory(new PropertyValueFactory<>("acqNomeCompleto"));
        purchasesPurchaserTelephoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("acqTelefono"));
        purchasesIdColumn.setCellValueFactory(new PropertyValueFactory<>("idAcquisto"));
        purchasesImportColumn.setCellValueFactory(new PropertyValueFactory<>("importo"));
    }

    private void setupRefuelsTable() {
        refuelsCostColumn.setCellValueFactory(new PropertyValueFactory<>("costo"));
        refuelsFuelTypeColumn.setCellValueFactory(new PropertyValueFactory<>("tipoCarburante"));
        refuelsFlightIdColumn.setCellValueFactory(new PropertyValueFactory<>("idVolo"));
        refuelsQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantita"));
    }

    private void setupSchedulesTable() {
        schedulesSeatConfigurationIdColumn.setCellValueFactory(new PropertyValueFactory<>("idConfigurazione"));
        schedulesDayColumn.setCellValueFactory(new PropertyValueFactory<>("giorno"));
        schedulesFlightNumberColumn.setCellValueFactory(new PropertyValueFactory<>("numeroDiVolo"));
        schedulesFlightTimeColumn.setCellValueFactory(new PropertyValueFactory<>("tempoDIPercorrenza"));
        schedulesDepartureTimeColumn.setCellValueFactory(new PropertyValueFactory<>("oraPartenza"));
        schedulesLegNumberColumn.setCellValueFactory(new PropertyValueFactory<>("numeroTratta"));
    }

    private void setupSeatClassTable() {
        travelClassesIdColumn.setCellValueFactory(new PropertyValueFactory<>("idClasse"));
        travelClassesNameColumn.setCellValueFactory(new PropertyValueFactory<>("nomeClasse"));
    }

    private void setupSeatConfigTable() {
        seatConfigurationsIdColumn.setCellValueFactory(new PropertyValueFactory<>("idConfigurazione"));
        seatConfigurationsCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("numMaxPosti"));
        seatConfigurationsNameColumn.setCellValueFactory(new PropertyValueFactory<>("nomeConfigurazione"));
    }

    private void setupSeatsTable() {
        seatsClassNameColumn.setCellValueFactory(new PropertyValueFactory<>("nomeClasse"));
        seatsNumberColumn.setCellValueFactory(new PropertyValueFactory<>("numeroPosto"));
    }

    private void setupTicketsTable() {
        ticketsConfigurationIdColumn.setCellValueFactory(new PropertyValueFactory<>("idConfigurazione"));
        ticketsFlightClassNameColumn.setCellValueFactory(new PropertyValueFactory<>("nomeClasse"));
        ticketsFlightIdColumn.setCellValueFactory(new PropertyValueFactory<>("idVolo"));
        ticketsFlightNumberColumn.setCellValueFactory(new PropertyValueFactory<>("numeroDiVolo"));
        ticketsIdColumn.setCellValueFactory(new PropertyValueFactory<>("idBiglietto"));
        ticketsSeatNumberColumn.setCellValueFactory(new PropertyValueFactory<>("numeroPosto"));
        ticketsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("prezzo"));
        ticketsPaxNameColumn.setCellValueFactory(new PropertyValueFactory<>("nomePasseggero"));
    }

    private void configureTables() {
        setupAircraftTable();
        setupAirportTable();
        setupEmployeeTable();
        setupFareTable();
        setupFlightLegsTable();
        setupFlightsTable();
        setupItinerariesTable();
        setupPurchasesTable();
        setupSchedulesTable();
        setupSeatClassTable();
        setupSeatConfigTable();
        setupSeatsTable();

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

        seatsConfigurationsTable.setRowFactory(tv -> {
            ObservableList<Posto> seatsData = FXCollections.observableArrayList();
            TableRow<Configurazione> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    seatsTable.getItems().clear();
                    Configurazione rowData = row.getItem();
                    try {
                        Connection conn = DAUtility.getConnection();
                        String query = "SELECT p.IdConfigurazione, p.NumeroPosto, p.IdClasse, c.NomeClasse\n" +
                                       "FROM posti p, classi c\n" +
                                       "WHERE p.IdClasse = c.IdClasse\n" +
                                       "AND p.IdConfigurazione = ?";
                        seatsData.addAll(Posto.mapTo(DAUtility.executeQuery(conn, query, rowData.getIdConfigurazione())));
                        conn.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    seatsTable.setItems(seatsData);
                }
            });
            return row;
        });
    }

    private void populateTables(Connection conn) {

        ObservableList<Aereo> aircraftData = FXCollections.observableArrayList();
        aircraftData.addAll(fetchAircraftsFromDatabase(conn));
        if (!aircraftData.isEmpty()) {
            aircrafstTable.setItems(aircraftData);
        }

        ObservableList<Aeroporto> airportsData = FXCollections.observableArrayList();
        airportsData.addAll(fetchAirportsFromDatabase(conn));
        if (!airportsData.isEmpty()) {
            airportsTable.setItems(airportsData);
        }

        ObservableList<Configurazione> configData = FXCollections.observableArrayList();
        configData.addAll(fetchConfigurationsFromDatabase(conn));
        if (!configData.isEmpty()) {
            seatsConfigurationsTable.setItems(configData);
        }

        /*
        if (configTable.getFocusModel().getFocusedItem() != null) {
            ObservableList<Posto> seatData = FXCollections.observableArrayList();
            seatData.addAll(fetchSeatsFromDatabase(conn, configTable.getFocusModel().getFocusedItem().getIdConfigurazione()));
            if (!seatData.isEmpty()) {
                seatTable.setItems(seatData);
            }
        }
        */

        ObservableList<Classe> flightClassesData = FXCollections.observableArrayList();
        flightClassesData.addAll(fetchFlightClassesFromDatabase(conn));
        if (!flightClassesData.isEmpty()) {
            travelClassesTable.setItems(flightClassesData);
        }

        ObservableList<Acquisto> acquistiData = FXCollections.observableArrayList();
        acquistiData.addAll(fetchAcquistiFromDatabase(conn));
        if (!acquistiData.isEmpty()) {
            purchasesTable.setItems(acquistiData);
        }

        ObservableList<Itinerario> itinerariesData = FXCollections.observableArrayList();
        itinerariesData.addAll(fetchItinerariFromDatabase(conn));
        if (!itinerariesData.isEmpty()) {

            itinerariesTable.setItems(itinerariesData);
        }

        if (itinerariesTable.getFocusModel().getFocusedItem() != null) {
            ObservableList<Tratta> legsData = FXCollections.observableArrayList();
            legsData.addAll(fetchLegsFromDatabase(conn, itinerariesTable.getFocusModel().getFocusedItem().getNumeroDiVolo()));
            if (!legsData.isEmpty()) {
                flightLegsTable.setItems(legsData);
            }
        }
        ObservableList<Dipendente> employeesData = FXCollections.observableArrayList();
        employeesData.addAll(fetchEmployeesFromDatabase(conn));
        if (!employeesData.isEmpty()) {
            employeesTable.setItems(employeesData);
        }

        ObservableList<Tariffa> faresData = FXCollections.observableArrayList();
        faresData.addAll(fetchFaresFromDatabase(conn));
        if (!faresData.isEmpty()) {
            faresTable.setItems(faresData);
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

        ObservableList<Programma> schedulesData = FXCollections.observableArrayList();
        schedulesData.addAll(fetchScheduleFromDatabase(conn));
        if (!schedulesData.isEmpty()) {
            schedulesTable.setItems(schedulesData);
        }

        ObservableList<Biglietto> ticketsData = FXCollections.observableArrayList();
        ticketsData.addAll(fetchTicketsFromDatabase(conn));
        if (!ticketsData.isEmpty()) {
            ticketsTable.setItems(ticketsData);
        }


    }

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

    private List<Configurazione> fetchConfigurationsFromDatabase(Connection conn) {
        List<Configurazione> configurazioni;
        try {
            configurazioni = Configurazione.mapTo(DAUtility.executeQuery(conn, Queries.SelectAll.CONFIGURAZIONI));
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return configurazioni;
    }

    private List<Classe> fetchFlightClassesFromDatabase(Connection conn) {
        List<Classe> classi;
        try {
            classi = Classe.mapTo(DAUtility.executeQuery(conn, Queries.SelectAll.CLASSI));
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return classi;
    }

    private List<Dipendente> fetchEmployeesFromDatabase(Connection conn) {
        List<Dipendente> dipendenti;
        try {
            dipendenti = Dipendente.mapTo(DAUtility.executeQuery(conn, Queries.SelectAll.DIPENDENTI));
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return dipendenti;
    }

    private List<Tariffa> fetchFaresFromDatabase(Connection conn) {
        List<Tariffa> tariffe;
        try {
            tariffe = Tariffa.mapTo(DAUtility.executeQuery(conn, Queries.SelectAll.TARIFFE));
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return tariffe;
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

    private List<Programma> fetchScheduleFromDatabase(Connection conn) {
        List<Programma> programmi;
        try {
            programmi = Programma.mapTo(DAUtility.executeQuery(conn, Queries.SelectAll.PROGRAMMI));
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return programmi;
    }

    /*
    private List<Posto> fetchSeatsFromDatabase(Connection conn, int idConfigurazione) {
        List<Posto> seats;
        try {
            seats = Posto.mapTo(DAUtility.executeQuery(conn, Queries.SelectAll.PROGRAMMI));
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return seats;
    }
    */

    private List<Biglietto> fetchTicketsFromDatabase(Connection conn) {
        List<Biglietto> biglietti;
        try {
            String selectTicketsWithFlightClassNames = "SELECT B.IdBiglietto, B.NumeroDiVolo, B.NomePasseggero, \n" +
                                                       "B.IdAcquisto, B.Prezzo, B.IdVolo, B.IdConfigurazione, \n" +
                                                       "B.NumeroPosto, C.NomeClasse \n" +
                                                       "FROM BIGLIETTI B \n" +
                                                       "JOIN POSTI P ON B.IdConfigurazione = P.IdConfigurazione \n" +
                                                       "JOIN CLASSI C ON P.IdClasse = C.IdClasse";
            biglietti = Biglietto.mapToWithFlightClassName(DAUtility.executeQuery(conn, selectTicketsWithFlightClassNames));
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return biglietti;
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

    private List<Aereo> fetchAircraftsFromDatabase(Connection conn) {
        List<Aereo> aerei;
        try {
            aerei = Aereo.mapTo(DAUtility.executeQuery(conn, Queries.SelectAll.AEREI));
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return aerei;
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
