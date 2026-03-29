package cs151.application;

public class DeckBean {
    private int deckID;
    private String title;
    private String description;

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
}
