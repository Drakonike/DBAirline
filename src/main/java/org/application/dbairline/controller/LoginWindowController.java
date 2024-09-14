package org.application.dbairline.controller;

import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.application.dbairline.model.data.DAUtility;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class LoginWindowController implements Initializable {

    private GUIManager manager;

    @FXML
    private Button loginButton;

    @FXML
    private TextField passwordTF;

    @FXML
    private TextField urlTF;

    @FXML
    private TextField usernameTF;

    @FXML
    private ChoiceBox<String> windowSelectionCB;


    @FXML
    void login(MouseEvent event) {
        String username = usernameTF.getText();
        String password = passwordTF.getText();
        String url = urlTF.getText();

        try {
            boolean res = DAUtility.setCredentials(url, username, password);
            if (!res) {
                System.out.println("Credentials can't be set");
                showErrorAlert("Errore di login", "");
                System.exit(1);
            }
            DAUtility.getConnection();
        } catch (SQLException e) {
            System.out.println("Can't connect to database");
            showErrorAlert("Errore di login",
                    "Impossibile connettersi al database. Verificare che i campi compilati siano corretti.");
            DAUtility.resetCredentials();
            return;
        }

        String chosenWindow = windowSelectionCB.getValue();
        if (chosenWindow == null) {
            System.out.println("No window selected");
            showErrorAlert("Errore di input",
                    "Si prega di compilare tutti i campi prima di procedere.");
            return;
        }
        manager.createWindow(chosenWindow);

        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        windowSelectionCB.setItems(FXCollections.observableList(List.of("Administrazione", "Dispatcher", "Gestione Biglietti")));
    }

    public void setManager(GUIManager manager) {
        this.manager = manager;
    }

    private void showErrorAlert(String header, String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(header);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
