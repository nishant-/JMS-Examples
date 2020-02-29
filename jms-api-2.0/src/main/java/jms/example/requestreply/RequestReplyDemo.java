package jms.example.requestreply;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;

public class RequestReplyDemo {

    public static void main(String[] args) throws Exception {

        InitialContext context = new InitialContext();
        String requestQueueName = "queue/requestQ";
        String replyQueueName = "queue/replyQ";

        Queue requestQueue = (Queue) context.lookup(requestQueueName);
        Queue replyQueue = (Queue) context.lookup(replyQueueName);

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()) {

            JMSProducer requestProducer = jmsContext.createProducer();
            requestProducer.send(requestQueue, "Request message");

            JMSConsumer requestConsumer = jmsContext.createConsumer(requestQueue);
            String requestMessage = requestConsumer.receiveBody(String.class);
            System.out.println("Message received = " + requestMessage);

            JMSProducer replyProducer = jmsContext.createProducer();
            requestProducer.send(replyQueue, "Reply message");

            JMSConsumer replyConsumer = jmsContext.createConsumer(replyQueue);
            Message replyMessage = replyConsumer.receive();
            System.out.println("Message received = " + replyMessage.getBody(String.class));

        }
    }
}
