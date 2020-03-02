package jms.example.messagetypes;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class BytesMessageExample {

    public static void main(String[] args) throws NamingException {

        InitialContext context = new InitialContext();
        String queueName = "queue/myQueue";
        Queue queue = (Queue) context.lookup(queueName);

        try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
            JMSContext jmsContext = cf.createContext()) {
            BytesMessage bytesMessage = jmsContext.createBytesMessage();
            bytesMessage.writeUTF("Some utf-8 string");
            bytesMessage.writeBoolean(true);

            JMSProducer producer = jmsContext.createProducer();
            producer.send(queue, bytesMessage);

            BytesMessage messageReceived = (BytesMessage) jmsContext.createConsumer(queue).receive(1000);
            String s = messageReceived.readUTF();
            boolean isTrue = messageReceived.readBoolean();

            System.out.println(s +", " + isTrue);

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

}
