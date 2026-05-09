package cs151.application;

import cs151.application.controller.MainMenuScene;
import cs151.application.controller.SceneController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

import cs151.application.model.DataAccessLayer;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        DataAccessLayer.getSingleInstance().readDeck();
        SceneController.switchScene(stage, new MainMenuScene());
    }

    public static void main(String[] args) {
        launch();
    }
}

