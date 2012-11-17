package org.switchyard.quickstarts.camel;

import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.hornetq.core.remoting.impl.netty.TransportConstants;
import org.switchyard.test.mixins.HornetQMixIn;

/**
 * @author: <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 * Date: 11/17/12
 * Time: 5:20 PM
 */
public class MessageSender {

    private static final String DEFAULT_HOST = TransportConstants.DEFAULT_HOST;
    private static final int DEFAULT_PORT = TransportConstants.DEFAULT_PORT;
    private static final String DEFAULT_USERNAME = "guest";
    private static final String DEFAULT_PASSWORD = "guestp";

    private static Object lock = new Object();

    public static void main(String args[]) {
        try {
            sendTextToQueue("exception", "testQueueInput");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendTextToQueue(final String payload, final String queueName) throws Exception {
        sendTextToQueue(payload, queueName, DEFAULT_USERNAME, DEFAULT_PASSWORD, DEFAULT_HOST, DEFAULT_PORT);
    }

    private static void sendTextToQueue(final String payload, final String queueName, final String username, final String password, final String host, final int port) throws Exception {
        HornetQMixIn hqMixIn = new HornetQMixIn(false)
                        .setUser(username)
                        .setPassword(password)
                        .setHost(host)
                        .setPort(port);
        hqMixIn.initialize();
        Session session = null;
        try {
            session = hqMixIn.createJMSSession();
            final MessageProducer producer = session.createProducer(HornetQMixIn.getJMSQueue(queueName));
            synchronized (lock) {
                Message message = session.createTextMessage((String) payload);
                producer.send(message);
            }
        } finally {
            hqMixIn.uninitialize();
        }
    }
}
