package org.application.dbairline.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.application.dbairline.model.data.DAUtility;
import org.application.dbairline.model.data.Queries;
import org.application.dbairline.model.data.tables.Classe;
import org.application.dbairline.model.data.tables.Tratta;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class InsertFareWindowController implements Initializable {

    private MainWindowController parentController;
    private List<Tratta> flightLegs;
    private List<Classe> flightClasses;

    @FXML
    private Button closeButton;

    @FXML
    private DatePicker endDateDP;

    @FXML
    private CheckBox endDateCheckBox;

    @FXML
    private ChoiceBox<String> flightClassCB;

    @FXML
    private ChoiceBox<Integer> flightNumberCB;

    @FXML
    private Button insertButton;

    @FXML
    private ChoiceBox<Integer> legNumberCB;

    @FXML
    private DatePicker startDateDP;

    @FXML
    private TextField tariffTF;

    @FXML
    void CloseWindow(MouseEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Inserts a new record into the database with the specified flight details and fare.
     *
     * @param event the MouseEvent triggering the method
     */
    @FXML
    void insert(MouseEvent event) {

        Integer flightNumber = flightNumberCB.getSelectionModel().getSelectedItem();
        Integer legNumber = legNumberCB.getSelectionModel().getSelectedItem();
        String flightClassName = flightClassCB.getSelectionModel().getSelectedItem();
        Integer flightCLassID = flightClasses.stream()
                .filter(c -> c.getNomeClasse().equals(flightClassName))
                .map(Classe::getIdClasse)
                .toList()
                .getFirst();
        Float fare = Float.parseFloat(tariffTF.getText());
        Date startDate = Date.valueOf(startDateDP.getValue().toString());

        if(flightNumber == null || legNumber == null || flightClassName == null || flightCLassID == null ||
           startDate == null){
            showErrorAlert("Errore di input",
                    "Si prega di compilare tutti i campi prima di procedere.");
            return;
        }

        try {
            int res = 0;
            Connection conn = DAUtility.getConnection();

            if (endDateDP.isDisabled()) {
                res = DAUtility.executeUpdate(conn, Queries.Insertions.TARIFFE_SENZA_DATAFINE,
                        flightNumber,
                        legNumber,
                        flightCLassID,
                        startDate,
                        fare);
            } else {
                Date endDate = Date.valueOf(endDateDP.getValue().toString());
                res = DAUtility.executeUpdate(conn, Queries.Insertions.TARIFFE,
                        flightNumber,
                        legNumber,
                        flightCLassID,
                        startDate,
                        endDate,
                        fare);
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
        }catch (NumberFormatException e){
            System.out.println(e.getMessage());
            showErrorAlert("Errore del database", ".");
        }
    }

    @FXML
    void disableEndDatePicker(MouseEvent event) {
        endDateDP.setDisable(!endDateDP.isDisabled());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            List<Integer> flightNumbers = new ArrayList<>();

            Connection conn = DAUtility.getConnection();
            String query = "SELECT NumeroDiVolo FROM Itinerari";
            ResultSet rs = DAUtility.executeQuery(conn, query);
            while (rs.next()) {
                flightNumbers.add(rs.getInt("NumeroDiVolo"));
            }

            flightNumberCB.getItems().addAll(FXCollections.observableArrayList(flightNumbers));
            this.flightClasses = Classe.mapTo(DAUtility.executeQuery(conn, Queries.SelectAll.CLASSI));
            this.flightLegs = Tratta.mapTo(DAUtility.executeQuery(conn, Queries.SelectAll.TRATTE));
            conn.close();

            flightClassCB.setItems(FXCollections.observableList(flightClasses.stream().map(Classe::getNomeClasse).toList()));


            endDateDP.setDisable(true);

            flightNumberCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    List<Integer> legNumbers = flightLegs.stream()
                            .filter(leg -> leg.getNumeroDiVolo() == newValue)
                            .map(Tratta::getNumeroTratta)
                            .collect(Collectors.toList());
                    legNumberCB.setItems(FXCollections.observableList(legNumbers));
                }
            });
        } catch (Exception e) {
            showErrorAlert("Errore di caricamento", "Impossibile caricare la finestra");
            throw new RuntimeException(e);
        }
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
