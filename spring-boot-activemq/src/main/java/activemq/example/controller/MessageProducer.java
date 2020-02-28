package activemq.example.controller;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;

@RestController
@RequestMapping("/produce")
public class MessageProducer {

    private JmsTemplate jmsTemplate;
    private Queue queue;

    public MessageProducer(JmsTemplate jmsTemplate, Queue queue) {
        this.jmsTemplate = jmsTemplate;
        this.queue = queue;
    }

    @GetMapping("/{message}")
    public String produce(@PathVariable("message") final String message) {

        Queue destination = queue;
        jmsTemplate.convertAndSend(destination, message);
        return "published message " + message;

    }
}
