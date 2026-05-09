package cs151.application.controller;

import cs151.application.model.DataAccessLayer;
import cs151.application.model.DeckBean;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ReviewScene implements Initializable, SceneInterface {
    public void show(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cs151/application/review_scene.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(
                    SceneController.class.getResource("/cs151/application/createDeck.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.setTitle("Review");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private TextField searchField;
    @FXML private TableView<DeckBean> deckTable;
    @FXML private TableColumn<DeckBean, String> titleColumn;
    @FXML private TableColumn<DeckBean, String> descriptionColumn;
    @FXML private Button backButton;

    private ObservableList<DeckBean> deckList;
    private FilteredList<DeckBean> filteredDecks;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getTitle())
        );

        descriptionColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getDescription())
        );

        deckList = FXCollections.observableArrayList(DataAccessLayer.getSingleInstance().getDecks());
        filteredDecks = new FilteredList<>(deckList, deck -> true);
        deckTable.setItems(filteredDecks);

        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            String filter = newValue == null ? "" : newValue.toLowerCase();

            filteredDecks.setPredicate(deck -> {
                if (filter.isEmpty()) {
                    return true;
                }

                return deck.getTitle().toLowerCase().contains(filter)
                        || deck.getDescription().toLowerCase().contains(filter);
            });
        });

        deckTable.setRowFactory(tv -> {
            TableRow<DeckBean> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    DeckBean selectedDeck = row.getItem();
                    Stage stage = (Stage) deckTable.getScene().getWindow();
                    SceneController.switchScene(stage, new ReviewDeckScene(selectedDeck));
                }
            });

            return row;
        });
    }

    @FXML
    private void handleBack() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        SceneController.switchScene(stage, new MainMenuScene());
    }
}