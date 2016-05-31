package messenger.functional.entities;

public class Message {
    private final String id;
    private final String text;
    private final String author;
    private final String time;
    private final String date;

    public Message(String id, String message, String author, String time, String date) {
        this.id = id;
        this.text = message;
        this.author = author;
        this.time = time;
        this.date = date;
    }

    public String getId() {
        return this.id;
    }

    public String getMessage() {
        return this.text;
    }

    public String getAuthor() {
        return this.author;
    }

    public String toString() {
        return "{\"text\":\"" + this.text + "\",\"author\":\"" + this.author + "\",\"time\":\"" + this.time + "\",\"date\":\"" + this.date + "\",\"id\":\"" + this.id + "\"}";
    }

    /*public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }*/
}
