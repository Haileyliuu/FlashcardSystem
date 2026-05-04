package cs151.application.controller;

import javafx.stage.Stage;

public class SceneController {
    public static void switchScene(Stage stage, SceneInterface scene) {
        scene.show(stage);
    }
}
