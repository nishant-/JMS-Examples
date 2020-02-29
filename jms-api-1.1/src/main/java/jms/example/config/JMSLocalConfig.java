package jms.example.config;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSLocalConfig {

    private Session session;
    private Connection connection;
    private InitialContext context;

    private static final JMSLocalConfig instance = new JMSLocalConfig();

    private JMSLocalConfig() {
        try {
            context = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
            connection = connectionFactory.createConnection();
            session = connection.createSession();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static JMSLocalConfig getInstance() {
        return instance;
    }

    public Session getSession() {
        return session;
    }

    public Connection getConnection() {
        return connection;
    }

    public InitialContext getContext() {
        return context;
    }
}
