package jms.example;

import jms.example.config.JMSLocalConfig;

import javax.jms.*;
import javax.naming.InitialContext;

import static jms.example.util.JMSUtility.*;

public class SendReceiveMessageQueue {

    //JMS send recv message using JMS 1.1 API

    public static void main(String[] args) {

        JMSLocalConfig sessionConnection = JMSLocalConfig.getInstance();
        Session session = sessionConnection.getSession();
        Connection connection = sessionConnection.getConnection();
        InitialContext context = sessionConnection.getContext();
        String queueNme = "queue/myQueue";
        String message = "A simple message";

        try {
            Queue queue = getQueue(context, queueNme);

            sendMessageToQueue(session, message, queue);
            receiveSingleMessageFromQueue(session, connection, queue);

        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            cleanUp(session, connection);
        }
    }

}
