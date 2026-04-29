package cs151.application;

import cs151.application.controller.DefineFlashcardScene;
import cs151.application.controller.ReviewDeckScene;
import cs151.application.controller.ViewDeckScene;
import cs151.application.model.DeckBean;
import cs151.application.model.FlashcardBean;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController {

    public static void switchScene1(Stage stage) {
       try {
            FXMLLoader loader = new FXMLLoader(
                    SceneController.class.getResource("/cs151/application/mainmenu.fxml")
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
                SceneController.class.getResource("/cs151/application/definedeck.fxml")
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
                SceneController.class.getResource("/cs151/application/defineflashcard.fxml")
            );

            Scene scene = new Scene(loader.load(), 900, 600);
            scene.getStylesheets().add(
                SceneController.class.getResource("/cs151/application/createDeck.css").toExternalForm()
            );

            DefineFlashcardScene controller = loader.getController();
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
                    SceneController.class.getResource("/cs151/application/viewdeck.fxml")
            );

            Scene scene = new Scene(loader.load(), 1200, 600);
            scene.getStylesheets().add(
                    SceneController.class.getResource("/cs151/application/createDeck.css").toExternalForm()
            );

            ViewDeckScene controller = loader.getController();
            controller.setDeck(deck);

            stage.setTitle(deck.getTitle());
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void switchScene5(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneController.class.getResource("/cs151/application/allflashcard.fxml")
            );

            Scene scene = new Scene(loader.load(), 1200, 600);
            scene.getStylesheets().add(
                    SceneController.class.getResource("/cs151/application/createDeck.css").toExternalForm()
            );

            stage.setTitle("All Flashcards");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void switchScene6(Stage stage, DeckBean deck) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneController.class.getResource("/cs151/application/reviewdeck.fxml")
            );

            Scene scene = new Scene(loader.load(), 600, 800);
            scene.getStylesheets().add(
                    SceneController.class.getResource("/cs151/application/createDeck.css").toExternalForm()
            );
            ReviewDeckScene controller = loader.getController();
            controller.setDeck(deck);

            stage.setTitle("Review " + deck.getTitle());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
