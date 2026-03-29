package cs151.application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DataAccessLayer {
    private static final String DECK_CSV = "data/Deck.csv";
    private static final String FLASHCARD_CSV = "data/Flashcard.csv";
    private static List<DeckBean> decks = new ArrayList<>();
    private static List<FlashcardBean> flashcards = new ArrayList<>();

    public DataAccessLayer() {}

// ----------------- Deck Methods -----------------------------------------------------

    // get the ArrayList of DeckBeans
    public static List<DeckBean> getDecks()
    {
        return decks;
    }

    // add deck to ArrayList
    public static void insertDeck(DeckBean d)
    {
        if (d.getDeckID() == 0) {
            d.setDeckID(getNextDeckId());
        }
        decks.add(d);
    }

    // delete deck from ArrayList
    public static void deleteDeck(int id)
    {
        for (int i = 0; i < decks.size(); i++) {
            if (decks.get(i).getDeckID() == id) {
                decks.remove(i);
                return;
            }
        }
    }

    // save ArrayList to deck.csv
    public static void writeDeck()
    {
        File folder = new File("data");
        if (!folder.exists()) {
            folder.mkdir();
        }

        try (FileWriter writer = new FileWriter(DECK_CSV)) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // read from deck.csv and put into ArrayList
    public static void readDeck()
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getNextDeckId() {
        int maxId = 0;
        for (DeckBean deck : decks) {
            if (deck.getDeckID() > maxId) {
                maxId = deck.getDeckID();
            }
        }
        return maxId + 1;
    }

// -------------------------- Flashcard Methods ----------------------------------------

    // get ArrayList of flashcards
    public static List<FlashcardBean> getFlashcards() 
    {
        return flashcards;
    }

    // add flashcard to ArrayList
    public static void insertFlashcard(FlashcardBean f) 
    {
        if (f.getFlashcardID() == 0) {
            f.setFlashcardID(getNextFlashcardId());
        }
        flashcards.add(f);
    }

    // delete flashcard from ArrayList
    public static void deleteFlashcard(int id) 
    {
        for (int i = 0; i < flashcards.size(); i++) {
            if (flashcards.get(i).getFlashcardID() == id) {
                flashcards.remove(i);
                return;
        }
    }
    }

    // get flashcards in a deck from ArrayList
    public static List<FlashcardBean> getFlashcardsByDeck(String deckName) 
    {
        List<FlashcardBean> result = new ArrayList<>();

        for (FlashcardBean card : flashcards) {
            if (card.getDeckName().equals(deckName)) {
                result.add(card);
            }
        }

        return result;
    }

    // write ArrayList to Flashcards.csv
    public static void writeFlashcards() 
    {
        try (FileWriter writer = new FileWriter(FLASHCARD_CSV)) {
            for (FlashcardBean card : flashcards) {
                String deckName = card.getDeckName();
                String front = card.getFront().replace("\n", "\\n");
                String back = card.getBack().replace("\n", "\\n");
                String status = card.getStatus();
                String creationDate = card.getCreationDate();
                String lastReviewed = card.getLastReviewed();

                writer.write(card.getFlashcardID() + "|" + deckName + "|" + front + "|" + back + "|" + status + "|" + creationDate + "|" + lastReviewed + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // read from Flashcards.csv to ArrayList
    public static void readFlashcards() {
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
                    card.setDeckName(parts[1]);
                    card.setFront(parts[2].replace("\\n", "\n"));
                    card.setBack(parts[3].replace("\\n", "\n"));
                    card.setStatus(parts[4]);
                    card.setCreationDate(parts[5]);
                    card.setLastReviewed(parts[6]);

                    flashcards.add(card);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getNextFlashcardId() {
        int maxId = 0;
        for (FlashcardBean card : flashcards) {
            if (card.getFlashcardID() > maxId) {
                maxId = card.getFlashcardID();
            }
        }
        return maxId + 1;
    }

}
