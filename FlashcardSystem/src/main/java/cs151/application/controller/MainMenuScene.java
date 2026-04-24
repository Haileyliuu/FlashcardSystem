package cs151.application.controller;

import cs151.application.model.DataAccessLayer;
import cs151.application.model.DeckBean;
import cs151.application.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainMenuScene implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private Label deckNumberLabel;

    @FXML
    private Button addDeckButton;

    @FXML
    private TableView<DeckBean> deckTable;

    @FXML
    private TableColumn<DeckBean, String> titleColumn;

    @FXML
    private TableColumn<DeckBean, String> descriptionColumn;

    @FXML
    private TableColumn<DeckBean, Void> actionColumn;

    @FXML
    private Label allFlashcards;

    private ObservableList<DeckBean> deckList;
    private FilteredList<DeckBean> filteredData;
    private SortedList<DeckBean> sortedData;

    public MainMenuScene() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allFlashcards.setUnderline(true);

        deckTable.setRowFactory(tv -> {
            TableRow<DeckBean> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !row.isEmpty()) {
                    DeckBean deck = row.getItem();
                    Stage stage = (Stage) deckTable.getScene().getWindow();
                    SceneController.switchScene4(stage, deck);
                }
            });
            row.setOnMouseEntered(e -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: grey; -fx-cursor: hand;");
                }
            });

            row.setOnMouseExited(e -> {
                row.setStyle("");
            });
            return row;
        });

        titleColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getTitle())
        );

        descriptionColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getDescription())
        );

        actionColumn.setCellFactory(col -> new TableCell<DeckBean, Void>() {
            private final HBox action = new HBox();
            private final Button editButton = new Button("Add Flashcard");
            {
                editButton.setOnAction(event -> {
                    DeckBean deck = getTableView().getItems().get(getTableRow().getIndex());
                    Stage stage = (Stage) getScene().getWindow();
                    SceneController.switchScene3(stage, deck);
                });
                setAlignment(Pos.CENTER);
                editButton.setStyle("-fx-background-color: green");
            }
            private final Button deleteButton = new Button("Delete Deck");
            {
                deleteButton.setStyle("-fx-background-color: red");
                deleteButton.setOnAction(event -> {
                    DeckBean deck = getTableView().getItems().get(getTableRow().getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm Deletion");
                    alert.setHeaderText("Delete Deck: " + deck.getTitle());
                    alert.setContentText("Are you sure you want to delete this deck?\nAll flashcards of this deck will also be deleted.");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        // Delete from storage
                        DataAccessLayer.deleteDeck(deck.getDeckID());
                        for (int i = 0; i < DataAccessLayer.getFlashcards().size(); i++) {
                            if (DataAccessLayer.getFlashcards().get(i).getDeckID() == deck.getDeckID()) {
                                DataAccessLayer.deleteFlashcard(DataAccessLayer.getFlashcards().get(i).getFlashcardID());
                            }
                        }
                        DataAccessLayer.writeDeck();
                        DataAccessLayer.writeFlashcards();
                        // Remove from table
                        deckList.remove(deck);
                    }
                });
            }
            {
                action.setSpacing(5);
                action.getChildren().add(editButton);
                action.getChildren().add(deleteButton);
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                }
                else {
                    setGraphic(action);
                }
            }
        });

        loadDecks();
        setupSearch();
    }

    private void loadDecks() {
        deckList = FXCollections.observableArrayList(DataAccessLayer.getDecks());
        filteredData = new FilteredList<>(deckList, b -> true);
        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(deckTable.comparatorProperty());

        deckTable.setItems(sortedData);
        deckNumberLabel.setText(deckList.size() + " Decks");
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(deck -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (deck.getTitle() != null &&
                        deck.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return deck.getDescription() != null &&
                        deck.getDescription().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }



    @FXML
    private void handleAddDeck() {
        Stage stage = (Stage) addDeckButton.getScene().getWindow();
        SceneController.switchScene2(stage);
    }

    @FXML
    private void handleAllFlashcard() {
        Stage stage = (Stage) allFlashcards.getScene().getWindow();
        SceneController.switchScene5(stage);
    }
}