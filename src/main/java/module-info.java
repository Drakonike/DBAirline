module org.application.dbairline {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.jdi;
    requires mysql.connector.java;

    opens org.application.dbairline to javafx.fxml;
    exports org.application.dbairline;
    exports org.application.dbairline.controller;
    opens org.application.dbairline.controller to javafx.fxml;
    opens org.application.dbairline.model.data.tables to javafx.base;
}