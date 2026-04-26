module cs151.application {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires javafx.graphics;
    requires javafx.base;

    opens cs151.application to javafx.fxml;
    opens cs151.application.model to javafx.base;
    opens cs151.application.controller to javafx.fxml;

    exports cs151.application;
    exports cs151.application.model;
    exports cs151.application.controller;
    
}