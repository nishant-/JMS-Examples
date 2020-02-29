package jms.example.properties;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;


public class MessagePropertiesExample {

    public static void main(String[] args) throws Exception {

        InitialContext context = new InitialContext();
        String queueName = "queue/myQueue";
        Queue queue = (Queue) context.lookup(queueName);

        try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
            JMSContext jmsContext = cf.createContext()) {

            String message = "I will be delayed";
            TextMessage textMessage = jmsContext.createTextMessage(message);
            JMSProducer producer = jmsContext.createProducer();
            //set properties on the message
            textMessage.setIntProperty("One",1);
            textMessage.setStringProperty("Broker", "Artemis");
            producer.send(queue, textMessage);

            //receive the message
            JMSConsumer consumer = jmsContext.createConsumer(queue);
            TextMessage receivedMessage = (TextMessage) consumer.receive();

            //retrieve the properties from the message
            int value = receivedMessage.getIntProperty("One");
            String strValue = receivedMessage.getStringProperty("Broker");

            System.out.println("int value = " + value);
            System.out.println("Broker = " + strValue);

        }
    }
}
