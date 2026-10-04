package in.hemant.notification;

public class SmsService implements NotificationService {

    @Override
    public void sendNotification(){
        System.out.println("Sms is send");
    }
}
