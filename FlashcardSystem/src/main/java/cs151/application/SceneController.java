package cs151.application;

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
