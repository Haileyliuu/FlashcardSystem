package cs151.application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class FlashcardItemController {

    @FXML
    private TextArea frontField;

    @FXML
    private TextArea backArea;

    @FXML
    private Button addCardButton;

    @FXML
    private Button deleteCardButton;

    private Runnable onAdd;
    private Runnable onDelete;

    @FXML
    public void initialize() {
        frontField.textProperty().addListener((obs, oldVal, newVal) -> {
            frontField.getStyleClass().remove("error");
        });

        backArea.textProperty().addListener((obs, oldVal, newVal) -> {
            backArea.getStyleClass().remove("error");
        });

        addCardButton.setOnAction(e -> {
            if (onAdd != null) {
                onAdd.run();
            }
        });

        deleteCardButton.setOnAction(e -> {
            if (onDelete != null) {
                onDelete.run();
            }
        });
    }

    public TextArea getFrontField() {
        return frontField;
    }

    public TextArea getBackArea() {
        return backArea;
    }

    public void setOnAdd(Runnable onAdd) {
        this.onAdd = onAdd;
    }

    public void setOnDelete(Runnable onDelete) {
        this.onDelete = onDelete;
    }
}