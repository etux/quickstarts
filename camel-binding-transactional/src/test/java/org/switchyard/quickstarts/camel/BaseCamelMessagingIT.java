package org.switchyard.quickstarts.camel;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.naming.NamingException;

import org.hornetq.api.core.HornetQException;
import org.junit.After;
import org.junit.Before;

/**
 * @author: <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 * Date: 11/18/12
 * Time: 2:15 PM
 */
public class BaseCamelMessagingIT extends AbstractMessagingIT {

    protected static final String TEST_QUEUE_INPUT = "testQueueInput";
    protected static final String TEST_QUEUE_OUTPUT = "testQueueOutput";
    protected static final String TEST_QUEUE_DEAD_LETTER = "testDeadLetterQueue";
    protected static final String CONNECTION_FACTORY = "ConnectionFactory";
    protected static final String RANDOM_MESSAGE = "MyMessage";
    protected static final int JMS_RECEPTION_TIMEOUT = 1000;
    private ConnectionFactory _connectionFactory;
    private Connection _connection;
    private Session _session;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        _connectionFactory = getConnectionFactory(CONNECTION_FACTORY);
        _connection = getStartedConnection(_connectionFactory);
        _session = getSession(_connection);
    }

    @After
    public void tearDown() {
        if (_session != null) try {_session.close(); } catch (Exception e){ }
        if (_connection != null) try { _connection.close(); } catch (Exception e) {}
    }

    protected void assertQueues(String output, String deadLetter) throws NamingException, JMSException, HornetQException {
        MessageConsumer outputConsumer = null;
        MessageConsumer dlConsumer = null;
        try {
            outputConsumer  = getConsumer(_session, getQueue(TEST_QUEUE_OUTPUT));
            dlConsumer      = getConsumer(_session, getQueue(TEST_QUEUE_DEAD_LETTER));

            assertConsumer(TEST_QUEUE_OUTPUT, outputConsumer, output);
            assertConsumer(TEST_QUEUE_DEAD_LETTER, dlConsumer, deadLetter);
        } finally {
            if (outputConsumer != null) try { outputConsumer.close(); } catch (Exception e) {}
            if (dlConsumer != null) try { dlConsumer.close(); } catch (Exception e) {}
        }
    }
}
