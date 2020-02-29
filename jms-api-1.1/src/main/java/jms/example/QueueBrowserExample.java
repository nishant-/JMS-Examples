package jms.example;

import jms.example.config.JMSLocalConfig;
import jms.example.util.JMSUtility;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Enumeration;

public class QueueBrowserExample {

    public static void main(String[] args) {

        JMSLocalConfig jmsLocalConfig = JMSLocalConfig.getInstance();

        Session session = jmsLocalConfig.getSession();
        InitialContext context = jmsLocalConfig.getContext();
        Connection connection = jmsLocalConfig.getConnection();

        String queueNme = "queue/myQueue";

        Queue queue = JMSUtility.getQueue(context, queueNme);
        try {
            MessageProducer messageProducer = session.createProducer(queue);

            TextMessage message1 = session.createTextMessage("Message - 1");
            TextMessage message2 = session.createTextMessage("Message - 2");

            messageProducer.send(message1);
            messageProducer.send(message2);

            //create queue browser
            QueueBrowser browser = session.createBrowser(queue);
            Enumeration<TextMessage> enumeration = browser.getEnumeration();//returns enumeration of messages
            while (enumeration.hasMoreElements()) {

                TextMessage message = enumeration.nextElement();
                System.out.println("Browsing messages from the queue -> " + message.getText());
                //the message is not consumed yet!!!
                //you must receive the message to consume it, then only it will be removed from the queue.

            }
            //for each message you need to have a receiver
            JMSUtility.receiveAllMessagesFromQueue(session, connection, queue);

        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            JMSUtility.cleanUp(session, connection);
        }
    }
}
