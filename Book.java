
/**
 * Write a description of class Book here.
 *
 * @author Tadhg
 * @version 10/12/2023
 */
import java.io.Serializable;

public class Book implements Serializable{
    private String title;
    private String genre;
    private String writer;

    // Constructor
    public Book(String title, String genre, String writer) {
        this.title = title;
        this.genre = genre;
        this.writer = writer;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    // toString method
    @Override
    public String toString() {
        return "Book{" +
               "title='" + title + '\'' +
               ", genre='" + genre + '\'' +
               ", writer='" + writer + '\'' +
               '}';
    }

    // print method
    public void print() {
        System.out.println(this.toString());
    }
}
