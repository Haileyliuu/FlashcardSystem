package cs151.application.controller;

import cs151.application.SceneController;
import cs151.application.model.DataAccessLayer;
import cs151.application.model.DeckBean;
import cs151.application.model.FlashcardBean;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class Scene4Controller {

    @FXML
    private VBox deckView;

    @FXML
    private Button backButton;

    @FXML
    private Label deckNameLabel;

    @FXML
    private Label flashcardAmountLabel;

    @FXML
    private TableView<FlashcardBean> flashcardTable;

    @FXML
    private TableColumn<FlashcardBean, String> frontColumn;

    @FXML
    private TableColumn<FlashcardBean, String> backColumn;

    @FXML
    private TableColumn<FlashcardBean, String> statusColumn;

    @FXML
    private TableColumn<FlashcardBean, String> reviewColumn;

    @FXML
    private TableColumn<FlashcardBean, String> creationColumn;

    public Scene4Controller() {}

    @FXML
    public void initialize() {
        flashcardTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        frontColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFront())
        );

        backColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBack())
        );

        statusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus())
        );

        reviewColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLastReviewed())
        );

        creationColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCreationDate())
        );
    }

    public void setDeck(DeckBean deck) {

        DataAccessLayer.readFlashcards();
        List<FlashcardBean> flashcards = DataAccessLayer.getFlashcardsByDeck(deck.getDeckID());

        deckNameLabel.setText("Viewing deck: " + deck.getTitle());
        flashcardAmountLabel.setText(flashcards.size() + " flashcards");
        flashcardTable.setItems(FXCollections.observableArrayList(flashcards));
    }

    @FXML
    private void handleBack() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        SceneController.switchScene1(stage);
    }
}