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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.quickstarts.camel.impl.TransactionalBeanImpl;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.mixins.CDIMixIn;
import org.switchyard.test.mixins.HornetQMixIn;
import org.switchyard.test.mixins.TransactionMixIn;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author: <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 * Date: 11/17/12
 * Time: 12:01 AM
 */
@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig (
        config = SwitchYardTestCaseConfig.SWITCHYARD_XML,
        mixins = {
                CDIMixIn.class,
                TransactionMixIn.class,
                HornetQMixIn.class
        }
)
public class CamelTransactionalIT {

    protected static final String TEST_QUEUE_INPUT = "testQueueInput";
    protected static final String TEST_QUEUE_OUTPUT = "testQueueOutput";
    protected static final String TEST_QUEUE_DEAD_LETTER = "testDeadLetterQueue";

    protected static final String CONNECTION_FACTORY = "ConnectionFactory";
    protected static final String RANDOM_MESSAGE = "MyMessage";

    private InitialContext _initialContext;
    private Connection _connection;
    private Session _session;
    private ConnectionFactory _connectionFactory;

    @Before
    public void setUp() throws JMSException, NamingException {
        _initialContext = new InitialContext();
        _connectionFactory = (ConnectionFactory) _initialContext.lookup(CONNECTION_FACTORY);
        _connection = _connectionFactory.createConnection();
        _session = _connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        _connection.start();
    }

    @After
    public void tearDown() {
        if (_session != null) try {_session.close(); } catch (Exception e){ }
        if (_connection != null) try { _connection.close(); } catch (Exception e) {}
        if (_initialContext != null) try { _initialContext.close(); } catch (Exception e) {}
    }


    @Test
    public void testSuccessfulExchange() throws Exception {
        sendTextToQueue(RANDOM_MESSAGE, TEST_QUEUE_INPUT);
        Queue testOutputQueue = (Queue) _initialContext.lookup(TEST_QUEUE_OUTPUT);
        MessageConsumer consumer = _session.createConsumer(testOutputQueue);
        TextMessage message = (TextMessage) consumer.receive();
        assertThat(message, is(notNullValue()));
        assertThat(message.getText(), is(equalTo(RANDOM_MESSAGE)));
    }

    @Test
    public void testDeadLetterQueue() throws Exception {
        sendTextToQueue(TransactionalBeanImpl.EXCEPTION_PAYLOAD, TEST_QUEUE_INPUT);
        Queue testDLQueue = (Queue) _initialContext.lookup(TEST_QUEUE_DEAD_LETTER);
        MessageConsumer consumer = _session.createConsumer(testDLQueue);
        TextMessage message = (TextMessage) consumer.receive(5000);
        assertThat(message, is(notNullValue()));
        assertThat(message.getText(), is(equalTo(RANDOM_MESSAGE)));
    }

    private void sendTextToQueue(final String text, final String queueName) throws Exception {
            MessageProducer producer = null;
            try {
                final Queue testQueue = (Queue) _initialContext.lookup(queueName);
                producer = _session.createProducer(testQueue);
                producer.send(_session.createTextMessage(text));
            } finally {
                if (producer != null) {
    	            producer.close();
                }
            }
        }
}
