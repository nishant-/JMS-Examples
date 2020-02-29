package jms.example.requestreply;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;




public class JMS2SendReceiveMessageApp {

    public static void main(String[] args) throws NamingException {

        InitialContext context = new InitialContext();
        String queueName = "queue/myQueue";
        Queue queue = (Queue) context.lookup(queueName);

        try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
            JMSContext jmsContext = cf.createContext()) {

            jmsContext.createProducer().send(queue, "Working with JMS 2.0");
            String s = jmsContext.createConsumer(queue).receiveBody(String.class);
            System.out.println("Message received is : " + s);

        }

    }
}
