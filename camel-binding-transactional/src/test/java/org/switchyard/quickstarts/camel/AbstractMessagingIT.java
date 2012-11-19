package org.switchyard.quickstarts.camel;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author: <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 * Date: 11/18/12
 * Time: 12:31 PM
 */
public class AbstractMessagingIT {


    private InitialContext _initialContext;

    public void setUp() throws Exception {
        _initialContext = new InitialContext();
    }

    public void tearDown() throws Exception {
        if (_initialContext != null) try { _initialContext.close(); } catch (Exception e) {}
    }

    protected ConnectionFactory getConnectionFactory(String connectionFactory) throws NamingException {
        return (ConnectionFactory) _initialContext.lookup(connectionFactory);
    }

    protected Connection getStartedConnection(ConnectionFactory connectionFactory) throws JMSException {
        final Connection connection = connectionFactory.createConnection();
        connection.start();
        return connection;
    }

    protected Session getSession(Connection connection) throws JMSException {
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    protected Queue getQueue(String queueName) throws NamingException {
        return (Queue) _initialContext.lookup(queueName);
    }

    protected MessageConsumer getConsumer(Session session, Queue queue) throws JMSException {
        return session.createConsumer(queue);
    }

    protected MessageProducer getProducer(Session session, Queue queue) throws JMSException {
        return session.createProducer(queue);
    }

    protected void assertConsumer(String queueName, MessageConsumer clientConsumer, String text) throws JMSException {
        TextMessage message = (TextMessage) clientConsumer.receive(BaseCamelMessagingIT.JMS_RECEPTION_TIMEOUT);
        assertMessage(text, message, queueName);
    }

    protected void assertMessage(String messageBody, TextMessage message, String queueName) throws JMSException {
        if (messageBody == null) {
            assertThat("The message should not have arrived to "+queueName+" queue.", message, is(nullValue()));
        } else {
            assertThat("The message should have arrived to "+queueName+" queue.", message, is(notNullValue()));
            assertThat("The message should have the right content.", message.getText(), is(equalTo(messageBody)));
        }
    }

    protected void sendTextToQueue(final String connectionFactoryName, final String queueName, final String text) throws Exception {
        MessageProducer producer = null;
        Connection connection = null;
        Session session = null;
        try {
            connection = getConnectionFactory(connectionFactoryName).createConnection();
            connection.start();
            session = getSession(connection);
            final Queue queue = getQueue(queueName);
            producer = getProducer(session, queue);
            producer.send(session.createTextMessage(text));
        } finally {
            if (producer != null) { producer.close(); }
            if (session != null) { session.close(); }
            if (connection != null) { connection.close(); }
        }
    }
}
