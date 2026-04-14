package cs151.application.controller;

import cs151.application.SceneController;
import cs151.application.model.DataAccessLayer;
import cs151.application.model.DeckBean;
import cs151.application.model.FlashcardBean;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private TextField searchBar;

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

    @FXML
    private TableColumn<FlashcardBean, Void> deleteColumn;

    private DeckBean deck;
    private ObservableList<FlashcardBean> data;
    private FilteredList<FlashcardBean> filtered;

    public Scene4Controller() {}

    @FXML
    public void initialize() {
        flashcardTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        frontColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getFront())
        );
        frontColumn.setCellFactory(col -> new TableCell<FlashcardBean, String>() {
            private final Label label = new Label();

            {
                label.setWrapText(false);
                label.setStyle("-fx-text-overrun: ellipsis;");
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    return;
                }

                // Always take only the first line
                String firstLine = item.split("\n")[0];

                label.setText(firstLine);
                setGraphic(label);
            }
        });
        backColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getBack())
        );
        backColumn.setCellFactory(col -> new TableCell<FlashcardBean, String>() {
            private final Label label = new Label();

            {
                label.setWrapText(false);
                label.setStyle("-fx-text-overrun: ellipsis;");
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    return;
                }

                // Always take only the first line
                String firstLine = item.split("\n")[0];

                label.setText(firstLine);
                setGraphic(label);
            }
        });
        statusColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getStatus())
        );
        reviewColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getLastReviewed())
        );
        creationColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getCreationDate())
        );

        deleteColumn.setCellFactory(col -> new TableCell<FlashcardBean, Void>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.getStyleClass().add("delete-button");
                deleteButton.setOnAction(event -> {
                    FlashcardBean flashcard = getTableView().getItems().get(getTableRow().getIndex());
                    DataAccessLayer.deleteFlashcard(flashcard.getFlashcardID());
                    data.remove(flashcard);
                    DataAccessLayer.writeFlashcards();
                    updateFlashcardCount();
                });
                
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });

        searchBar.textProperty().addListener((obs, oldValue, newValue) -> {
            if (filtered == null) {
                return;
            }

            String filter = newValue == null ? "" : newValue.toLowerCase();

            filtered.setPredicate(f -> {
                if (filter.isEmpty()) {
                    return true;
                }

                return contains(f.getFront(), filter)
                        || contains(f.getBack(), filter)
                        || contains(f.getStatus(), filter);
            });
        });
    }

    public void setDeck(DeckBean deck) {
        this.deck = deck;

        DataAccessLayer.readFlashcards();
        List<FlashcardBean> flashcards = DataAccessLayer.getFlashcardsByDeck(deck.getDeckID());

        deckNameLabel.setText(deck.getTitle());
        data = FXCollections.observableArrayList(flashcards);
        filtered = new FilteredList<>(data, p -> true);
        flashcardTable.setItems(filtered);
        updateFlashcardCount();
    }

    private void updateFlashcardCount() {
        int count = data == null ? 0 : data.size();
        flashcardAmountLabel.setText(count + " flashcards");
    }

    private boolean contains(String value, String filter) {
        return value != null && value.toLowerCase().contains(filter);
    }

    @FXML
    private void handleBack() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        SceneController.switchScene1(stage);
    }
}