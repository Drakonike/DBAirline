package org.application.dbairline.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.application.dbairline.model.data.DAUtility;
import org.application.dbairline.model.data.Queries;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static org.application.dbairline.model.data.tables.Dipendente.RANKS;
import static org.application.dbairline.model.data.tables.Dipendente.ROLES;

public class InsertEmployeeWindowController implements Initializable {

    private MainWindowController parentController;

    @FXML
    private ChoiceBox<String> rankChoiceBox;

    @FXML
    private ChoiceBox<String> roleChoiceBox;

    @FXML
    private TextField addressCAPTextField;

    @FXML
    private TextField cityTextField;

    @FXML
    private Button closeButton;

    @FXML
    private Button insertButton;

    @FXML
    private TextField addressNameTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField nationalityTextField;

    @FXML
    private TextField addressNumberTextField;

    @FXML
    private TextField wageTextField;

    @FXML
    private TextField surnameTextField;

    @FXML
    private TextField phoneTextField;


    @FXML
    void CloseWindow(MouseEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void insert(MouseEvent event) {
        try {
            int res = 1;
            String role = roleChoiceBox.getSelectionModel().getSelectedItem();
            String rank = rankChoiceBox.getSelectionModel().getSelectedItem();

            if (role == null || rank == null) {
                showErrorAlert("Errore di input",
                        "Si prega di compilare tutti i campi prima di procedere.");
                return;
            }

            if (role.equals(ROLES.getFirst()) && rank.equals(RANKS.getFirst())) {
                showErrorAlert("Errore di input",
                        "I piloti devono avere un rango assegnato.");
                return;
            }

            Connection conn = DAUtility.getConnection();
            res = DAUtility.executeUpdate(conn, Queries.Insertions.DIPENDENTI,
                    nameTextField.getText(),
                    surnameTextField.getText(),
                    nationalityTextField.getText(),
                    cityTextField.getText(),
                    addressNameTextField.getText(),
                    Integer.parseInt(addressNumberTextField.getText()),
                    addressCAPTextField.getText(),
                    phoneTextField.getText(),
                    role,
                    rank,
                    Float.parseFloat(wageTextField.getText()));
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
    public void initialize(URL location, ResourceBundle resources) {
        roleChoiceBox.setItems(FXCollections.observableList(ROLES));
        rankChoiceBox.setItems(FXCollections.observableList(RANKS));
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
