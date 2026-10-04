package in.hemant.notification;

public class PopUpNotification implements NotificationService {

    @Override
    public void sendNotification(){
        System.out.println("pop up is display");
    }
}
