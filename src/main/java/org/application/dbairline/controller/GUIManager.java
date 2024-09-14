package org.application.dbairline.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.application.dbairline.App;
import org.application.dbairline.model.data.DAUtility;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GUIManager {

    private final Map<String,String> screens = Map.of("Administrazione", "AdministrationWindow.fxml", "Dispatcher", "DispatcherWindow.fxml", "Gestione Biglietti", "TicketManagerWindow.fxml");

    private final Stage primaryStage;
    private final Stage popup;

    public GUIManager(Stage stage) {
        primaryStage = stage;
        popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(primaryStage);

        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/LoginWindow.fxml"));
            Scene scene = new Scene(loader.load());
            LoginWindowController controller = loader.getController();
            controller.setManager(this);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Stage createPopup(FXMLLoader loader) {
        if (!popup.isShowing()) {
            try {
                Scene scene = new Scene(loader.load());
                popup.setScene(scene);
                return popup;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public void createWindow(String chosenWindow) {

        String screenPath = screens.get(chosenWindow);

        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/" + screenPath));
            Scene scene = new Scene(loader.load());
            WindowController controller = loader.getController();
            controller.setManager(this);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
