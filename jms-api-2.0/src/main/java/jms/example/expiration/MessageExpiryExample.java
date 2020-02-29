package jms.example.expiration;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.UUID;


public class MessageExpiryExample {

    public static void main(String[] args) throws NamingException, InterruptedException {

        InitialContext context = new InitialContext();
        String queueName = "queue/myQueue";
        Queue queue = (Queue) context.lookup(queueName);

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

        }

    }
}
