package jms.example.expiration;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Enumeration;
import java.util.UUID;

public class AccessExpiredMessages {

    public static void main(String[] args) throws Exception {

        InitialContext context = new InitialContext();
        String queueName = "queue/myQueue";
        String expiryQueueName = "queue/expiryQueue";
        Queue queue = (Queue) context.lookup(queueName);
        Queue expiryQueue = (Queue) context.lookup(expiryQueueName);

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext()) {

            String message = UUID.randomUUID().toString();
            TextMessage textMessage = jmsContext.createTextMessage(message);
            JMSProducer producer = jmsContext.createProducer();
            producer.setTimeToLive(5000); //message is available for consumption for 5000 ms,
            //after that it goes to expiry Q
            producer.send(queue, message);
            //sleep on current thread so that message expires
            Thread.sleep(5000);


            Message receivedMessage = jmsContext.createConsumer(queue).receive(5000);//after 5 seconds receive will be unblocked
            System.out.println("Message received is : " + receivedMessage); //will print null because message has expired

            reciveAllExpiredMessages(expiryQueue, jmsContext);
        }

    }

    public static void reciveAllExpiredMessages(Queue expiryQueue, JMSContext jmsContext) throws JMSException {
        QueueBrowser queueBrowser = jmsContext.createBrowser(expiryQueue);
        Enumeration<Message> queEnumeration = queueBrowser.getEnumeration();

        int c = 0;
        while (queEnumeration.hasMoreElements()) {
            queEnumeration.nextElement();
            c++;
        }

        JMSConsumer consumer = jmsContext.createConsumer(expiryQueue);
        while(c-- > 0) {
            Message tmp = consumer.receive();
            System.out.println("expired message is " + tmp.getBody(String.class));
        }
    }
}
