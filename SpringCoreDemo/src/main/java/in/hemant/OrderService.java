package in.hemant;

public class OrderService {
    private PaymentService paymentService= new PaymentService();
    public void placeOrder(){
        paymentService.pay();
        System.out.println("Order Placed");

    }
}
