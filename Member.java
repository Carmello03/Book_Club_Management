
/**
 * Write a description of class Member here.
 *
 * @author Tadhg
 * @version 10/12/2023
 */
public class Member extends Person {
    private PaymentList payments;
    private int maxNoOfPayments;
    private boolean paid;

    public Member(String name, String address, String phone) {
        super(name, address, phone);
        this.maxNoOfPayments = 12;
        this.payments = new PaymentList(maxNoOfPayments);
        this.paid = false;
    }

    public boolean makePayment(Payment payment) throws ClubFullException {
        if (!payments.isFull()) {
            payments.add(payment);
            if(payments.calculateTotalPaid() == 60) {
                paid = true;
            }
            return true;
        } else {
            throw new ClubFullException("Maximum number of payments reached.");
        }
    }

    public PaymentList getPayment() {
        return payments;
    }

    public boolean getPaid() {
        return paid;
    }

    @Override
    public String toString() {
        return "Member{" +
               "Name='" + getName() + '\'' +
               ", Address='" + getAddress() + '\'' +
               ", Phone='" + getPhone() + '\'' +
               ", Payments=" + payments +
               ", MaxNoOfPayments=" + maxNoOfPayments +
               ", Paid=" + paid +
               '}';
    }

    public void print() {
        System.out.println(this.toString());
    }
}
