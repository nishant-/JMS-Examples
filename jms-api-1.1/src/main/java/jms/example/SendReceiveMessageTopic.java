package jms.example;


import jms.example.config.JMSLocalConfig;

import javax.jms.*;
import javax.naming.InitialContext;

import static jms.example.util.JMSUtility.cleanUp;
import static jms.example.util.JMSUtility.getTopic;

public class SendReceiveMessageTopic {

    public static void main(String[] args) {

        JMSLocalConfig jmsLocalConfig = JMSLocalConfig.getInstance();
        InitialContext context = jmsLocalConfig.getContext();
        String topicName = "topic/myTopic";
        Topic topic = getTopic(context, topicName);
        Connection connection = jmsLocalConfig.getConnection();
        Session session = jmsLocalConfig.getSession();

        try {
            MessageProducer messageProducer = session.createProducer(topic);
            MessageConsumer messageConsumer = session.createConsumer(topic);
            MessageConsumer messageConsumer2 = session.createConsumer(topic);

            String message = "Message sent to topic...";
            TextMessage textMessage = session.createTextMessage(message);
            messageProducer.send(textMessage);

            //receive the message

            connection.start();
            TextMessage fromConsumer1 = (TextMessage) messageConsumer.receive();
            System.out.println("Message from consumer1 = " + fromConsumer1.getText());
            TextMessage fromConsumer2 = (TextMessage) messageConsumer2.receive();
            System.out.println("Message from consumer2 = " + fromConsumer1.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            cleanUp(session, connection);
        }
    }
}
