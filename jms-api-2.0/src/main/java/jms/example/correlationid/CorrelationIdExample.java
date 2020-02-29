package jms.example.correlationid;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;

public class CorrelationIdExample {

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
            requestTextMessage.setJMSReplyTo(replyQueue);

            //send this message to request Q
            requestProducer.send(requestQueue, requestTextMessage);

            JMSConsumer requestConsumer = jmsContext.createConsumer(requestQueue);
            TextMessage receivedRequestMessage = (TextMessage) requestConsumer.receive();

            System.out.println("Message received = " + receivedRequestMessage.getText());
            String recvdMsgJMSID = receivedRequestMessage.getJMSMessageID();
            System.out.println("Received request jms id = " + recvdMsgJMSID);

            JMSProducer replyProducer = jmsContext.createProducer();

            Destination destinationName = receivedRequestMessage.getJMSReplyTo();

            TextMessage replyTextMessage = jmsContext.createTextMessage("Reply message");
            replyTextMessage.setJMSCorrelationID(recvdMsgJMSID);
            replyProducer.send(destinationName, replyTextMessage);

            JMSConsumer replyConsumer = jmsContext.createConsumer(destinationName);
            TextMessage replyMessage = (TextMessage) replyConsumer.receive();
            System.out.println("Message received = " + replyMessage.getBody(String.class));
            System.out.println("Reply message correlation id " + replyMessage.getJMSCorrelationID());

        }
    }
}
