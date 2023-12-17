
/**
 * Write a description of class BookList here.
 *
 * @author Tadhg
 * @version 10/12/2023
 */
public class BookList extends ObjectList {

    public BookList(int capacity) {
        super(capacity);
    }

    public Book getBook(int index) {
        return (Book)super.getObject(index);
    }
}
