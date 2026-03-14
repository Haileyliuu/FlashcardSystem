package cs151.application;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Main Menu");

        SwitchScene main_menu = new SwitchScene();
        main_menu.switchScene1(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}

