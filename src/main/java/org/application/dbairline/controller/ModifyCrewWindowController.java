package org.application.dbairline.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.application.dbairline.model.data.DAUtility;
import org.application.dbairline.model.data.Queries;
import org.application.dbairline.model.data.tables.Dipendente;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ModifyCrewWindowController implements Initializable {

    private WindowController parentController;
    private int flightId;
    private ObservableList<Dipendente> assignedEmployees;
    private ObservableList<Dipendente> assigningEmployees;
    private ObservableList<Dipendente> deployableEmployees;

    @FXML
    private TableColumn<?, ?> cabinCrewIdCol;

    @FXML
    private TableColumn<?, ?> cabinCrewNameCol;

    @FXML
    private TableColumn<?, ?> cabinCrewRankCol;

    @FXML
    private TableColumn<?, ?> cabinCrewRoleCol;

    @FXML
    private TableColumn<?, ?> cabinCrewSurnameCol;

    @FXML
    private TableView<Dipendente> cabinCrewTable;

    @FXML
    private TableColumn<?, ?> employeeIdCol;

    @FXML
    private TableColumn<?, ?> employeeNameCol;

    @FXML
    private TableColumn<?, ?> employeeRankCol;

    @FXML
    private TableColumn<?, ?> employeeRoleCol;

    @FXML
    private TableColumn<?, ?> employeeSurnameCol;

    @FXML
    private Button insertButton;

    @FXML
    private Button closeButton;

    @FXML
    private TableView<Dipendente> employeeTable;

    @FXML
    private void insert(MouseEvent event) {
        List<Dipendente> tmp = new ArrayList<>(assigningEmployees);
        tmp.removeAll(assignedEmployees);
        assignedEmployees.removeAll(assigningEmployees);

        try {
            int res = 0;
            Connection conn = DAUtility.getConnection();
            String deleteSQL = "DELETE FROM equipaggi WHERE IdDipendente = ? AND IdVolo = ?";

            for (Dipendente employee : assignedEmployees) {
                res = DAUtility.executeUpdate(conn, deleteSQL, employee.getIdDipendente(), flightId);
            }

            for (Dipendente employee : tmp) {
                res = DAUtility.executeUpdate(conn, Queries.Insertions.EQUIPAGGI, employee.getIdDipendente(), flightId);
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

    @FXML
    private void clearCabinCrewTable(MouseEvent event) {
        for (Dipendente employee : assigningEmployees) {
            if (!deployableEmployees.contains(employee)) {
                deployableEmployees.add(employee);
            }
        }
        assigningEmployees.clear();
        cabinCrewTable.setItems(assigningEmployees);
        employeeTable.setItems(deployableEmployees);
    }

    @FXML
    private void exitWindow(MouseEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void moveIn(MouseEvent event) {
        if (employeeTable.getSelectionModel().getSelectedItem() != null) {
            assigningEmployees.add(employeeTable.getSelectionModel().getSelectedItem());
            deployableEmployees.remove(employeeTable.getSelectionModel().getSelectedItem());
            employeeTable.setItems(deployableEmployees);
        }
    }

    @FXML
    private void moveOut(MouseEvent event) {
        if (cabinCrewTable.getSelectionModel().getSelectedItem() != null) {
            deployableEmployees.add(cabinCrewTable.getSelectionModel().getSelectedItem());
            assigningEmployees.remove(cabinCrewTable.getSelectionModel().getSelectedItem());
            cabinCrewTable.setItems(assigningEmployees);
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
                String query = "SELECT d.* FROM dipendenti d, equipaggi a WHERE d.IdDipendente = a.IdDipendente AND a.IdVolo = ?";
                assignedEmployees.addAll(FXCollections.observableList(Dipendente.mapTo(DAUtility.executeQuery(conn, query, this.flightId))));
                deployableEmployees.addAll(FXCollections.observableList(Dipendente.mapTo(DAUtility.executeQuery(conn, Queries.SelectAll.DIPENDENTI))));
                conn.close();

                deployableEmployees.removeAll(assignedEmployees);
                assigningEmployees.addAll(assignedEmployees);

                cabinCrewTable.setItems(assigningEmployees);
                employeeTable.setItems(deployableEmployees);
                return 0;
            } catch (Exception e) {
                showErrorAlert("Errore di caricamento",
                        "Errore durante l'inizializzazione della finestra.");
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cabinCrewIdCol.setCellValueFactory(new PropertyValueFactory<>("idDipendente"));
        cabinCrewNameCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cabinCrewRankCol.setCellValueFactory(new PropertyValueFactory<>("grado"));
        cabinCrewRoleCol.setCellValueFactory(new PropertyValueFactory<>("ruolo"));
        cabinCrewSurnameCol.setCellValueFactory(new PropertyValueFactory<>("cognome"));

        employeeIdCol.setCellValueFactory(new PropertyValueFactory<>("idDipendente"));
        employeeNameCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
        employeeRankCol.setCellValueFactory(new PropertyValueFactory<>("grado"));
        employeeRoleCol.setCellValueFactory(new PropertyValueFactory<>("ruolo"));
        employeeSurnameCol.setCellValueFactory(new PropertyValueFactory<>("cognome"));

        this.assignedEmployees = FXCollections.observableArrayList();
        this.assigningEmployees = FXCollections.observableArrayList();
        this.deployableEmployees = FXCollections.observableArrayList();
    }

    private void showErrorAlert(String header, String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(header);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
