package jms.example.replytoheader;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;

public class ReplyToHeaderExample {

    public static void main(String[] args) throws Exception {

        InitialContext context = new InitialContext();
        String requestQueueName = "queue/requestQ";
        String replyQueueName = "queue/replyQ";

        Queue requestQueue = (Queue) context.lookup(requestQueueName);
        Queue replyQueue = (Queue) context.lookup(replyQueueName);

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()) {

            JMSProducer requestProducer = jmsContext.createProducer();
            String requestText = "Request message";
            TextMessage requestTextMessage = jmsContext.createTextMessage(requestText);
            //set the header - reply Q
            requestTextMessage.setJMSReplyTo(requestQueue);
            //send this message to request Q
            requestProducer.send(requestQueue, requestTextMessage);

            JMSConsumer requestConsumer = jmsContext.createConsumer(requestQueue);
            TextMessage requestMessage = (TextMessage) requestConsumer.receive();

            System.out.println("Message received = " + requestMessage.getText());

            JMSProducer replyProducer = jmsContext.createProducer();
            //now get the reply queue name from the request message
            //no need to specify the destination for the reply message
            //it is dynamically obtained from the request message replyTo header
            Destination destinationName = requestMessage.getJMSReplyTo();
            replyProducer.send(destinationName, "Reply message");

            JMSConsumer replyConsumer = jmsContext.createConsumer(destinationName);
            Message replyMessage = replyConsumer.receive();
            System.out.println("Message received = " + replyMessage.getBody(String.class));

        }
    }
}
