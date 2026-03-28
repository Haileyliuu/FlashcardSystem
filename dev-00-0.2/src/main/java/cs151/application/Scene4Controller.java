package cs151.application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Scene4Controller {

    public Scene4Controller() {}

    public static void scene4UI(Stage stage, DeckBean deck) {
        DataAccessLayer.readFlashcards();
        List<FlashcardBean> flashcards = DataAccessLayer.getFlashcardsByDeck(deck.getTitle());
        ScrollPane scroll = new ScrollPane();
        scroll.setStyle("-fx-background: #1e1e1e; -fx-background-color: #1e1e1e;");

        VBox deck_view = new VBox();
        deck_view.setStyle("-fx-background-color: #1e1e1e;");
        deck_view.setPadding(new Insets(10,0,0,10));
        deck_view.setSpacing(20);
        HBox deck_info = new HBox();
        deck_info.setSpacing(30);
        Label deck_name = new Label("Viewing deck: " + deck.getTitle());
        deck_name.setFont(Font.font("", 30));
        Label flashcard_amount = new Label(flashcards.size() + " flashcards");
        flashcard_amount.setFont(Font.font("", 30));
        deck_info.getChildren().add(deck_name);
        deck_info.getChildren().add(flashcard_amount);

        //Table
        TableView<FlashcardBean> table = new TableView<>();
        TableColumn<FlashcardBean, String> front = new TableColumn<>("Front");
        front.setCellValueFactory(new PropertyValueFactory<>("front"));
        TableColumn<FlashcardBean, String> back = new TableColumn<>("Back");
        back.setCellValueFactory(new PropertyValueFactory<>("back"));
        TableColumn<FlashcardBean, String> status = new TableColumn<>("Status");
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn<FlashcardBean, String> review = new TableColumn<>("Last Reviewed");
        review.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        TableColumn<FlashcardBean, String> creation = new TableColumn<>("Created");
        creation.setCellValueFactory(new PropertyValueFactory<>("lastReviewed"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMaxWidth(Double.MAX_VALUE);
        table.setPrefHeight(400);
        table.setPrefWidth(1000);
        table.getColumns().add(front);
        table.getColumns().add(back);
        table.getColumns().add(status);
        table.getColumns().add(review);
        table.getColumns().add(creation);
        for (FlashcardBean flashcard : flashcards) {
            table.getItems().add(flashcard);
        }
        //Table end


        Button back_button = new Button("Back");
        back_button.setOnAction(event -> {
            SceneController.switchScene1(stage);
        });
        deck_view.getChildren().add(back_button);
        deck_view.getChildren().add(deck_info);
        deck_view.getChildren().add(table);
        scroll.setContent(deck_view);
        Scene scene = new Scene(scroll,1300, 600);
        scene.getStylesheets().add(Scene4Controller.class.getResource("/cs151/application/createDeck.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
