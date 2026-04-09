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

public class Scene2Controller {

    public Scene2Controller() {}

    public static void scene2UI(Stage stage) {
        stage.setTitle("Define deck");
        Label define_deck_label = new Label("Define Deck");
        define_deck_label.setFont(Font.font("", 50));

        Label required_name_label = new Label("Deck name:");
        required_name_label.setFont(Font.font("",20));
        TextField deck_name_field = new TextField();
        deck_name_field.setMaxWidth(300);
        deck_name_field.textProperty().addListener((obs, oldText, newText) -> {
            deck_name_field.getStyleClass().remove("error");
        });

        Label deck_description_label = new Label("Deck description (optional):");
        deck_description_label.setTextFill(Color.GREY);
        TextArea deck_description_area = new TextArea();
        deck_description_area.setMaxWidth(400);
        deck_description_area.setMaxHeight(100);
        deck_description_area.setWrapText(true);

        HBox cancel_save_box = new HBox();
        cancel_save_box.setAlignment(Pos.CENTER);
        cancel_save_box.setSpacing(40);
        Button cancel_button = new Button("Cancel");
        Button save_button = new Button("Save");
        cancel_save_box.getChildren().add(cancel_button);
        cancel_save_box.getChildren().add(save_button);

        cancel_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SceneController.switchScene1(stage);
            }
        });
        save_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String trimmed_name = deck_name_field.getText().replaceAll(" ", "");
                if(trimmed_name.isEmpty()){
                    deck_name_field.clear();
                    deck_name_field.setPromptText("Deck name is required");
                    deck_name_field.getStyleClass().add("error");
                }
                else {
                    boolean found = false;
                    for (int i = 0; i < DataAccessLayer.getDecks().size(); i++) {
                        String check = DataAccessLayer.getDecks().get(i).getTitle().replaceAll(" ", "");
                        if (trimmed_name.equals(check)) {
                            deck_name_field.clear();
                            deck_name_field.setPromptText("Title has already been used");
                            deck_name_field.getStyleClass().add("error");
                            found = true;
                        }
                    }
                    if (!found) {
                        DeckBean new_deck = new DeckBean();
                        new_deck.setTitle(deck_name_field.getText());
                        new_deck.setDescription(deck_description_area.getText());
                        DataAccessLayer.insertDeck(new_deck);
                        DataAccessLayer.writeDeck();
                        SceneController.switchScene1(stage);
                    }
                }
            }
        });


        VBox define_deck_menu = new VBox();
        define_deck_menu.setAlignment(Pos.TOP_CENTER);
        define_deck_menu.setSpacing(20);
        define_deck_menu.getChildren().add(define_deck_label);
        define_deck_menu.getChildren().add(required_name_label);
        define_deck_menu.getChildren().add(deck_name_field);
        define_deck_menu.getChildren().add(deck_description_label);
        define_deck_menu.getChildren().add(deck_description_area);
        define_deck_menu.getChildren().add(cancel_save_box);
        Scene scene = new Scene(define_deck_menu, 900, 600);
        scene.getStylesheets().add(Scene2Controller.class.getResource("/cs151/application/createDeck.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
