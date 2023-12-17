
/**
 * Write a description of class ObjectList here.
 *
 * @author Tadhg
 * @version 10/12/2023
 */
import java.io.Serializable;

public class ObjectList implements Serializable{
    private Object[] objectList;
    private int total; // Counter for the number of objects in the list

    public ObjectList(int capacity) {
        this.objectList = new Object[capacity];
        this.total = 0;
    }

    public boolean add(Object object) throws ClubFullException {
        if (isFull()) {
            throw new ClubFullException("The list is full.");
        }
        objectList[total] = object;
        total++;
        return true;
    }

    public boolean isEmpty() {
        return total == 0;
    }

    public boolean isFull() {
        return total == objectList.length;
    }

    public Object getObject(int index) {
        if (index < 0 || index >= total) {
            return null; // or throw an exception
        }
        return objectList[index];
    }

    public int indexOf(Object obj) {
        for (int i = 0; i < total; i++) {
            if (objectList[i].equals(obj)) {
                return i; // Object found, return its index
            }
        }
        return -1; // Object not found
    }

    public int getTotal() {
        return total;
    }

    public boolean remove(int index) {
        if (index < 0 || index >= total) {
            return false; // or throw an exception
        }
        // Shift the elements to the left to fill the hole created by the removed element
        for (int i = index; i < total - 1; i++) {
            objectList[i] = objectList[i + 1];
        }
        objectList[total - 1] = null;
        total--;
        return true;
    }
}
