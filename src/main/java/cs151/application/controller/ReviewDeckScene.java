package cs151.application.controller;

import cs151.application.model.DataAccessLayer;
import cs151.application.model.DeckBean;
import cs151.application.model.FlashcardBean;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReviewDeckScene implements Initializable, SceneInterface {
    public ReviewDeckScene() {}
    public ReviewDeckScene(DeckBean deck) {
        this.deck = deck;
    }

    public void show(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cs151/application/reviewdeck.fxml"));
            Scene scene = new Scene(loader.load(), 900, 700);
            scene.getStylesheets().add(
                    SceneController.class.getResource("/cs151/application/createDeck.css").toExternalForm()
            );

            ReviewDeckScene controller = loader.getController();
            controller.setDeck(deck);

            stage.setScene(scene);
            stage.setTitle("Review " + deck.getTitle());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private Label deckName;
    @FXML private Label deckDescription;
    @FXML private ComboBox<String> filter;
    @FXML private Label flashcardAmount;
    @FXML private Button editButton;
    @FXML private Label frontLabel;
    @FXML private Label backLabel;
    @FXML private TextArea frontEdit;
    @FXML private TextArea backEdit;
    @FXML private VBox statusWrapper;
    @FXML private Label status;
    @FXML private ComboBox<String> statusEdit;
    @FXML private Label creation;
    @FXML private Label review;
    @FXML private Button nextButton;
    @FXML private Button prevButton;
    @FXML private Button saveButton;
    @FXML private Button backButton;
    @FXML private Button flipButton;
    @FXML private BorderPane flashcard;
    @FXML private Label emptyFilter;

    private DeckBean deck;
    private List<FlashcardBean> list;
    private List<FlashcardBean> listNew = new ArrayList<>();
    private List<FlashcardBean> listLearning = new ArrayList<>();
    private List<FlashcardBean> listMastered = new ArrayList<>();
    private List<FlashcardBean> reviewList;
    private int index;
    private List<Boolean> showingFront  = new ArrayList<>();;
    //private boolean showingFront = true;
    private boolean editing = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        filter.getItems().addAll("All", "New", "Learning", "Mastered");
        filter.setValue("All");
        filter.valueProperty().addListener((obs, oldVal, newVal) -> {
            DataAccessLayer.readFlashcards();
            resetList();
            setReviewList(newVal);
            if (reviewList.size() <= 1) {
                nextButton.setDisable(true);
                nextButton.setOpacity(0.5);
            }
            else {
                nextButton.setDisable(false);
                nextButton.setOpacity(1);
            }
            prevButton.setDisable(true);
            prevButton.setOpacity(0.5);
            flashcardAmount.setText(reviewList.size() + " Flashcards");
        });

        backEdit.setVisible(false);
        backEdit.setManaged(false);
        frontEdit.setVisible(false);
        frontEdit.setManaged(false);
        backLabel.setVisible(false);
        backLabel.setManaged(false);
        statusEdit.setVisible(false);
        statusEdit.setManaged(false);
        emptyFilter.setVisible(false);
        emptyFilter.setManaged(false);

        flipButton.setOnAction(flip -> {
            flipCard();
            if (!frontLabel.getText().equals(frontEdit.getText()) ||
                !backLabel.getText().equals(backEdit.getText()))
            {
                reviewList.get(index).setLastReviewed(LocalDate.now().toString());
            }
            frontLabel.setText(frontEdit.getText());
            reviewList.get(index).setFront(frontLabel.getText());
            backLabel.setText(backEdit.getText());
            reviewList.get(index).setBack(backLabel.getText());
        });

        frontEdit.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                if (!frontLabel.getText().equals(frontEdit.getText())) {
                    reviewList.get(index).setLastReviewed(LocalDate.now().toString());
                    review.setText("Last reviewed: " + LocalDate.now().toString());
                }
                frontLabel.setText(frontEdit.getText());
                reviewList.get(index).setFront(frontLabel.getText());
                frontEdit.setManaged(false);
                frontEdit.setVisible(false);
                frontLabel.setVisible(true);
                frontLabel.setManaged(true);

                editing = false;
                editButton.setText("Edit");
            }
        });
        frontLabel.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                frontLabel.setManaged(false);
                frontLabel.setVisible(false);
                frontEdit.setManaged(true);
                frontEdit.setVisible(true);

                editing = true;
                editButton.setText("Done");
            }
        });
        backEdit.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                if (!backLabel.getText().equals(backEdit.getText())) {
                    reviewList.get(index).setLastReviewed(LocalDate.now().toString());
                    review.setText("Last reviewed: " + LocalDate.now().toString());
                }
                backLabel.setText(backEdit.getText());
                reviewList.get(index).setBack(backLabel.getText());
                backEdit.setVisible(false);
                backEdit.setManaged(false);
                backLabel.setVisible(true);
                backLabel.setManaged(true);

                editing = false;
                editButton.setText("Edit");
            }
        });
        backLabel.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                backLabel.setManaged(false);
                backLabel.setVisible(false);
                backEdit.setManaged(true);
                backEdit.setVisible(true);

                editing = true;
                editButton.setText("Done");
            }
        });
        statusEdit.getItems().addAll("New", "Learning", "Mastered");
        statusEdit.setOnAction(e -> {
            statusEdit.setVisible(false);
            statusEdit.setManaged(false);
            statusWrapper.setVisible(true);
            statusWrapper.setManaged(true);
            status.setText(statusEdit.getValue());
            reviewList.get(index).setStatus(status.getText());
            reviewList.get(index).setLastReviewed(LocalDate.now().toString());
        });
        statusWrapper.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                statusWrapper.setManaged(false);
                statusWrapper.setVisible(false);
                statusEdit.setVisible(true);
                statusEdit.setManaged(true);
                statusEdit.setValue(status.getText());
            }
        });
        prevButton.setDisable(true);
        prevButton.setOpacity(0.5);
        prevButton.setOnAction(e -> {
            index--;
            setupFlashcard();
            nextButton.setDisable(false);
            nextButton.setOpacity(1);
            if (index == 0) {
                prevButton.setDisable(true);
                prevButton.setOpacity(0.5);
            }
        });
        nextButton.setOnAction(e -> {
            index++;
            setupFlashcard();
            prevButton.setDisable(false);
            prevButton.setOpacity(1);
            if (index == reviewList.size()-1) {
                nextButton.setDisable(true);
                nextButton.setOpacity(0.5);
            }
        });
    }

    public void setDeck(DeckBean deck) {
        this.deck = deck;

        DataAccessLayer.readFlashcards();

        deckName.setText(deck.getTitle());
        deckDescription.setText(deck.getDescription());
        resetList();
        reviewList = list;

        resetShowingFront();

        flashcardAmount.setText(reviewList.size() + " Flashcards");
        index = 0;
        if (reviewList.size() <= 1) {
            nextButton.setDisable(true);
            nextButton.setOpacity(0.5);
        }
        else {
            nextButton.setDisable(false);
            nextButton.setOpacity(1);
        }
        setupFlashcard();
    }

    private void setupFlashcard() {
        if (!reviewList.isEmpty()) {
            frontLabel.setText(reviewList.get(index).getFront());
            frontEdit.setText(reviewList.get(index).getFront());
            backLabel.setText(reviewList.get(index).getBack());
            backEdit.setText(reviewList.get(index).getBack());
            status.setText(reviewList.get(index).getStatus());
            creation.setText("Created: " + reviewList.get(index).getCreationDate());
            review.setText("Last reviewed: " + reviewList.get(index).getLastReviewed());

            stopEditing();

            if (showingFront.get(index)) {
                frontLabel.setVisible(true);
                frontLabel.setManaged(true);
                backLabel.setVisible(false);
                backLabel.setManaged(false);
            } else {
                frontLabel.setVisible(false);
                frontLabel.setManaged(false);
                backLabel.setVisible(true);
                backLabel.setManaged(true);
            }
        }
    }

    private void resetList() {
        list = DataAccessLayer.getFlashcardsByDeck(deck.getDeckID());
        listNew.clear();
        listLearning.clear();
        listMastered.clear();
        for (FlashcardBean f : list) {
            switch (f.getStatus()) {
                case "New":
                    listNew.add(f);
                    break;
                case "Learning":
                    listLearning.add(f);
                    break;
                case "Mastered":
                    listMastered.add(f);
            }
        }
        index = 0;
    }

    private void setReviewList(String newVal) {
        if (newVal.equals("All")) {
            reviewList = list;
        }
        if (newVal.equals("New")) {
            reviewList = listNew;
        }
        if (newVal.equals("Learning")) {
            reviewList = listLearning;
        }
        if (newVal.equals("Mastered")) {
            reviewList = listMastered;
        }

        resetShowingFront();

        index = 0;
        checkFilter();
        setupFlashcard();
    }

    private boolean checkFilter() {
        if (reviewList.isEmpty()) {
            flashcard.setVisible(false);
            flashcard.setManaged(false);
            flashcardAmount.setVisible(false);
            flashcardAmount.setManaged(false);
            emptyFilter.setManaged(true);
            emptyFilter.setVisible(true);
        }
        else {
            flashcard.setVisible(true);
            flashcard.setManaged(true);
            flashcardAmount.setVisible(true);
            flashcardAmount.setManaged(true);
            emptyFilter.setManaged(false);
            emptyFilter.setVisible(false);
        }
        return reviewList.isEmpty();
    }

    @FXML
    private void flipCard() {
        if (reviewList == null || reviewList.isEmpty()) return;
        if (showingFront == null || showingFront.isEmpty()) return;
        
        stopEditing();

        if (showingFront.get(index)) {
            frontEdit.setVisible(false);
            frontEdit.setManaged(false);
            backEdit.setManaged(false);
            backEdit.setVisible(false);
            frontLabel.setVisible(false);
            frontLabel.setManaged(false);
            backLabel.setVisible(true);
            backLabel.setManaged(true);
            reviewList.get(index).setLastReviewed(LocalDate.now().toString());
            review.setText("Last reviewed: " + LocalDate.now());
            showingFront.set(index, false);
        }
        else {
            frontEdit.setVisible(false);
            frontEdit.setManaged(false);
            backEdit.setManaged(false);
            backEdit.setVisible(false);
            frontLabel.setManaged(true);
            frontLabel.setVisible(true);
            backLabel.setVisible(false);
            backLabel.setManaged(false);
            showingFront.set(index, true);
        }
    }

    private void resetShowingFront() {
    showingFront.clear();
    for (int i = 0; i < reviewList.size(); i++) {
        showingFront.add(true);
    }
}

    @FXML
    private void handleBack() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        SceneController.switchScene(stage, new MainMenuScene());
    }

    @FXML
    private void handleSave() {
        DataAccessLayer.writeFlashcards();
         Stage stage = (Stage) saveButton.getScene().getWindow();
         SceneController.switchScene(stage, new MainMenuScene());
    }

    @FXML
    private void handleEditToggle() {
        if (reviewList == null || reviewList.isEmpty()) return;

        if (!editing) {
            editing = true;
            editButton.setText("Done");

            if (showingFront.get(index)) {
                frontEdit.setText(frontLabel.getText());
                frontLabel.setVisible(false);
                frontLabel.setManaged(false);
                frontEdit.setVisible(true);
                frontEdit.setManaged(true);
            } else {
                backEdit.setText(backLabel.getText());
                backLabel.setVisible(false);
                backLabel.setManaged(false);
                backEdit.setVisible(true);
                backEdit.setManaged(true);
            }
        } else {
            editing = false;
            editButton.setText("Edit");

            if (showingFront.get(index)) {
                frontLabel.setText(frontEdit.getText());
                reviewList.get(index).setFront(frontEdit.getText());
                reviewList.get(index).setLastReviewed(LocalDate.now().toString());
                review.setText("Last reviewed: " + LocalDate.now());

                frontEdit.setVisible(false);
                frontEdit.setManaged(false);
                frontLabel.setVisible(true);
                frontLabel.setManaged(true);
            } else {
                backLabel.setText(backEdit.getText());
                reviewList.get(index).setBack(backEdit.getText());
                reviewList.get(index).setLastReviewed(LocalDate.now().toString());
                review.setText("Last reviewed: " + LocalDate.now());

                backEdit.setVisible(false);
                backEdit.setManaged(false);
                backLabel.setVisible(true);
                backLabel.setManaged(true);
            }
        }
    }

    private void stopEditing() {
        editing = false;
        editButton.setText("Edit");

        frontEdit.setVisible(false);
        frontEdit.setManaged(false);
        backEdit.setVisible(false);
        backEdit.setManaged(false);
    }
}
