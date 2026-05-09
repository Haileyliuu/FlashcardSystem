package cs151.application.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataAccessLayer {
    private static DataAccessLayer singleInstance;
    private static final String DECK_CSV = "data/Deck.csv";
    private static final String FLASHCARD_CSV = "data/Flashcard.csv";
    private static List<DeckBean> decks = new ArrayList<>();
    private static List<FlashcardBean> flashcards = new ArrayList<>();
    private static int nextDeckId = 1;
    private static int nextFlashcardId = 1;

    private DataAccessLayer() {}

    public static DataAccessLayer getSingleInstance() {
        if (singleInstance == null) {
            singleInstance = new DataAccessLayer();
        }
        return singleInstance;
    }

// ----------------- Deck Methods -----------------------------------------------------

    // get the ArrayList of DeckBeans
    public List<DeckBean> getDecks()
    {
        return decks;
    }

    // add deck to ArrayList
    public void insertDeck(DeckBean d)
    {
        d.setDeckID(nextDeckId);
        nextDeckId++;
        decks.add(d);
    }

    // delete deck from ArrayList
    public void deleteDeck(int id)
    {
        for (int i = 0; i < decks.size(); i++) {
            if (decks.get(i).getDeckID() == id) {
                decks.remove(i);
            }
        }
        for (int i = 0; i < flashcards.size(); i++) {
            if (flashcards.get(i).getDeckID() == id) {
                flashcards.remove(i);
            }
        }
    }

    // save ArrayList to deck.csv
    public void writeDeck()
    {
        File folder = new File("data");
        if (!folder.exists()) {
            folder.mkdir();
        }

        String tempPath = "data/temp.csv";
        File originalFile = new File(DECK_CSV);
        File tempFile = new File(tempPath);
        try (FileWriter writer = new FileWriter(tempPath)) {
            for (DeckBean deck : decks) {
                String title = deck.getTitle();
                String description = deck.getDescription();

                if (title == null) {
                    title = "";
                }
                if (description == null) {
                    description = "";
                }

                title = title.replace("|", ";");
                description = description.replace("|", ";");
                description = description.replace("\n", "\\n");
                

                writer.write(deck.getDeckID() + "|" + title + "|" + description + "\n");
            }
            writer.write("NextID|" + nextDeckId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        originalFile.delete();
        tempFile.renameTo(originalFile);
    }

    // read from deck.csv and put into ArrayList
    public void readDeck()
    {
        decks.clear();

        File file = new File(DECK_CSV);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(DECK_CSV))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 3);

                if (parts.length == 3) {
                    DeckBean deck = new DeckBean();
                    deck.setDeckID(Integer.parseInt(parts[0]));
                    deck.setTitle(parts[1]);
                    String desc = parts[2].replace("\\n", "\n");
                    deck.setDescription(desc);
                    decks.add(deck);
                }
                else {
                    nextDeckId = Integer.parseInt(parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

// -------------------------- Flashcard Methods ----------------------------------------

    // get ArrayList of flashcards
    public List<FlashcardBean> getFlashcards()
    {
        return flashcards;
    }

    // add flashcard to ArrayList
    public void insertFlashcard(FlashcardBean f)
    {
        f.setFlashcardID(nextFlashcardId);
        nextFlashcardId++;
        flashcards.add(f);
    }

    // delete flashcard from ArrayList and Database
    public void deleteFlashcard(int id)
    {
        for (int i = 0; i < flashcards.size(); i++) {
            if (flashcards.get(i).getFlashcardID() == id) {
                flashcards.remove(i);
            }
        }
    }

    // get flashcards in a deck from ArrayList
    public List<FlashcardBean> getFlashcardsByDeck(int deckID)
    {
        List<FlashcardBean> result = new ArrayList<>();

        for (FlashcardBean card : flashcards) {
            if (card.getDeckID()== deckID) {
                result.add(card);
            }
        }

        return result;
    }

    // write ArrayList to Flashcards.csv
    public void writeFlashcards()
    {
        File folder = new File("data");
        if (!folder.exists()) {
            folder.mkdir();
        }
        String tempPath = "data/temp.csv";
        File originalFile = new File(FLASHCARD_CSV);
        File tempFile = new File(tempPath);
        try (FileWriter writer = new FileWriter(tempPath)) {
            for (FlashcardBean card : flashcards) {
                int deckID = card.getDeckID();
                String front = card.getFront().replace("\n", "\\n");
                String back = card.getBack().replace("\n", "\\n");
                String status = card.getStatus();
                String creationDate = card.getCreationDate();
                String lastReviewed = card.getLastReviewed();

                writer.write(card.getFlashcardID() + "|" + deckID + "|" + front + "|" + back + "|" + status + "|" + creationDate + "|" + lastReviewed + "\n");
            }
            writer.write("NextID|" + nextFlashcardId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        originalFile.delete();
        tempFile.renameTo(originalFile);
    }

    // read from Flashcards.csv to ArrayList
    public void readFlashcards() {
        flashcards.clear();

        File file = new File(FLASHCARD_CSV);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FLASHCARD_CSV))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 7);

                if (parts.length == 7) {
                    FlashcardBean card = new FlashcardBean();
                    card.setFlashcardID(Integer.parseInt(parts[0]));
                    card.setDeckID(Integer.parseInt(parts[1]));
                    card.setFront(parts[2].replace("\\n", "\n"));
                    card.setBack(parts[3].replace("\\n", "\n"));
                    card.setStatus(parts[4]);
                    card.setCreationDate(parts[5]);
                    card.setLastReviewed(parts[6]);

                    flashcards.add(card);
                }
                else {
                    nextFlashcardId = Integer.parseInt(parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
