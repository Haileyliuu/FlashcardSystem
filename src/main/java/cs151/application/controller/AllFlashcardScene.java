package cs151.application.controller;

import cs151.application.model.DataAccessLayer;
import cs151.application.model.FlashcardBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class AllFlashcardScene implements Initializable, SceneInterface {
    public void show(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cs151/application/allflashcard.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 600);
            scene.getStylesheets().add(
                    SceneController.class.getResource("/cs151/application/createDeck.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.setTitle("All Flashcards");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private Button backButton;
    @FXML private TextField searchBar;
    @FXML private Label flashcardAmount;
    @FXML private TableView<FlashcardBean> flashcardTable;
    @FXML private TableColumn<FlashcardBean, String> deckColumn;
    @FXML private TableColumn<FlashcardBean, String> frontColumn;
    @FXML private TableColumn<FlashcardBean, String> backColumn;
    @FXML private TableColumn<FlashcardBean, String> statusColumn;
    @FXML private TableColumn<FlashcardBean, String> reviewColumn;
    @FXML private TableColumn<FlashcardBean, String> creationColumn;

    private HashMap<Integer, String> decks = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Build deck ID -> name map
        for (int i = 0; i < DataAccessLayer.getDecks().size(); i++) {
            decks.put(DataAccessLayer.getDecks().get(i).getDeckID(),
                    DataAccessLayer.getDecks().get(i).getTitle());
        }

        setupColumns();

        ObservableList<FlashcardBean> flashcardList =
                FXCollections.observableArrayList(DataAccessLayer.getFlashcards());

        flashcardAmount.setText(flashcardList.size() + " Flashcards");

        FilteredList<FlashcardBean> filtered = new FilteredList<>(flashcardList, p -> true);
        flashcardTable.setItems(filtered);

        searchBar.textProperty().addListener((obs, oldValue, newValue) -> {
            String filter = newValue.toLowerCase();
            filtered.setPredicate(f -> {
                if (filter.isEmpty()) return true;
                String deckName = decks.getOrDefault(f.getDeckID(), "").toLowerCase();
                return f.getFront().toLowerCase().contains(filter)
                        || f.getBack().toLowerCase().contains(filter)
                        || f.getStatus().toLowerCase().contains(filter)
                        || deckName.contains(filter);
            });
        });
    }

    private void setupColumns() {
        deckColumn.setCellFactory(col -> new TableCell<FlashcardBean, String>() {
            private final Label label = new Label();
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null); return;
                }
                String deckName = decks.getOrDefault(getTableRow().getItem().getDeckID(), "");
                label.setText(deckName.split("\n")[0]);
                setGraphic(label);
            }
        });

        frontColumn.setCellValueFactory(new PropertyValueFactory<>("front"));
        frontColumn.setCellFactory(col -> new TableCell<FlashcardBean, String>() {
            private final Label label = new Label();
            { label.setWrapText(false); label.setStyle("-fx-text-overrun: ellipsis;"); }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setGraphic(null); return; }
                label.setText(item.split("\n")[0]);
                setGraphic(label);
            }
        });

        backColumn.setCellValueFactory(new PropertyValueFactory<>("back"));
        backColumn.setCellFactory(col -> new TableCell<FlashcardBean, String>() {
            private final Label label = new Label();
            { label.setWrapText(false); label.setStyle("-fx-text-overrun: ellipsis;"); }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setGraphic(null); return; }
                label.setText(item.split("\n")[0]);
                setGraphic(label);
            }
        });

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        reviewColumn.setCellValueFactory(new PropertyValueFactory<>("lastReviewed"));
        creationColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
    }

    @FXML
    private void handleBack() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        SceneController.switchScene(stage, new MainMenuScene());
    }
}