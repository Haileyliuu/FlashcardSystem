package cs151.application;

import javafx.scene.control.Button;

public class DeckBean {
    private int deckID;
    private String title;
    private String description;
    Button button;

    public int getDeckID()
    {
        return deckID;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDeckID(int id)
    {
        deckID = id;
    }

    public void setTitle(String newTitle)
    {
        title = newTitle;
    }

    public void setDescription(String newDesc)
    {
        description = newDesc;
    }

    public void setButton(Button newBut) {button = newBut;}

    public Button getButton() {return button;}
}
