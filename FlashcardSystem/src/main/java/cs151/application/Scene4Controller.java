package cs151.application;

import cs151.application.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;

public class Scene4Controller {

    public Scene4Controller() {}

    public static void sceneUI(Stage stage, DeckBean deck) {
        stage.setTitle(deck.getTitle());
        DataAccessLayer.readFlashcards();
        List<FlashcardBean> flashcards = DataAccessLayer.getFlashcardsByDeck(deck.getDeckID());
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);

        VBox deck_view = new VBox();
        deck_view.getStyleClass().add("scene4-root");
        VBox deck_info = new VBox();
        deck_info.getStyleClass().add("scene4-header");

        Label deck_name = new Label(deck.getTitle());
        deck_name.getStyleClass().add("scene4-title");

        Label flashcard_amount = new Label(flashcards.size() + " flashcards");
        flashcard_amount.getStyleClass().add("scene4-subtitle");

        deck_info.getChildren().addAll(deck_name, flashcard_amount);


        //Table
        ObservableList<FlashcardBean> data = FXCollections.observableArrayList(flashcards);
        TableView<FlashcardBean> table = new TableView<>();
        table.getStyleClass().add("scene4-table");
        TableColumn<FlashcardBean, String> front = new TableColumn<>("Front");
        front.setCellValueFactory(new PropertyValueFactory<>("front"));
        TableColumn<FlashcardBean, String> back = new TableColumn<>("Back");
        back.setCellValueFactory(new PropertyValueFactory<>("back"));
        TableColumn<FlashcardBean, String> status = new TableColumn<>("Status");
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn<FlashcardBean, String> review = new TableColumn<>("Last Reviewed");
        review.setCellValueFactory(new PropertyValueFactory<>("lastReviewed"));
        TableColumn<FlashcardBean, String> creation = new TableColumn<>("Created");
        creation.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        TableColumn<FlashcardBean, Void> delete = new TableColumn<>("Action");
        delete.setCellFactory(col -> new TableCell<FlashcardBean, Void>() {
            private final Button delete_button = new Button("Delete");
            {
                delete_button.getStyleClass().add("delete-button");
                delete_button.setOnAction(event -> {
                    FlashcardBean flashcard = getTableView().getItems().get(getTableRow().getIndex());
                    DataAccessLayer.deleteFlashcard(flashcard.getFlashcardID());
                    data.remove(flashcard);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                }
                else {
                    setGraphic(delete_button);
                }
            }
        });
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMaxWidth(Double.MAX_VALUE);
        table.setPrefHeight(400);
        table.setPrefWidth(1000);
        table.getColumns().add(front);
        table.getColumns().add(back);
        table.getColumns().add(status);
        table.getColumns().add(review);
        table.getColumns().add(creation);
        table.getColumns().add(delete);
        table.setItems(data);
        //Table end

        //Search bar
        TextField search_bar = new TextField();
        search_bar.setPromptText("Search flashcards");
        search_bar.setMaxWidth(300);
        FilteredList<FlashcardBean> filtered = new FilteredList<>(data, p -> true);
        table.setItems(filtered);
        search_bar.textProperty().addListener((obs, oldValue, newValue) -> {
            String filter = newValue.toLowerCase();

            filtered.setPredicate(f -> {
                if (filter.isEmpty()) return true;

                return f.getFront().toLowerCase().contains(filter)
                        || f.getBack().toLowerCase().contains(filter)
                        || f.getStatus().toLowerCase().contains(filter)
                        || f.getCreationDate().toLowerCase().contains(filter)
                        || f.getLastReviewed().toLowerCase().contains(filter);
            });
        });
        //Search bar end

        Button back_button = new Button("Back");
        back_button.getStyleClass().add("back-button");
        back_button.setOnAction(event -> SceneController.switchScene1(stage));

        search_bar.getStyleClass().add("scene4-search");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox toolbar = new HBox();
        toolbar.getStyleClass().add("scene4-toolbar");
        toolbar.getChildren().addAll(back_button, spacer, search_bar);

        VBox table_card = new VBox();
        table_card.getStyleClass().add("scene4-table-card");
        table_card.getChildren().add(table);

        deck_view.getChildren().addAll(toolbar, deck_info, table_card);
        scroll.setContent(deck_view);
        Scene scene = new Scene(scroll,1300, 600);
        scene.getStylesheets().add(Scene4Controller.class.getResource("/cs151/application/createDeck.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
