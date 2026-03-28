package cs151.application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Scene3Controller {

    private static List<CreateFlashcard> flashcards = new ArrayList<>();
    private static VBox list_flashcards = new VBox();

    public Scene3Controller() {}

    public static void scene3UI(Stage stage, DeckBean deck) {
        flashcards.clear();
        list_flashcards.getChildren().clear();
        DataAccessLayer.readFlashcards();
        List<FlashcardBean> flashcard_list = DataAccessLayer.getFlashcardsByDeck(deck.getTitle());

        ScrollPane scrollPane = new ScrollPane();
        VBox define_flashcards = new VBox();
        define_flashcards.setSpacing(10);

        Label define_flashcards_label = new Label("Define flashcards of deck: " + deck.getTitle());
        define_flashcards_label.setFont(Font.font("", 30));
        define_flashcards_label.setTranslateX(10);
        define_flashcards_label.setTranslateY(10);

        list_flashcards.setSpacing(10);

        CreateFlashcard first = new CreateFlashcard();
        flashcards.add(first);
        list_flashcards.getChildren().add(first.create());
        attachHandlers(first);

        HBox cancel_save = new HBox();
        cancel_save.setSpacing(300);
        cancel_save.setTranslateX(200);
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

        define_flashcards.getChildren().add(define_flashcards_label);
        define_flashcards.getChildren().add(list_flashcards);
        define_flashcards.getChildren().add(cancel_save);
        scrollPane.setContent(define_flashcards);
        scrollPane.setPadding(new Insets(0,0,10,10));
        Scene scene = new Scene(scrollPane, 900, 600);
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
            delete_card_button = new Button("Del");
            front_field = new TextField();
            front_field.setPrefWidth(300);
            back_area = new TextArea();
            back_area.setPrefHeight(50);
            back_area.setPrefWidth(300);
            back_area.setWrapText(true);
        }
        HBox create() {
            HBox info = new HBox();
            info.setSpacing(10);

            Line line = new Line();
            line.setEndY(90);

            VBox study_info = new VBox();
            study_info.setSpacing(5);
            HBox front_info = new HBox();
            Label front_label = new Label("Front: ");
            front_info.getChildren().add(front_label);
            front_info.getChildren().add(front_field);
            study_info.getChildren().add(front_info);

            HBox back_info = new HBox();
            Label back_label = new Label("Back:  ");
            back_info.getChildren().add(back_label);
            back_info.getChildren().add(back_area);
            study_info.getChildren().add(back_info);

            info.getChildren().add(add_card_button);
            info.getChildren().add(delete_card_button);
            info.getChildren().add(line);
            info.getChildren().add(study_info);
            return info;
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
