package artemis.demo.senders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    private JmsTemplate jmsTemplate;

    @Value("${springjms.myQ}")
    private String queue;

    public MessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }


    public void send(String message) {
        //convertAndSend method converts the message
        // to a JMS text message and sends it to the Q
        jmsTemplate.convertAndSend(queue, message);
    }

}
