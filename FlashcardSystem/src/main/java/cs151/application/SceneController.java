package cs151.application;

import javafx.stage.Stage;
import cs151.application.model.DataAccessLayer;
import cs151.application.model.DeckBean;

public class SceneController {

    public static void switchScene1(Stage stage) {
        DataAccessLayer.readDeck();

        Scene1Controller.sceneUI(stage);
    }

    public static void switchScene2(Stage stage) {
        Scene2Controller.sceneUI(stage);
    }

    public static void switchScene3(Stage stage, DeckBean deck) {
        Scene3Controller.sceneUI(stage, deck);
    }

    public static void switchScene4(Stage stage, DeckBean deck) {
        Scene4Controller.sceneUI(stage, deck);
    }
}
