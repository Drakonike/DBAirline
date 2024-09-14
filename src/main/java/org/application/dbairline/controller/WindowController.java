package org.application.dbairline.controller;

import javafx.fxml.Initializable;

public abstract class WindowController implements Initializable {

    private GUIManager manager;

    public void refreshTableViews(){}

    public void setManager(GUIManager GUIManager) {
        this.manager = GUIManager;
    }

}
