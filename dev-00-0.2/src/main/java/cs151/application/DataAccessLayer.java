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
    private static List<DeckBean> decks = new ArrayList<>();

    public DataAccessLayer() {}

    // get the ArrayList of DeckBeans
    public static List<DeckBean> getDecks()
    {
        return decks;
    }

    // add to ArrayList
    public static void insertDeck(DeckBean d)
    {
        decks.add(d);
    }

    // delete from ArrayList
    public static void deleteDeck(String title)
    {
        for (int i = 0; i < decks.size(); i++) {
            if (decks.get(i).getTitle().equals(title)) {
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

                writer.write(title + "|" + description + "\n");
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
                String[] parts = line.split("\\|", 2);

                if (parts.length == 2) {
                    DeckBean deck = new DeckBean();
                    deck.setTitle(parts[0]);
                    deck.setDescription(parts[1]);
                    decks.add(deck);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
