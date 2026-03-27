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

import java.util.ArrayList;
import java.util.List;

public class Scene3Controller {
    private Scene scene;

    public Scene3Controller() {}

    public void scene3UI(Stage stage) {
        ScrollPane scrollPane = new ScrollPane();
        VBox define_flashcards = new VBox();
        define_flashcards.setSpacing(10);

        Label define_flashcards_label = new Label("Define Flashcards");
        define_flashcards_label.setFont(Font.font("", 30));
        define_flashcards_label.setTranslateX(10);
        define_flashcards_label.setTranslateY(10);

        List<CreateFlashcard> flashcards = new ArrayList<>();
        VBox list_flashcards = new VBox();
        list_flashcards.setSpacing(10);
        CreateFlashcard default_flashcard = new CreateFlashcard();
        list_flashcards.getChildren().add(default_flashcard.create());
        flashcards.add(default_flashcard);

        flashcards.getFirst().add_card_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                CreateFlashcard flashcard = new CreateFlashcard();
                list_flashcards.getChildren().add(flashcard.create());
                flashcards.add(flashcard);
                flashcards.getLast().add_card_button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        CreateFlashcard flashcard = new CreateFlashcard();
                        list_flashcards.getChildren().add(flashcard.create());
                        flashcards.add(flashcard);
                    }
                });
                flashcard.delete_card_button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if (flashcards.size() > 1) {
                            System.out.println(flashcards.size());
                            flashcards.removeLast();
                            list_flashcards.getChildren().remove(flashcards.size()-1);
                        }
                    }
                });
            }
        });
        flashcards.getFirst().delete_card_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (flashcards.size() > 1) {
                    System.out.println(flashcards.size());
                    flashcards.removeLast();
                    list_flashcards.getChildren().remove(flashcards.size()-1);
                }
            }
        });

        HBox cancel_save = new HBox();
        cancel_save.setSpacing(300);
        cancel_save.setTranslateX(200);
//        cancel_save.setTranslateY(10);
        Button cancel_button = new Button("Cancel");
        cancel_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SceneController.switchScene1(stage);
            }
        });
        Button save_button = new Button("Save");
        cancel_save.getChildren().add(cancel_button);
        cancel_save.getChildren().add(save_button);

        define_flashcards.getChildren().add(define_flashcards_label);
        define_flashcards.getChildren().add(list_flashcards);
        define_flashcards.getChildren().add(cancel_save);
        scrollPane.setContent(define_flashcards);
        scrollPane.setPadding(new Insets(0,0,10,10));
        scene = new Scene(scrollPane, 900, 600);
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
            back_area = new TextArea();
            back_area.setMaxHeight(50);
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
            Label back_label = new Label("Back: ");
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

}
