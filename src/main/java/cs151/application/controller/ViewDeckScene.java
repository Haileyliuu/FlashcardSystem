package cs151.application.controller;

import cs151.application.model.DataAccessLayer;
import cs151.application.model.DeckBean;
import cs151.application.model.FlashcardBean;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class ViewDeckScene implements SceneInterface{
    public ViewDeckScene() {}
    public ViewDeckScene(DeckBean deck) {
        this.deck = deck;
    }

    public void show(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cs151/application/viewdeck.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 600);
            scene.getStylesheets().add(
                    SceneController.class.getResource("/cs151/application/createDeck.css").toExternalForm()
            );

            ViewDeckScene controller = loader.getController();
            controller.setDeck(deck);

            stage.setScene(scene);
            stage.setTitle(deck.getTitle());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private VBox deckView;

    @FXML
    private Button backButton;

    @FXML
    private Label deckNameLabel;

    @FXML
    private Label deckDescriptionLabel;

    @FXML
    private TextArea deckNameChange;

    @FXML
    private TextArea deckDescriptionChange;

    @FXML
    private Button saveChanges;

    @FXML
    private Label flashcardAmountLabel;

    @FXML
    private TextField searchBar;

    @FXML
    private TableView<FlashcardBean> flashcardTable;

    @FXML
    private TableColumn<FlashcardBean, String> frontColumn;

    @FXML
    private TableColumn<FlashcardBean, String> backColumn;

    @FXML
    private TableColumn<FlashcardBean, String> statusColumn;

    @FXML
    private TableColumn<FlashcardBean, String> reviewColumn;

    @FXML
    private TableColumn<FlashcardBean, String> creationColumn;

    @FXML
    private TableColumn<FlashcardBean, Void> deleteColumn;


    private DeckBean deck;
    private ObservableList<FlashcardBean> data;
    private FilteredList<FlashcardBean> filtered;

    @FXML
    public void initialize() {
        flashcardTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        flashcardTable.setRowFactory(tv -> {
            TableRow<FlashcardBean> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !row.isEmpty()) {
                    FlashcardBean deck = row.getItem();

                }
            });
            return row;
        });
        flashcardTable.setEditable(true);
        frontColumn.setEditable(true);
        frontColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getFront())
        );
        frontColumn.setCellFactory(col -> new TableCell<FlashcardBean, String>() {
            private final Label label = new Label();
            private final TextArea textArea = new TextArea();

            {
                label.setWrapText(false);
                label.setStyle("-fx-text-overrun: ellipsis;");

                textArea.setWrapText(true);
                textArea.setPrefRowCount(3);

                // Save on focus loss
                textArea.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        commitEdit(textArea.getText());
                    }
                });

                // Double-click label to start editing
                label.setOnMouseClicked(e -> {
                    if (e.getClickCount() == 2) {
                        startEdit();
                    }
                });
            }

            @Override
            public void startEdit() {
                super.startEdit();
                textArea.setText(getItem());
                setGraphic(textArea);
                textArea.requestFocus();
                textArea.selectAll();
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                String firstLine = getItem() != null ? getItem().split("\n")[0] : "";
                label.setText(firstLine);
                setGraphic(label);
            }

            @Override
            public void commitEdit(String newValue) {
                super.commitEdit(newValue);
                FlashcardBean flashcard = getTableView().getItems().get(getIndex());
                flashcard.setFront(newValue);
                flashcard.setLastReviewed(LocalDate.now().toString());
                DataAccessLayer.writeFlashcards();
                label.setText(newValue.split("\n")[0]);
                setGraphic(label);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    return;
                }

                if (isEditing()) {
                    textArea.setText(item);
                    setGraphic(textArea);
                } else {
                    label.setText(item.split("\n")[0]);
                    setGraphic(label);
                }
            }
        });
        backColumn.setEditable(true);
        backColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getBack())
        );
        backColumn.setCellFactory(col -> new TableCell<FlashcardBean, String>() {
            private final Label label = new Label();
            private final TextArea textArea = new TextArea();

            {
                label.setWrapText(false);
                label.setStyle("-fx-text-overrun: ellipsis;");

                textArea.setWrapText(true);
                textArea.setPrefRowCount(3);

                textArea.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        commitEdit(textArea.getText());
                    }
                });

                label.setOnMouseClicked(e -> {
                    if (e.getClickCount() == 2) {
                        startEdit();
                    }
                });
            }

            @Override
            public void startEdit() {
                super.startEdit();
                textArea.setText(getItem());
                setGraphic(textArea);
                textArea.requestFocus();
                textArea.selectAll();
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                String firstLine = getItem() != null ? getItem().split("\n")[0] : "";
                label.setText(firstLine);
                setGraphic(label);
            }

            @Override
            public void commitEdit(String newValue) {
                super.commitEdit(newValue);
                FlashcardBean flashcard = getTableView().getItems().get(getIndex());
                flashcard.setBack(newValue);
                flashcard.setLastReviewed(LocalDate.now().toString());
                DataAccessLayer.writeFlashcards();
                label.setText(newValue.split("\n")[0]);
                setGraphic(label);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    return;
                }

                if (isEditing()) {
                    textArea.setText(item);
                    setGraphic(textArea);
                } else {
                    label.setText(item.split("\n")[0]);
                    setGraphic(label);
                }
            }
        });
        statusColumn.setEditable(true);
        statusColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getStatus())
        );
        statusColumn.setCellFactory(col -> new TableCell<FlashcardBean, String>() {
            private final Label label = new Label();
            private final ComboBox<String> comboBox = new ComboBox<>();

            {
                comboBox.getItems().addAll("New", "Learning", "Mastered");
                comboBox.setStyle("-fx-background-color: grey; -fx-color: grey;");

                // Save on selection
                comboBox.setOnAction(e -> {
                    if (comboBox.getValue() != null) {
                        commitEdit(comboBox.getValue());
                    }
                });

                // Cancel on focus loss without selection
                comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        cancelEdit();
                    }
                });

                label.setOnMouseClicked(e -> {
                    if (e.getClickCount() == 2) {
                        startEdit();
                    }
                });
            }

            @Override
            public void startEdit() {
                super.startEdit();
                comboBox.setValue(getItem());
                setGraphic(comboBox);
                comboBox.show(); // auto-open the dropdown
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                label.setText(getItem());
                setGraphic(label);
            }

            @Override
            public void commitEdit(String newValue) {
                super.commitEdit(newValue);
                FlashcardBean flashcard = getTableView().getItems().get(getIndex());
                flashcard.setStatus(newValue);
                flashcard.setLastReviewed(LocalDate.now().toString());
                DataAccessLayer.writeFlashcards();
                label.setText(newValue);
                setGraphic(label);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    return;
                }

                if (isEditing()) {
                    comboBox.setValue(item);
                    setGraphic(comboBox);
                } else {
                    label.setText(item);
                    setGraphic(label);
                }
            }
        });
        reviewColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getLastReviewed())
        );
        creationColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getCreationDate())
        );

        deleteColumn.setCellFactory(col -> new TableCell<FlashcardBean, Void>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.getStyleClass().add("delete-button");
                deleteButton.setOnAction(event -> {
                    FlashcardBean flashcard = getTableView().getItems().get(getTableRow().getIndex());
                    DataAccessLayer.deleteFlashcard(flashcard.getFlashcardID());
                    data.remove(flashcard);
                    DataAccessLayer.writeFlashcards();
                    updateFlashcardCount();
                });
                
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });

        searchBar.textProperty().addListener((obs, oldValue, newValue) -> {
            if (filtered == null) {
                return;
            }

            String filter = newValue == null ? "" : newValue.toLowerCase();

            filtered.setPredicate(f -> {
                if (filter.isEmpty()) {
                    return true;
                }

                return contains(f.getFront(), filter)
                        || contains(f.getBack(), filter)
                        || contains(f.getStatus(), filter);
            });
        });
    }

    public void setDeck(DeckBean deck) {
        this.deck = deck;

        DataAccessLayer.readFlashcards();
        List<FlashcardBean> flashcards = DataAccessLayer.getFlashcardsByDeck(deck.getDeckID());

        deckNameLabel.setText(deck.getTitle());
        deckDescriptionLabel.setText(deck.getDescription());
        data = FXCollections.observableArrayList(flashcards);
        filtered = new FilteredList<>(data, p -> true);
        flashcardTable.setItems(filtered);
        updateFlashcardCount();
    }

    private void updateFlashcardCount() {
        int count = data == null ? 0 : data.size();
        flashcardAmountLabel.setText(count + " flashcards");
    }

    private boolean contains(String value, String filter) {
        return value != null && value.toLowerCase().contains(filter);
    }

    @FXML
    private void handleBack() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        SceneController.switchScene(stage, new MainMenuScene());
    }

    @FXML
    private void handleChange() {
        if (!deckNameChange.getText().equals("")) {
            deckNameLabel.setText(deckNameChange.getText());
        }
        if (!deckDescriptionChange.getText().equals("")) {
            deckDescriptionLabel.setText(deckDescriptionChange.getText());
        }
        deck.setTitle(deckNameLabel.getText());
        deck.setDescription(deckDescriptionLabel.getText());
        DataAccessLayer.writeDeck();
    }
}