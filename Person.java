
/**
 * Write a description of class Person here.
 *
 * @author Tadhg
 * @version 10/12/2023
 */
import java.io.Serializable;

public class Person implements Serializable {
    private String name;
    private String address;
    private String phone;

    public Person(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String toString() {
        return "Name: " + name + ", Address: " + address + ", Phone: " + phone;
    }

    public void print() {
        System.out.println(this.toString());
    }
}
