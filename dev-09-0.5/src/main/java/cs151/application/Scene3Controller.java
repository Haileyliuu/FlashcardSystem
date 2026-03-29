package cs151.application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;

public class Scene3Controller {

    private static List<CreateFlashcard> flashcards = new ArrayList<>();
    private static VBox list_flashcards = new VBox();

    public Scene3Controller() {}

    public static void scene3UI(Stage stage, DeckBean deck) {
        stage.setTitle("Define flashcards");
        flashcards.clear();
        list_flashcards.getChildren().clear();
        DataAccessLayer.readFlashcards();
        List<FlashcardBean> flashcard_list = DataAccessLayer.getFlashcardsByDeck(deck.getTitle());

        Label define_flashcards_label = new Label("Define flashcards of deck: " + deck.getTitle());
        define_flashcards_label.setAlignment(Pos.CENTER);
        define_flashcards_label.setMaxWidth(Double.MAX_VALUE);
        define_flashcards_label.getStyleClass().add("header-text");
        define_flashcards_label.setFont(Font.font("", 30));
        define_flashcards_label.setTranslateX(10);
        define_flashcards_label.setTranslateY(10);

        list_flashcards.setSpacing(25);
        list_flashcards.setAlignment(Pos.CENTER);

        CreateFlashcard first = new CreateFlashcard();
        flashcards.add(first);
        list_flashcards.getChildren().add(first.create());
        attachHandlers(first);

        HBox cancel_save = new HBox();
        cancel_save.setSpacing(20);
        cancel_save.setAlignment(Pos.CENTER_RIGHT);
        cancel_save.setMaxWidth(700);
        Button cancel_button = new Button("Cancel");
        cancel_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SceneController.switchScene1(stage);
            }
        });
        Button save_button = new Button("Save");
        save_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                boolean error = false;
                for (CreateFlashcard flashcard: flashcards) {
                    String trimmed_name = flashcard.front_field.getText().replaceAll(" ","");
                    if(trimmed_name.isEmpty()){
                        flashcard.front_field.clear();
                        flashcard.front_field.setPromptText("Front text is required");
                        flashcard.front_field.getStyleClass().add("error");
                        error = true;
                    }
                    if (flashcard.back_area.getText().isEmpty()) {
                        flashcard.back_area.clear();
                        flashcard.back_area.setPromptText("Back text is required");
                        flashcard.back_area.getStyleClass().add("error");
                        error = true;
                    }
                    for (int i = 0; i < flashcard_list.size(); i++) {
                        String check = flashcard_list.get(i).getFront().replaceAll(" ","");
                        if (trimmed_name.equals(check)) {
                            flashcard.front_field.clear();
                            flashcard.front_field.setPromptText("Front text has already been used");
                            flashcard.front_field.getStyleClass().add("error");
                            error = true;
                        }
                    }
                }
                if (!error) {
                    for (CreateFlashcard flashcard : flashcards) {
                        FlashcardBean new_flashcard = new FlashcardBean();
                        new_flashcard.setDeckName(deck.getTitle());
                        new_flashcard.setFront(flashcard.front_field.getText());
                        new_flashcard.setBack(flashcard.back_area.getText());
                        new_flashcard.setStatus("New");
                        new_flashcard.setCreationDate(LocalDate.now().toString());
                        new_flashcard.setLastReviewed(LocalDate.now().toString());
                        DataAccessLayer.insertFlashcard(new_flashcard);

                    }
                    DataAccessLayer.writeFlashcards();
                    SceneController.switchScene1(stage);
                }
            }
        });
        cancel_save.getChildren().add(cancel_button);
        cancel_save.getChildren().add(save_button);

        VBox sidebar = new VBox(20);
        sidebar.setPrefWidth(200);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #121212;");

        Label home = new Label("Home");
        Label library = new Label("Library");
        Label folders = new Label("Folders");

        sidebar.getChildren().addAll(home, library, folders);

        VBox mainContent = new VBox(40);
        mainContent.setPadding(new Insets(40));
        mainContent.setAlignment(Pos.TOP_CENTER);

        mainContent.getChildren().addAll(
                define_flashcards_label,
                list_flashcards,
                cancel_save
        );
        HBox root = new HBox();
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(mainContent);
        scroll.setFitToWidth(true);
        root.getChildren().addAll(sidebar, scroll);
        Scene scene = new Scene(root, 900, 600);

        scene.getStylesheets().add(
                Scene3Controller.class.getResource("/cs151/application/createDeck.css").toExternalForm()
        );

        stage.setScene(scene);
        stage.show();
    }
    private static class CreateFlashcard {
        Button add_card_button;
        Button delete_card_button;
        TextField front_field;
        TextArea back_area;

        CreateFlashcard() {
            add_card_button = new Button("Add");
            add_card_button.setMinWidth(50);

            delete_card_button = new Button("Del");
            delete_card_button.setMinWidth(50);

            front_field = new TextField();
            front_field.setPromptText("Front");
            front_field.setMaxWidth(Double.MAX_VALUE);

            back_area = new TextArea();
            back_area.setPromptText("Back");
            back_area.setPrefRowCount(3);
            back_area.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(back_area, Priority.ALWAYS);

            add_card_button = new Button("Add");
            delete_card_button = new Button("Delete");
        }
        public VBox create() {

            VBox card = new VBox(20);
            card.getStyleClass().add("card");
            card.setAlignment(Pos.CENTER);
            card.setMaxWidth(600);

            // FRONT ROW
            HBox frontRow = new HBox(15);
            frontRow.setAlignment(Pos.CENTER_LEFT);

            Label frontLabel = new Label("Front:");
            frontLabel.setMinWidth(50);
            HBox.setHgrow(front_field, Priority.ALWAYS);

            frontRow.getChildren().addAll(frontLabel, front_field);

            // BACK ROW
            HBox backRow = new HBox(15);
            backRow.setAlignment(Pos.CENTER_LEFT);

            Label backLabel = new Label("Back:");
            backLabel.setMinWidth(50);
            HBox.setHgrow(back_area, Priority.ALWAYS);

            backRow.getChildren().addAll(backLabel, back_area);

            // BUTTON ROW
            HBox buttonRow = new HBox(20);
            buttonRow.setAlignment(Pos.CENTER);

            buttonRow.getChildren().addAll(add_card_button, delete_card_button);

            // ADD EVERYTHING TO CARD
            card.getChildren().addAll(frontRow, backRow, buttonRow);
            card.setAlignment(Pos.CENTER);
            card.setSpacing(25);
            card.setPadding(new Insets(30));

            return card;
        }
    }
    private static void attachHandlers(CreateFlashcard flashcard) {
        flashcard.add_card_button.setOnAction(e -> {
            CreateFlashcard newCard = new CreateFlashcard();

            int index = flashcards.indexOf(flashcard) + 1;

            flashcards.add(index, newCard);
            list_flashcards.getChildren().add(index, newCard.create());

            attachHandlers(newCard);
        });

        flashcard.delete_card_button.setOnAction(e -> {
            if (flashcards.size() > 1) {
                int index = flashcards.indexOf(flashcard);
                flashcards.remove(index);
                list_flashcards.getChildren().remove(index);
            }
        });
    }


}
