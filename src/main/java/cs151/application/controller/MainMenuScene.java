package cs151.application.controller;

import cs151.application.model.DataAccessLayer;
import cs151.application.model.DeckBean;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainMenuScene implements Initializable, SceneInterface {
    public void show(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cs151/application/mainmenu.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 600);
            scene.getStylesheets().add(
                    SceneController.class.getResource("/cs151/application/createDeck.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.setTitle("Main Menu");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private TextField searchField;
    @FXML private Label deckNumberLabel;
    @FXML private Button addDeckButton;
    @FXML private TableView<DeckBean> deckTable;
    @FXML private TableColumn<DeckBean, String> titleColumn;
    @FXML private TableColumn<DeckBean, String> descriptionColumn;
    @FXML private Label allFlashcards;

    @FXML private HBox bottomActionBar;
    @FXML private Button addFlashcardButton;
    @FXML private Button editDeckButton;
    @FXML private Button deleteDeckButton;

    private ObservableList<DeckBean> deckList;
    private FilteredList<DeckBean> filteredData;
    private SortedList<DeckBean> sortedData;
    private DeckBean currentDeckSelected;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allFlashcards.setUnderline(true);

        deckTable.setRowFactory(tv -> {
            TableRow<DeckBean> row = new TableRow<>();

            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !row.isEmpty()) {
                    DeckBean deck = row.getItem();
                    Stage stage = (Stage) deckTable.getScene().getWindow();
                    SceneController.switchScene(stage, new ReviewDeckScene(deck));
                }
            });

            row.setOnMouseEntered(e -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: rgb(88, 88, 88); -fx-cursor: hand;");
                }
            });

            row.setOnMouseExited(e -> row.setStyle(""));

            return row;
        });

        titleColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getTitle())
        );

        descriptionColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getDescription())
        );

        bottomActionBar.setVisible(false);
        bottomActionBar.setManaged(false);

        deckTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            currentDeckSelected = newSelection;

            boolean hasSelection = newSelection != null;
            bottomActionBar.setVisible(hasSelection);
            bottomActionBar.setManaged(hasSelection);
        });

        loadDecks();
        setupSearch();
    }

    private void loadDecks() {
        deckList = FXCollections.observableArrayList(DataAccessLayer.getSingleInstance().getDecks());
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

                if (deck.getTitle() != null && deck.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return deck.getDescription() != null &&
                        deck.getDescription().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    @FXML
    private void handleAddFlashcard() {
        if (currentDeckSelected != null) {
            Stage stage = (Stage) deckTable.getScene().getWindow();
            SceneController.switchScene(stage, new DefineFlashcardScene(currentDeckSelected));
        }
    }

    @FXML
    private void handleEditDeck() {
        if (currentDeckSelected != null) {
            Stage stage = (Stage) deckTable.getScene().getWindow();
            SceneController.switchScene(stage, new ViewDeckScene(currentDeckSelected));
        }
    }

    @FXML
    private void handleDeleteDeck() {
        if (currentDeckSelected == null) return;

        DeckBean deck = currentDeckSelected;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/cs151/application/createDeck.css").toExternalForm()
        );
        dialogPane.getStyleClass().add("delete-dialog");
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Deck: " + deck.getTitle());
        alert.setContentText("Are you sure you want to delete this deck?\nAll flashcards of this deck will also be deleted.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            DataAccessLayer.getSingleInstance().deleteDeck(deck.getDeckID());
            deckNumberLabel.setText(deckList.size() + " Decks");

            for (int i = DataAccessLayer.getSingleInstance().getFlashcards().size() - 1; i >= 0; i--) {
                if (DataAccessLayer.getSingleInstance().getFlashcards().get(i).getDeckID() == deck.getDeckID()) {
                    DataAccessLayer.getSingleInstance().deleteFlashcard(DataAccessLayer.getSingleInstance().getFlashcards().get(i).getFlashcardID());
                }
            }

            DataAccessLayer.getSingleInstance().writeDeck();
            DataAccessLayer.getSingleInstance().writeFlashcards();

            deckList.remove(deck);
            deckTable.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void handleAddDeck() {
        Stage stage = (Stage) addDeckButton.getScene().getWindow();
        SceneController.switchScene(stage, new DefineDeckScene());
    }

    @FXML
    private void handleAllFlashcard() {
        Stage stage = (Stage) allFlashcards.getScene().getWindow();
        SceneController.switchScene(stage, new AllFlashcardScene());
    }
    @FXML
    private void handleReview() {
        Stage stage = (Stage) deckTable.getScene().getWindow();
        SceneController.switchScene(stage, new ReviewScene());
    }
}