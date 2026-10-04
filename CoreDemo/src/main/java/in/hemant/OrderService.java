package in.hemant;

import in.hemant.notification.EmailService;
import in.hemant.notification.NotificationService;
import in.hemant.notification.PopUpNotification;
import in.hemant.notification.SmsService;

public class OrderService {
    NotificationService notification;

    public OrderService(NotificationService notification){
        this.notification=notification;
    }

    public void placeholder(){
        System.out.println("Order Placed");
        notification.sendNotification();
    }
}
