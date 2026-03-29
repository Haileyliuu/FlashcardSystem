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

    public static void switchScene1(Stage stage) {
        DataAccessLayer.readDeck();

        Scene1Controller.scene1UI(stage);
    }

    public static void switchScene2(Stage stage) {
        Scene2Controller.scene2UI(stage);
    }

    public static void switchScene3(Stage stage, DeckBean deck) {
        Scene3Controller.scene3UI(stage, deck);
    }

    public static void switchScene4(Stage stage, DeckBean deck) {
        Scene4Controller.scene4UI(stage, deck);
    }
}
