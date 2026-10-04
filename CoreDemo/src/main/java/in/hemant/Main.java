package in.hemant;

import in.hemant.notification.EmailService;
import in.hemant.notification.NotificationService;
import in.hemant.notification.SmsService;

import javax.management.Notification;

public class Main {
    static void main() {
        NotificationService notification= new SmsService();
        OrderService order= new OrderService(notification);
        order.placeholder();;
    }
}


// 📝 Full notes:  NOTES.md (in CoreDemo project root)
// 📚 Main hub:    ../NOTES.md (LearningSpringBoot root — links all project notes)

// IOC is a principle;
//Di is approach/technique to achieve IOC;

// Today We learn about
/*

1. tight coupling
2. how we can achieve loose coupling by using interface
3. Dependency Injection nad there types:
4. IOC
5. role of spring framework.

*/
