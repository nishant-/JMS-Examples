package jms.example.priority;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;

public class MessagePriority {

    public static void main(String[] args) throws Exception {

        InitialContext context = new InitialContext();
        String queueName = "queue/myQueue";
        Queue queue = (Queue) context.lookup(queueName);

        try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
            JMSContext jmsContext = cf.createContext()) {

            JMSProducer jmsProducer = jmsContext.createProducer();
            String [] messages = new String[] {"Message-1", "Message-2", "Message-3"};
            jmsProducer.setPriority(9);
            jmsProducer.send(queue, messages[0]); // sned message with priority = 4

            jmsProducer.setPriority(2);
            jmsProducer.send(queue, messages[1]); // sned message with priority = 6

            jmsProducer.setPriority(3);
            jmsProducer.send(queue, messages[2]); // sned message with priority = 6

            JMSConsumer consumer = jmsContext.createConsumer(queue);

            int c = messages.length;

            while (c-- > 0) {
               Message message =  consumer.receive();
               //message will be received based on priority
                //higher the priority earlier the message will be received
                System.out.println("Message received -> {"+ message.getBody(String.class) +"} ,message priority -> "
                + message.getJMSPriority());
            }

        }
    }
}
