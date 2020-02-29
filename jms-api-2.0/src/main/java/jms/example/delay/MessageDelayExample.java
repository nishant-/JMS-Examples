package jms.example.delay;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class MessageDelayExample {

    public static void main(String[] args) throws NamingException {

        InitialContext context = new InitialContext();
        String queueName = "queue/myQueue";
        Queue queue = (Queue) context.lookup(queueName);

        try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
            JMSContext jmsContext = cf.createContext()) {

            String delayedTextMessage = "I will be delayed";
            JMSProducer producer = jmsContext.createProducer();
            producer.setDeliveryDelay(10000); // delivery of this message is delayed by 10 seconds sec
            producer.send(queue, delayedTextMessage);
            String s = jmsContext.createConsumer(queue).receiveBody(String.class);
            System.out.println("Message received is : " + s);
        }

    }
}
