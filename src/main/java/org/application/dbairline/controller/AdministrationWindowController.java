package org.application.dbairline.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
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
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

public class AdministrationWindowController extends WindowController {

    private GUIManager manager;

    @FXML
    private TableColumn<?, ?> aircraftsBuilderColumn;
    @FXML
    private TableColumn<?, ?> aircraftsConfigurationIdColumn;
    @FXML
    private TableColumn<?, ?> aircraftsRegistrationCodeColumn;
    @FXML
    private TableColumn<?, ?> aircraftsTypeColumn;
    @FXML
    private TableView<Aereo> aircrafstTable;

    //Travel classes Table
    @FXML
    private TableColumn<?, ?> travelClassesIdColumn;
    @FXML
    private TableColumn<?, ?> travelClassesNameColumn;
    @FXML
    private TableView<Classe> travelClassesTable;

    // Seats Configurations Table
    @FXML
    private TableColumn<?, ?> seatConfigurationsIdColumn;
    @FXML
    private TableColumn<?, ?> seatConfigurationsCapacityColumn;
    @FXML
    private TableColumn<?, ?> seatConfigurationsNameColumn;
    @FXML
    private TableView<Configurazione> seatsConfigurationsTable;

    // Employees Table
    @FXML
    private TableColumn<?, ?> employeesAddressColumn;
    @FXML
    private TableColumn<?, ?> employeesCityColumn;
    @FXML
    private TableColumn<?, ?> employeesIdColumn;
    @FXML
    private TableColumn<?, ?> employeesNameColumn;
    @FXML
    private TableColumn<?, ?> employeesNationalityColumn;
    @FXML
    private TableColumn<?, ?> employeesRankColumn;
    @FXML
    private TableColumn<?, ?> employeesRoleColumn;
    @FXML
    private TableColumn<?, ?> employeesSalaryColumn;
    @FXML
    private TableColumn<?, ?> employeesSurnameColumn;
    @FXML
    private TableColumn<?, ?> employeesTelephoneNumberColumn;
    @FXML
    private TableView<Dipendente> employeesTable;

    // Fares Table
    @FXML
    private TableColumn<?, ?> faresFlightClassColumn;
    @FXML
    private TableColumn<?, ?> faresEndDateColumn;
    @FXML
    private TableColumn<?, ?> faresFlightNumberColumn;
    @FXML
    private TableColumn<?, ?> faresIdColumn;
    @FXML
    private TableColumn<?, ?> faresLegNumberColumn;
    @FXML
    private TableColumn<?, ?> faresStartDateColumn;
    @FXML
    private TableColumn<?, ?> faresValueColumn;
    @FXML
    private TableView<Tariffa> faresTable;

    // table
    @FXML
    private TableColumn<?, ?> schedulesSeatConfigurationIdColumn;
    @FXML
    private TableColumn<?, ?> schedulesDayColumn;
    @FXML
    private TableColumn<?, ?> schedulesFlightNumberColumn;
    @FXML
    private TableColumn<?, ?> schedulesDepartureTimeColumn;
    @FXML
    private TableColumn<?, ?> schedulesLegNumberColumn;
    @FXML
    private TableView<Programma> schedulesTable;

    // table
    @FXML
    private TableColumn<?, ?> seatsClassNameColumn;
    @FXML
    private TableColumn<?, ?> seatsNumberColumn;
    @FXML
    private TableView<Posto> seatsTable;

    @FXML
    private TableView<List<Object>> customizableTable;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;

    public static class DataResult {

        private final List<String> columnNames;
        private final List<List<Object>> data;

        public DataResult(List<String> columnNames, List<List<Object>> data) {
            this.columnNames = columnNames;
            this.data = data;
        }

        public int getNumColumns() {
            return columnNames.size();
        }

        public String getColumnName(int index) {
            return columnNames.get(index);
        }

        public int getNumRows() {
            return data.size();
        }

        public Object getData(int column, int row) {
            return data.get(row).get(column);
        }

        public List<List<Object>> getData() {
            return data;
        }
    }

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
    private void insertAircraft(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/InsertAircraftWindow.fxml"));
        Stage stage = manager.createPopup(loader);
        InsertAircraftWindowController controller = loader.getController();
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
    private void insertFlightClass(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/InsertFlightClassWindow.fxml"));
        Stage stage = manager.createPopup(loader);
        InsertFlightClasslWindowController controller = loader.getController();
        controller.prepare(this);
        stage.showAndWait();
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
    private void rankingFrequentedItineraries(MouseEvent event) {
        customizableTable.getColumns().clear();

        Timestamp startDate;
        Timestamp endDate;
        try {
            startDate = Timestamp.valueOf(startDatePicker.getValue().atStartOfDay());
            endDate = Timestamp.valueOf(endDatePicker.getValue().plusDays(1).atStartOfDay());

            if (startDate.after(endDate)) {
                throw new IllegalArgumentException();
            }

        } catch (NullPointerException e) {
            showErrorAlert("Errore di input",
                    "Controllare che sia la data di inizio e di fine siano selezionate.");
            return;
        } catch (IllegalArgumentException e) {
            showErrorAlert("Errore di input",
                    "La data di fine deve essere successiva alla data di inizio.");
            return;
        }

        try {
            Connection connection = DAUtility.getConnection();
            String query = "SELECT i.NumeroDiVolo, COUNT(b.IdBiglietto) AS NumeroBigliettiVenduti " +
                           "FROM Itinerari i " +
                           "JOIN Voli v ON i.NumeroDiVolo = v.NumeroDiVolo " +
                           "JOIN Biglietti b ON v.IdVolo = b.IdVolo " +
                           "WHERE v.DataOraPartenzaProg BETWEEN ? AND ? " +
                           "GROUP BY i.NumeroDiVolo " +
                           "ORDER BY NumeroBigliettiVenduti DESC;";

            DataResult data = DAUtility.getAllData(DAUtility.executeQuery(connection, query, startDate, endDate));

            for (int i = 0; i < data.getNumColumns(); i++) {
                TableColumn<List<Object>, Object> column = new TableColumn<>(data.getColumnName(i));
                int columnIndex = i;
                column.setCellValueFactory(cellData ->
                        new SimpleObjectProperty<>(cellData.getValue().get(columnIndex)));
                customizableTable.getColumns().add(column);
            }

            customizableTable.getItems().setAll(data.getData());

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void occupancyRateForClass(MouseEvent event) {
        customizableTable.getColumns().clear();

        Timestamp startDate;
        Timestamp endDate;
        try {
            startDate = Timestamp.valueOf(startDatePicker.getValue().atStartOfDay());
            endDate = Timestamp.valueOf(endDatePicker.getValue().plusDays(1).atStartOfDay());

            if (startDate.after(endDate)) {
                throw new IllegalArgumentException();
            }

        } catch (NullPointerException e) {
            showErrorAlert("Errore di input",
                    "Controllare che sia la data di inizio e di fine siano selezionate.");
            return;
        } catch (IllegalArgumentException e) {
            showErrorAlert("Errore di input",
                    "La data di fine deve essere successiva alla data di inizio.");
            return;
        }

        try {
            Connection connection = DAUtility.getConnection();
            String query = "SELECT c.NomeClasse, " +
                           "SUM(CASE WHEN b.NumeroPosto IS NOT NULL THEN 1 ELSE 0 END) / COUNT(p.NumeroPosto) * 100 " +
                           "AS TassoOccupazione " +
                           "FROM Configurazioni cfg " +
                           "JOIN Posti p ON cfg.IdConfigurazione = p.IdConfigurazione " +
                           "JOIN Classi c ON p.IdClasse = c.IdClasse " +
                           "LEFT JOIN Biglietti b ON p.NumeroPosto = b.NumeroPosto " +
                           "AND cfg.IdConfigurazione = b.IdConfigurazione " +
                           "LEFT JOIN Voli v ON b.IdVolo = v.IdVolo " +
                           "WHERE v.DataOraPartenzaProg BETWEEN ? AND ? " +
                           "GROUP BY c.NomeClasse;";

            DataResult data = DAUtility.getAllData(DAUtility.executeQuery(connection, query, startDate, endDate));

            for (int i = 0; i < data.getNumColumns(); i++) {
                TableColumn<List<Object>, Object> column = new TableColumn<>(data.getColumnName(i));
                int columnIndex = i;
                column.setCellValueFactory(cellData ->
                        new SimpleObjectProperty<>(cellData.getValue().get(columnIndex)));
                customizableTable.getColumns().add(column);
            }

            customizableTable.getItems().setAll(data.getData());

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void averageTicketsPerRoute(MouseEvent event) {
        customizableTable.getColumns().clear();

        Timestamp startDate;
        Timestamp endDate;
        try {
            startDate = Timestamp.valueOf(startDatePicker.getValue().atStartOfDay());
            endDate = Timestamp.valueOf(endDatePicker.getValue().plusDays(1).atStartOfDay());

            if (startDate.after(endDate)) {
                throw new IllegalArgumentException();
            }

        } catch (NullPointerException e) {
            showErrorAlert("Errore di input",
                    "Controllare che sia la data di inizio e di fine siano selezionate.");
            return;
        } catch (IllegalArgumentException e) {
            showErrorAlert("Errore di input",
                    "La data di fine deve essere successiva alla data di inizio.");
            return;
        }

        try {
            Connection connection = DAUtility.getConnection();
            String query = "SELECT v.IdVolo, COUNT(b.IdBiglietto) / COUNT(DISTINCT v.IdVolo) AS MediaBigliettiVenduti " +
                           "FROM Voli v " +
                           "LEFT JOIN Biglietti b ON v.IdVolo = b.IdVolo " +
                           "WHERE v.DataOraPartenzaProg BETWEEN ? AND ? " +
                           "GROUP BY v.IdVolo";

            DataResult data = DAUtility.getAllData(DAUtility.executeQuery(connection, query, startDate, endDate));

            for (int i = 0; i < data.getNumColumns(); i++) {
                TableColumn<List<Object>, Object> column = new TableColumn<>(data.getColumnName(i));
                int columnIndex = i;
                column.setCellValueFactory(cellData ->
                        new SimpleObjectProperty<>(cellData.getValue().get(columnIndex)));
                customizableTable.getColumns().add(column);
            }

            customizableTable.getItems().setAll(data.getData());

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void revenuePerRoute(MouseEvent event) {
        customizableTable.getColumns().clear();

        Timestamp startDate;
        Timestamp endDate;
        try {
            startDate = Timestamp.valueOf(startDatePicker.getValue().atStartOfDay());
            endDate = Timestamp.valueOf(endDatePicker.getValue().plusDays(1).atStartOfDay());

            if (startDate.after(endDate)) {
                throw new IllegalArgumentException();
            }

        } catch (NullPointerException e) {
            showErrorAlert("Errore di input",
                    "Controllare che sia la data di inizio e di fine siano selezionate.");
            return;
        } catch (IllegalArgumentException e) {
            showErrorAlert("Errore di input",
                    "La data di fine deve essere successiva alla data di inizio.");
            return;
        }

        try {
            Connection connection = DAUtility.getConnection();
            String query = "SELECT t.NumeroTratta, SUM(a.Importo) AS RicaviTotali " +
                           "FROM Tratte t " +
                           "JOIN Voli v ON t.NumeroTratta = v.NumeroTratta " +
                           "JOIN Biglietti b ON v.IdVolo = b.IdVolo " +
                           "JOIN Acquisti a ON b.IdAcquisto = a.IdAcquisto " +
                           "WHERE v.DataOraPartenzaProg BETWEEN ? AND ? " +
                           "GROUP BY t.NumeroTratta";

            DataResult data = DAUtility.getAllData(DAUtility.executeQuery(connection, query, startDate, endDate));

            for (int i = 0; i < data.getNumColumns(); i++) {
                TableColumn<List<Object>, Object> column = new TableColumn<>(data.getColumnName(i));
                int columnIndex = i;
                column.setCellValueFactory(cellData ->
                        new SimpleObjectProperty<>(cellData.getValue().get(columnIndex)));
                customizableTable.getColumns().add(column);
            }

            customizableTable.getItems().setAll(data.getData());

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void totalRevenue(MouseEvent event) {
        customizableTable.getColumns().clear();

        Timestamp startDate;
        Timestamp endDate;
        try {
            startDate = Timestamp.valueOf(startDatePicker.getValue().atStartOfDay());
            endDate = Timestamp.valueOf(endDatePicker.getValue().plusDays(1).atStartOfDay());

            if (startDate.after(endDate)) {
                throw new IllegalArgumentException();
            }

        } catch (NullPointerException e) {
            showErrorAlert("Errore di input",
                    "Controllare che sia la data di inizio e di fine siano selezionate.");
            return;
        } catch (IllegalArgumentException e) {
            showErrorAlert("Errore di input",
                    "La data di fine deve essere successiva alla data di inizio.");
            return;
        }

        try {
            Connection connection = DAUtility.getConnection();
            String query = "SELECT SUM(b.Prezzo) AS Ricavi_Totali " +
                           "FROM BIGLIETTI b " +
                           "JOIN ACQUISTI a ON b.IdAcquisto = a.IdAcquisto " +
                           "WHERE a.DataAcquisto BETWEEN ? AND ?";

            DataResult data = DAUtility.getAllData(DAUtility.executeQuery(connection, query, startDate, endDate));

            for (int i = 0; i < data.getNumColumns(); i++) {
                TableColumn<List<Object>, Object> column = new TableColumn<>(data.getColumnName(i));
                int columnIndex = i;
                column.setCellValueFactory(cellData ->
                        new SimpleObjectProperty<>(cellData.getValue().get(columnIndex)));
                customizableTable.getColumns().add(column);
            }

            customizableTable.getItems().setAll(data.getData());

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void operatingCosts(MouseEvent event) {
        customizableTable.getColumns().clear();

        Timestamp startDate;
        Timestamp endDate;
        try {
            startDate = Timestamp.valueOf(startDatePicker.getValue().atStartOfDay());
            endDate = Timestamp.valueOf(endDatePicker.getValue().plusDays(1).atStartOfDay());

            if (startDate.after(endDate)) {
                throw new IllegalArgumentException();
            }

        } catch (NullPointerException e) {
            showErrorAlert("Errore di input",
                    "Controllare che sia la data di inizio e di fine siano selezionate.");
            return;
        } catch (IllegalArgumentException e) {
            showErrorAlert("Errore di input",
                    "La data di fine deve essere successiva alla data di inizio.");
            return;
        }

        try {
            Connection connection = DAUtility.getConnection();
            String query = "SELECT SUM(r.Costo) AS Costo_Operativo_Totale " +
                           "FROM RIFORNIMENTI r " +
                           "JOIN VOLI v ON r.IdVolo = v.IdVolo " +
                           "WHERE v.DataOraPartenzaProg BETWEEN ? AND ?";

            DataResult data = DAUtility.getAllData(DAUtility.executeQuery(connection, query, startDate, endDate));

            for (int i = 0; i < data.getNumColumns(); i++) {
                TableColumn<List<Object>, Object> column = new TableColumn<>(data.getColumnName(i));
                int columnIndex = i;
                column.setCellValueFactory(cellData ->
                        new SimpleObjectProperty<>(cellData.getValue().get(columnIndex)));
                customizableTable.getColumns().add(column);
            }

            customizableTable.getItems().setAll(data.getData());

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void netProfit(MouseEvent event) {
        customizableTable.getColumns().clear();

        Timestamp startDate;
        Timestamp endDate;
        try {
            startDate = Timestamp.valueOf(startDatePicker.getValue().atStartOfDay());
            endDate = Timestamp.valueOf(endDatePicker.getValue().plusDays(1).atStartOfDay());

            if (startDate.after(endDate)) {
                throw new IllegalArgumentException();
            }

        } catch (NullPointerException e) {
            showErrorAlert("Errore di input",
                    "Controllare che sia la data di inizio e di fine siano selezionate.");
            return;
        } catch (IllegalArgumentException e) {
            showErrorAlert("Errore di input",
                    "La data di fine deve essere successiva alla data di inizio.");
            return;
        }

        try {
            Connection connection = DAUtility.getConnection();
            String query = "SELECT (Ricavi_Totali - Costo_Operativo_Totale) AS Profitto_Netto " +
                           "FROM ( " +
                           "SELECT SUM(b.Prezzo) AS Ricavi_Totali " +
                           "FROM BIGLIETTI b " +
                           "JOIN ACQUISTI a ON b.IdAcquisto = a.IdAcquisto " +
                           "WHERE a.DataAcquisto BETWEEN ? AND ? " +
                           ") AS ricavi, " +
                           "(" +
                           "SELECT SUM(r.Costo) AS Costo_Operativo_Totale " +
                           "FROM RIFORNIMENTI r " +
                           "JOIN VOLI v ON r.IdVolo = v.IdVolo " +
                           "WHERE v.DataOraPartenzaProg BETWEEN ? AND ?) AS costi";

            DataResult data = DAUtility.getAllData(DAUtility.executeQuery(connection, query, startDate, endDate, startDate, endDate));

            for (int i = 0; i < data.getNumColumns(); i++) {
                TableColumn<List<Object>, Object> column = new TableColumn<>(data.getColumnName(i));
                int columnIndex = i;
                column.setCellValueFactory(cellData ->
                        new SimpleObjectProperty<>(cellData.getValue().get(columnIndex)));
                customizableTable.getColumns().add(column);
            }

            customizableTable.getItems().setAll(data.getData());

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupAircraftTable() {
        aircraftsBuilderColumn.setCellValueFactory(new PropertyValueFactory<>("costruttore"));
        aircraftsConfigurationIdColumn.setCellValueFactory(new PropertyValueFactory<>("idConfigurazione"));
        aircraftsRegistrationCodeColumn.setCellValueFactory(new PropertyValueFactory<>("codiceRegistrazione"));
        aircraftsTypeColumn.setCellValueFactory(new PropertyValueFactory<>("modello"));
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

    private void setupSchedulesTable() {
        schedulesSeatConfigurationIdColumn.setCellValueFactory(new PropertyValueFactory<>("idConfigurazione"));
        schedulesDayColumn.setCellValueFactory(new PropertyValueFactory<>("giorno"));
        schedulesFlightNumberColumn.setCellValueFactory(new PropertyValueFactory<>("numeroDiVolo"));
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

    private void configureTables() {
        setupAircraftTable();
        setupEmployeeTable();
        setupFareTable();
        setupSchedulesTable();
        setupSeatClassTable();
        setupSeatConfigTable();
        setupSeatsTable();

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

        ObservableList<Configurazione> configData = FXCollections.observableArrayList();
        configData.addAll(fetchConfigurationsFromDatabase(conn));
        if (!configData.isEmpty()) {
            seatsConfigurationsTable.setItems(configData);
        }

        if (seatsConfigurationsTable.getFocusModel().getFocusedItem() != null) {
            ObservableList<Configurazione> configurationsData = FXCollections.observableArrayList();
            configurationsData.addAll(fetchConfigurationsFromDatabase(conn));
            if (!configurationsData.isEmpty()) {
                seatsConfigurationsTable.setItems(configurationsData);
            }
        }

        ObservableList<Classe> flightClassesData = FXCollections.observableArrayList();
        flightClassesData.addAll(fetchFlightClassesFromDatabase(conn));
        if (!flightClassesData.isEmpty()) {
            travelClassesTable.setItems(flightClassesData);
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

        ObservableList<Programma> schedulesData = FXCollections.observableArrayList();
        schedulesData.addAll(fetchScheduleFromDatabase(conn));
        if (!schedulesData.isEmpty()) {
            schedulesTable.setItems(schedulesData);
        }
    }


    /////////////////////////////////////// Data fetching for tables /////////////////////////////////////////////////

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

    private List<Posto> fetchSeatsFromDatabase(Connection conn, int idConfigurazione) {
        List<Posto> seats;
        try {
            seats = Posto.mapTo(DAUtility.executeQuery(conn, String.valueOf(idConfigurazione)));
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return seats;
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

    private void showErrorAlert(String header, String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(header);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

}
