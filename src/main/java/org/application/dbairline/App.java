package org.application.dbairline;

import javafx.application.Application;
import javafx.stage.Stage;
import org.application.dbairline.controller.WindowManager;

import java.util.Arrays;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        try {
            new WindowManager(stage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}