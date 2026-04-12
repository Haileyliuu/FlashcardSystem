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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Scene1Controller implements Initializable {

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

    private ObservableList<DeckBean> deckList;
    private FilteredList<DeckBean> filteredData;
    private SortedList<DeckBean> sortedData;

    public Scene1Controller() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getTitle())
        );

        descriptionColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getDescription())
        );

        titleColumn.setCellFactory(col -> new TableCell<DeckBean, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setOnMouseClicked(null);
                    setStyle("");
                    return;
                }

                setText(item);
                setUnderline(true);
                setStyle("-fx-cursor: hand;");

                setOnMouseClicked(e -> {
                    DeckBean deck = getTableView().getItems().get(getIndex());
                    Stage stage = (Stage) getScene().getWindow();
                    SceneController.switchScene4(stage, deck);
                });
            }
        });

        actionColumn.setCellFactory(col -> new TableCell<DeckBean, Void>() {
            private final Button editButton = new Button("Add");

            {
                editButton.setOnAction(event -> {
                    DeckBean deck = getTableView().getItems().get(getTableRow().getIndex());
                    Stage stage = (Stage) getScene().getWindow();
                    SceneController.switchScene3(stage, deck);
                });
                setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                }
                else {
                    setGraphic(editButton);
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
}