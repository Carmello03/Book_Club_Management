
/**
 * Write a description of class BookClub here.
 *
 * @author Tadhg
 * @version 10/12/2023
 */
import java.io.Serializable;

public class BookClub implements Serializable {
    private MemberList members;
    private BookList books;
    private final int maxNoOfMembers = 5;
    private int year;

    public BookClub() {
        members = new MemberList(maxNoOfMembers);
        books = new BookList(maxNoOfMembers);
        year = java.time.Year.now().getValue(); // Automatically set to the current year
    }

    public MemberList getMembersList() {
        return members;
    }

    public BookList getBookList() {
        return books;
    }

    public int getTotalMembers() {
        return members.getTotal();
    }

    public boolean deleteMember(Member member) {
        // Check if the member exists in the list
        int index = members.indexOf(member);
    
        // Remove the member from the list
        return members.remove(index);
    }

    public String toString() {
        return "BookClub{" +
               "maxNoOfMembers=" + maxNoOfMembers +
               ", year=" + year +
               ", members=" + members +
               ", books=" + books +
               '}';
    }

    public void print() {
        System.out.println(this.toString());
    }
}
