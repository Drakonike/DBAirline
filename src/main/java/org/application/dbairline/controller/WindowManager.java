package org.application.dbairline.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.application.dbairline.App;

import java.io.IOException;

public class WindowManager {
    private final Stage primaryStage;
    private final Stage popup;

    public WindowManager(Stage stage) {
        primaryStage = stage;
        popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(primaryStage);

        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/MainWindow.fxml"));
            Scene scene = new Scene(loader.load());
            MainWindowController controller = loader.getController();
            controller.setManager(this);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
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
}
