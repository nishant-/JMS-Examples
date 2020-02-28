package activemq.example.listener;

import activemq.example.config.ConfigConstants;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    // destination is the queue where the messages are written
    @JmsListener(destination = ConfigConstants.QUEUE_NAME)
    public void listener(String message) {
        System.out.println("Message received = " + message);
    }
}
