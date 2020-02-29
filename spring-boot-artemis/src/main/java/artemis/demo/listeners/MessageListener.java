package artemis.demo.listeners;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    @JmsListener(destination = "${springjms.myQ}")
    public void receive(String message) {
        System.out.println("Message received -> " + message);
    }
}
