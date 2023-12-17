
/**
 * Write a description of class PaymentList here.
 *
 * @author Tadhg
 * @version 10/12/2023
 */
public class PaymentList extends ObjectList {

    public PaymentList(int maxNoOfPayments) {
        super(maxNoOfPayments);
    }

    public Payment getPayment(int index) {
        return (Payment) getObject(index);
    }

    public double calculateTotalPaid() {
        double total = 0;
        for (int i = 0; i < this.getTotal(); i++) {
            Payment payment = getPayment(i);
            if (payment != null) {
                total += payment.getAmount();
            }
        }
        return total;
    }
}
