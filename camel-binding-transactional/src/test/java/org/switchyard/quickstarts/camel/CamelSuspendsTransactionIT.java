package org.switchyard.quickstarts.camel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.quickstarts.camel.impl.TransactionalBeanImpl;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.mixins.CDIMixIn;
import org.switchyard.test.mixins.HornetQMixIn;
import org.switchyard.test.mixins.TransactionMixIn;

/**
 * @author: <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 * Date: 11/17/12
 * Time: 12:01 AM
 */
@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig (
        config = "/META-INF/switchyard-suspends_transaction.xml",
        mixins = {
                CDIMixIn.class,
                TransactionMixIn.class,
                HornetQMixIn.class
        }
)
public class CamelSuspendsTransactionIT extends CamelTransactionIT {

    @Test
    public void testSuspendedTransactionExchange() throws Exception {
        sendTextToQueue(CONNECTION_FACTORY, TEST_QUEUE_INPUT, TransactionalBeanImpl.EXCEPTION_PAYLOAD);
        assertQueues(TransactionalBeanImpl.EXCEPTION_PAYLOAD, null);
    }
}
