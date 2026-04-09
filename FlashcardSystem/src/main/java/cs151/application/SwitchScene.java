package cs151.application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SwitchScene {
    private Stage stage;
    private Scene scene;

    public void switchScene1(Stage stage) {
        DataAccessLayer.readDeck();
        int deck_amount = DataAccessLayer.getDecks().size();
        Label create_deck_text = new Label("Create");
        create_deck_text.getStyleClass().add("header-text");
        Button add_deck_btn = new Button("+");
        add_deck_btn.getStyleClass().add("add-card");
        add_deck_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                switchScene2(stage);
            }
        });

        VBox create = new VBox();
        create.setTranslateX(10);
        create.setTranslateY(10);
        create.setSpacing(40);
        create.getChildren().add(create_deck_text);
        create.getChildren().add(add_deck_btn);

        Line line = new Line();
        line.setStartY(10);
        line.setEndY(500);
        line.setStroke(Color.WHITE);

        //***************List number of decks and all decks created*************//
        VBox deck_listing = new VBox();
        deck_listing.setSpacing(20);
        Label deck_number_text = new Label(deck_amount + " Decks");
        deck_number_text.setFont(Font.font("", 50));
        deck_number_text.setTranslateX(-10);
        deck_number_text.setTranslateY(10);

        TableView<DeckBean> table = new TableView<>();
        table.setPrefWidth(700);
        TableColumn<DeckBean, String> title_column = new TableColumn<>("Title");
        title_column.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<DeckBean, String> description_column = new TableColumn<>("Description");
        description_column.setCellValueFactory(new PropertyValueFactory<>("description"));
        title_column.setPrefWidth(150);
        description_column.setPrefWidth(550);
        table.getColumns().add(title_column);
        table.getColumns().add(description_column);
        for (int i = 0; i < deck_amount; i++) {
            table.getItems().add(DataAccessLayer.getDecks().get(i));
        }
        deck_listing.getChildren().add(deck_number_text);
        deck_listing.getChildren().add(table);
        //********************************************************************//

        TextField search = new TextField();
        search.setPromptText("Search...");
        search.setMaxWidth(400);

        HBox topBar = new HBox();
        topBar.setSpacing(20);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.getChildren().add(search);

        HBox main_menu = new HBox();
        main_menu.setSpacing(20);
        main_menu.getChildren().add(create);
        main_menu.getChildren().add(line);
        main_menu.getChildren().add(deck_listing);
        VBox root = new VBox(20);
        root.setStyle("-fx-padding: 20;");
        root.getChildren().addAll(topBar, main_menu);

        scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(getClass().getResource("/cs151/application/createDeck.css").toExternalForm());
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
        deck_name_field.textProperty().addListener((obs, oldText, newText) -> {
            deck_name_field.getStyleClass().remove("error");
        });

        Label deck_description_label = new Label("Deck description (optional):");
        deck_description_label.setTextFill(Color.GREY);
        TextArea deck_description_area = new TextArea();
        deck_description_area.setMaxWidth(400);
        deck_description_area.setMaxHeight(100);

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
                switchScene1(stage);
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
                        switchScene1(stage);
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
        scene = new Scene(define_deck_menu, 900, 600);
        scene.getStylesheets().add(getClass().getResource("/cs151/application/createDeck.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
