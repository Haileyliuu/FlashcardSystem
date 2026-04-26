package cs151.application.model;

public class FlashcardBean {
    private int flashcardID;
    private int deckID;
    private String front;
    private String back;
    private String status; // New, Learning, Mastered
    private String creationDate; // auto-generated
    private String lastReviewed; // auto-generated upon review


//------- getters ----------------------------------
    public int getFlashcardID(){
        return flashcardID;
    }

    public int getDeckID() {
        return deckID;
    }

    public String getFront() {
        return front;
    }

    public String getBack() {
        return back;
    }

    public String getStatus() {
        return status;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getLastReviewed() {
        return lastReviewed;
    }

//------- setters ----------------------------------
    public void setFlashcardID(int id){
        flashcardID = id;
    }

    public void setDeckID(int id) {
        deckID = id;
    }

    public void setFront(String f) {
        front = f;
    }

    public void setBack(String b) {
        back = b;
    }

    public void setStatus(String s) {
        status = s;
    }

    public void setCreationDate(String cd) {
        creationDate = cd;
    }

    public void setLastReviewed(String lr) {
        lastReviewed = lr;
    }

}
