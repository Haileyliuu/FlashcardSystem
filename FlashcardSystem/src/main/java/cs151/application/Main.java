package cs151.application;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SceneController.switchScene1(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}

