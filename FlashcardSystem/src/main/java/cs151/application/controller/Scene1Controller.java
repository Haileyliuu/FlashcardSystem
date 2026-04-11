package cs151.application.controller;

import cs151.application.SceneController;
import cs151.application.model.DataAccessLayer;
import cs151.application.model.DeckBean;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Scene1Controller implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private Label deckNumberLabel;

    @FXML
    private Button addDeckBtn;

    @FXML
    private TableView<DeckBean> deckTable;

    @FXML
    private TableColumn<DeckBean, String> titleColumn;

    @FXML
    private TableColumn<DeckBean, String> descriptionColumn;

    @FXML
    private TableColumn<DeckBean, Void> actionColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int deckAmount = DataAccessLayer.getDecks().size();
        deckNumberLabel.setText(deckAmount + " Decks");

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

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
            private final Button editButton = new Button("Edit");

            {
                editButton.setOnAction(event -> {
                    DeckBean deck = getTableView().getItems().get(getTableRow().getIndex());
                    Stage stage = (Stage) getScene().getWindow();
                    SceneController.switchScene3(stage, deck);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        
        deckTable.setItems(FXCollections.observableArrayList(DataAccessLayer.getDecks()));
    }

    @FXML
    private void handleAddDeck() {
        Stage stage = (Stage) addDeckBtn.getScene().getWindow();
        SceneController.switchScene2(stage);
    }
}