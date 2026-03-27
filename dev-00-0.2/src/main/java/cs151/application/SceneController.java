package cs151.application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SceneController {
    private Scene scene;

    public static void switchScene1(Stage stage) {
        DataAccessLayer.readDeck();

        Scene1Controller scene1 = new Scene1Controller();
        scene1.scene1UI(stage);
    }

    public static void switchScene2(Stage stage) {
        Scene2Controller scene2 = new Scene2Controller();
        scene2.scene2UI(stage);
    }

    public static void switchScene3(Stage stage) {
        Scene3Controller scene3 = new Scene3Controller();
        scene3.scene3UI(stage);
    }
}
