package jms.example.util;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Enumeration;

public class JMSUtility {

    public static void receiveSingleMessageFromQueue(Session session, Connection connection, Queue queue) throws JMSException {
        TextMessage textMessage;
        MessageConsumer messageConsumer = session.createConsumer(queue); //consumer expects a destination
        System.out.println("About to receive a message..");
        connection.start(); //tells jms provider that application is ready to recv message
        textMessage = (TextMessage) messageConsumer.receive(5000);
        System.out.println("Message received = " + textMessage.getText());
    }

    public static void receiveAllMessagesFromQueue(Session session, Connection connection, Queue queue) throws JMSException {
        TextMessage textMessage;
        MessageConsumer messageConsumer = session.createConsumer(queue); //consumer expects a destination
        int count = getMessageCountFromQueue(queue, session);

        if(count == 0) {
            throw new RuntimeException("Queue is empty");
        }
        connection.start(); //tells jms provider that application is ready to recv message
        while (count-- > 0) {
            textMessage = (TextMessage) messageConsumer.receive(5000);
            System.out.println("Message received = " + textMessage.getText());
        }
    }

    public static void sendMessageToQueue(Session session, String message, Queue queue) throws JMSException {
        MessageProducer messageProducer = session.createProducer(queue);
        TextMessage textMessage = session.createTextMessage(message);
        //send the text message via the producer
        System.out.println("Going to send a text message = " + message);
        messageProducer.send(textMessage);
        System.out.println("Message sent..");
    }

    public static Queue getQueue(InitialContext context, String queueNme) {
        try {
            return (Queue) context.lookup(queueNme);
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Topic getTopic(InitialContext context, String topicName) {
        try {
            return (Topic) context.lookup(topicName);
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void cleanUp(Session session, Connection connection) {

        if (session != null) {
            try {
                session.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getMessageCountFromQueue(Queue queue, Session session) {
        int count = 0;
        QueueBrowser browser = null;
        try {
            browser = session.createBrowser(queue);
            Enumeration<Queue> enumeration = browser.getEnumeration();
            while (enumeration.hasMoreElements()) {
                count++;
                enumeration.nextElement();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
        finally {
            if(browser != null) {
                try {
                    browser.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
        return count;
    }
}
