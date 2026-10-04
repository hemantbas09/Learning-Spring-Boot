package in.hemant.notification;

public class EmailService implements NotificationService {


    @Override
    public void sendNotification() {
        System.out.println("Email Notification Send");
    }

}

