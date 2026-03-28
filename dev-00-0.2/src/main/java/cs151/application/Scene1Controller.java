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

public class Scene1Controller {

    public Scene1Controller() {}

    public static void scene1UI(Stage stage) {
        int deck_amount = DataAccessLayer.getDecks().size();
        Label create_deck_text = new Label("Create");
        create_deck_text.getStyleClass().add("header-text");
        Button add_deck_btn = new Button("+");
        add_deck_btn.getStyleClass().add("add-card");
        add_deck_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SceneController.switchScene2(stage);
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
        title_column.setCellFactory(col -> new TableCell<DeckBean, String>() {

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

                // Make the cell clickable
                setOnMouseClicked(e -> {
                    DeckBean deck = getTableView().getItems().get(getIndex());
                    SceneController.switchScene4(stage, deck);
                });
                setStyle("-fx-cursor: hand;");
            }
        });


        TableColumn<DeckBean, String> description_column = new TableColumn<>("Description");
        description_column.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<DeckBean, Void> edit_deck_column = new TableColumn<>("Action");
        edit_deck_column.setCellFactory(col -> new TableCell<DeckBean, Void>() {
            private final Button edit_button = new Button("Edit");
            {
                edit_button.setOnAction(event -> {
                    DeckBean deck = getTableView().getItems().get(getTableRow().getIndex());
                    SceneController.switchScene3(stage, deck);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                }
                else {
                    setGraphic(edit_button);
                }
            }
        });
        title_column.setPrefWidth(150);
        description_column.setPrefWidth(500);
        edit_deck_column.setPrefWidth(50);
        table.getColumns().add(title_column);
        table.getColumns().add(description_column);
        table.getColumns().add(edit_deck_column);
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

        Scene scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add(Scene1Controller.class.getResource("/cs151/application/createDeck.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
