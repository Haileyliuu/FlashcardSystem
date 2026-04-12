package cs151.application;

import cs151.application.controller.Scene3Controller;
import cs151.application.controller.Scene4Controller;
import cs151.application.model.DeckBean;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController {

    public static void switchScene1(Stage stage) {
       try {
            FXMLLoader loader = new FXMLLoader(
                    SceneController.class.getResource("/cs151/application/scene1.fxml")
            );

            Scene scene = new Scene(loader.load(), 1000, 600);
            scene.getStylesheets().add(
                    SceneController.class.getResource("/cs151/application/createDeck.css").toExternalForm()
            );

            stage.setTitle("Main menu");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void switchScene2(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(
                SceneController.class.getResource("/cs151/application/scene2.fxml")
            );

            Scene scene = new Scene(loader.load(), 900, 600);
            scene.getStylesheets().add(
                SceneController.class.getResource("/cs151/application/createDeck.css").toExternalForm()
            );

            stage.setTitle("Define deck");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void switchScene3(Stage stage, DeckBean deck) {
        try {
            FXMLLoader loader = new FXMLLoader(
                SceneController.class.getResource("/cs151/application/scene3.fxml")
            );

            Scene scene = new Scene(loader.load(), 900, 600);
            scene.getStylesheets().add(
                SceneController.class.getResource("/cs151/application/createDeck.css").toExternalForm()
            );

            Scene3Controller controller = loader.getController();
            controller.setDeck(deck);

            stage.setTitle("Define flashcards");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void switchScene4(Stage stage, DeckBean deck) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneController.class.getResource("/cs151/application/scene4.fxml")
            );

            Scene scene = new Scene(loader.load(), 1200, 600);
            scene.getStylesheets().add(
                    SceneController.class.getResource("/cs151/application/createDeck.css").toExternalForm()
            );

            Scene4Controller controller = loader.getController();
            controller.setDeck(deck);

            stage.setTitle(deck.getTitle());
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
