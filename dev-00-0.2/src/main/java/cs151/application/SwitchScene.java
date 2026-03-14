package cs151.application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SwitchScene {
    private Stage stage;
    private Scene scene;

    public void switchScene1(Stage stage) {
        Label create_deck_text = new Label("Create");
        create_deck_text.setFont(Font.font("", 50));
        Button add_deck_btn = new Button("+");
        add_deck_btn.setFont(Font.font("",30));
        add_deck_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                switchScene2(stage);
            }
        });

        VBox main_menu = new VBox();
        main_menu.getChildren().add(create_deck_text);
        main_menu.getChildren().add(add_deck_btn);
        scene = new Scene(main_menu, 600, 600);
        stage.setScene(scene);
        stage.show();
    }

    public void switchScene2(Stage stage) {
        Label define_deck_label = new Label("Define Deck");
        define_deck_label.setFont(Font.font("", 50));

        Label required_name_label = new Label("Deck name:");
        required_name_label.setFont(Font.font("",20));
        TextField deck_name_field = new TextField();
        deck_name_field.setMaxWidth(300);

        Label deck_description_label = new Label("Deck description (optional)");
        deck_description_label.setTextFill(Color.GREY);
        TextArea deck_description_area = new TextArea();
        deck_description_area.setMaxWidth(400);
        deck_description_area.setMaxHeight(100);

        HBox cancel_save_box = new HBox();
        cancel_save_box.setTranslateX(150);
        cancel_save_box.setTranslateY(200);
        cancel_save_box.setSpacing(200);
        Button cancel_button = new Button("Cancel");
        Button save_button = new Button("Save");
        cancel_save_box.getChildren().add(cancel_button);
        cancel_save_box.getChildren().add(save_button);

        cancel_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                switchScene1(stage);
            }
        });
        save_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                switchScene1(stage);
            }
        });


        VBox define_deck_menu = new VBox();
        define_deck_menu.getChildren().add(define_deck_label);
        define_deck_menu.getChildren().add(required_name_label);
        define_deck_menu.getChildren().add(deck_name_field);
        define_deck_menu.getChildren().add(deck_description_label);
        define_deck_menu.getChildren().add(deck_description_area);
        define_deck_menu.getChildren().add(cancel_save_box);
        scene = new Scene(define_deck_menu, 600, 600);
        stage.setScene(scene);
        stage.show();
    }
}
