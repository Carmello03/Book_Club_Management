
/**
 * Write a description of class Payment here.
 *
 * @author Tadhg
 * @version 10/12/2023
 */
import java.io.Serializable;

public class Payment implements Serializable{
    private String month;
    private double amount;

    // Constructor to initialize month and amount
    public Payment(String month, double amount) {
        this.month = month;
        this.amount = amount;
    }

    // Getter for month
    public String getMonth() {
        return month;
    }

    // Getter for amount
    public double getAmount() {
        return amount;
    }

    // toString method to represent the Payment object as a String
    public String toString() {
        return "Month: " + month + ", Amount: " + amount;
    }

    // Method to print the Payment details to the console
    public void print() {
        System.out.println(this.toString());
    }
}
