package cs151.application.controller;

import cs151.application.model.DataAccessLayer;
import cs151.application.model.DeckBean;
import cs151.application.model.FlashcardBean;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DefineFlashcardScene implements SceneInterface {
    public DefineFlashcardScene() {}
    public DefineFlashcardScene(DeckBean deck) {
        this.deck = deck;
    }

    public void show(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cs151/application/defineflashcard.fxml"));
            Scene scene = new Scene(loader.load(), 900, 600);
            scene.getStylesheets().add(
                    SceneController.class.getResource("/cs151/application/createDeck.css").toExternalForm()
            );

            DefineFlashcardScene controller = loader.getController();
            controller.setDeck(deck);

            stage.setScene(scene);
            stage.setTitle("Define Flashcards");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Label defineFlashcardsLabel;

    @FXML
    private VBox listFlashcards;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;

    @FXML
    private void handleHome() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        SceneController.switchScene(stage, new MainMenuScene());
    }

    @FXML
    private void handleLibrary() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        SceneController.switchScene(stage, new AllFlashcardScene());
    }

    private DeckBean deck;
    private List<FlashcardBean> existingFlashcards = new ArrayList<>();
    private final List<FlashcardItemController> flashcardControllers = new ArrayList<>();

    public void setDeck(DeckBean deck) {
        this.deck = deck;

        DataAccessLayer.getSingleInstance().readFlashcards();
        existingFlashcards = DataAccessLayer.getSingleInstance().getFlashcardsByDeck(deck.getDeckID());

        defineFlashcardsLabel.setText("Define flashcards of deck: " + deck.getTitle());

        flashcardControllers.clear();
        listFlashcards.getChildren().clear();

        addFlashcardAtEnd();
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        SceneController.switchScene(stage, new MainMenuScene());
    }

    @FXML
    private void handleSave() {
        boolean error = false;

        for (FlashcardItemController flashcard : flashcardControllers) {
            String frontText = flashcard.getFrontField().getText();
            String backText = flashcard.getBackArea().getText();
            String trimmedName = frontText.replaceAll(" ", "");

            if (trimmedName.isEmpty()) {
                flashcard.getFrontField().clear();
                flashcard.getFrontField().setPromptText("Front text is required");
                flashcard.getFrontField().getStyleClass().add("error");
                error = true;
            }

            if (backText.isEmpty()) {
                flashcard.getBackArea().clear();
                flashcard.getBackArea().setPromptText("Back text is required");
                flashcard.getBackArea().getStyleClass().add("error");
                error = true;
            }

            for (FlashcardBean existing : existingFlashcards) {
                String check = existing.getFront().replaceAll(" ", "");
                if (trimmedName.equals(check)) {
                    flashcard.getFrontField().clear();
                    flashcard.getFrontField().setPromptText("Front text has already been used");
                    flashcard.getFrontField().getStyleClass().add("error");
                    error = true;
                }
            }
        }

        for (int i = 0; i < flashcardControllers.size(); i++) {
            for (int j = i + 1; j < flashcardControllers.size(); j++) {
                String a = flashcardControllers.get(i).getFrontField().getText().replaceAll(" ", "");
                String b = flashcardControllers.get(j).getFrontField().getText().replaceAll(" ", "");

                if (!a.isEmpty() && a.equals(b)) {
                    flashcardControllers.get(j).getFrontField().clear();
                    flashcardControllers.get(j).getFrontField().setPromptText("Duplicate front text");
                    flashcardControllers.get(j).getFrontField().getStyleClass().add("error");
                    error = true;
                }
            }
        }

        if (!error) {
            for (FlashcardItemController flashcard : flashcardControllers) {
                FlashcardBean newFlashcard = new FlashcardBean();
                newFlashcard.setDeckID(deck.getDeckID());
                newFlashcard.setFront(flashcard.getFrontField().getText());
                newFlashcard.setBack(flashcard.getBackArea().getText());
                newFlashcard.setStatus("New");
                newFlashcard.setCreationDate(LocalDate.now().toString());
                newFlashcard.setLastReviewed(LocalDate.now().toString());

                DataAccessLayer.getSingleInstance().insertFlashcard(newFlashcard);
            }

            DataAccessLayer.getSingleInstance().writeFlashcards();

            Stage stage = (Stage) saveButton.getScene().getWindow();
            SceneController.switchScene(stage, new MainMenuScene());
        }
    }

    private void addFlashcardAtEnd() {
        addFlashcardAtIndex(flashcardControllers.size());
    }

    private void addFlashcardAtIndex(int index) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/cs151/application/flashcard_item.fxml")
            );

            Parent flashcardNode = loader.load();
            FlashcardItemController controller = loader.getController();

            controller.setOnAdd(() -> {
                int currentIndex = flashcardControllers.indexOf(controller);
                addFlashcardAtIndex(currentIndex + 1);
            });

            controller.setOnDelete(() -> {
                if (flashcardControllers.size() > 1) {
                    int currentIndex = flashcardControllers.indexOf(controller);
                    flashcardControllers.remove(currentIndex);
                    listFlashcards.getChildren().remove(currentIndex);
                }
            });

            flashcardControllers.add(index, controller);
            listFlashcards.getChildren().add(index, flashcardNode);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}