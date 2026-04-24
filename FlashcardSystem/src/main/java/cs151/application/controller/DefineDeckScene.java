package cs151.application.controller;

import cs151.application.SceneController;
import cs151.application.model.DataAccessLayer;
import cs151.application.model.DeckBean;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DefineDeckScene implements Initializable {

    @FXML
    private TextField deckNameField;

    @FXML
    private TextArea deckDescriptionArea;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;

    public DefineDeckScene() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deckNameField.textProperty().addListener((obs, oldText, newText) -> {
            deckNameField.getStyleClass().remove("error");
        });
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        SceneController.switchScene1(stage);
    }

    @FXML
    private void handleSave() {
        String trimmedName = deckNameField.getText().replaceAll(" ", "");

        if (trimmedName.isEmpty()) {
            deckNameField.clear();
            deckNameField.setPromptText("Deck name is required");
            deckNameField.getStyleClass().add("error");
            return;
        }

        boolean found = false;
        for (int i = 0; i < DataAccessLayer.getDecks().size(); i++) {
            String check = DataAccessLayer.getDecks().get(i).getTitle().replaceAll(" ", "");
            if (trimmedName.equals(check)) {
                deckNameField.clear();
                deckNameField.setPromptText("Title has already been used");
                deckNameField.getStyleClass().add("error");
                found = true;
                break;
            }
        }

        if (!found) {
            DeckBean newDeck = new DeckBean();
            newDeck.setTitle(deckNameField.getText());
            newDeck.setDescription(deckDescriptionArea.getText());

            DataAccessLayer.insertDeck(newDeck);
            DataAccessLayer.writeDeck();

            Stage stage = (Stage) saveButton.getScene().getWindow();
            SceneController.switchScene1(stage);
        }
    }
}