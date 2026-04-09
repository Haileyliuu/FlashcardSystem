package cs151.application;

public class FlashcardBean {
    private int flashcardID;
    private String deckName;
    private String front;
    private String back;
    private String status; // New, Learning, Mastered
    private String creationDate; // auto-generated
    private String lastReviewed; // auto-generated upon review


//------- getters ----------------------------------
    public int getFlashcardID(){
        return flashcardID;
    }

    public String getDeckName() {
        return deckName;
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

    public void setDeckName(String n) {
        deckName = n;
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
