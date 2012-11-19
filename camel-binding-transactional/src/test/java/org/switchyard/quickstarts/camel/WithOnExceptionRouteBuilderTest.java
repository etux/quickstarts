package org.switchyard.quickstarts.camel;


import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.switchyard.quickstarts.camel.impl.WithOnExceptionRouteBuilder;
import org.switchyard.quickstarts.camel.impl.TransactionalBeanImpl;
import org.switchyard.quickstarts.camel.impl.TransactionalException;

/**
 * @author: <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 * Date: 11/17/12
 * Time: 7:09 PM
 */
public class WithOnExceptionRouteBuilderTest extends CamelTestSupport {

    private MockEndpoint exceptionService;
    private MockEndpoint outputService;
    private MockEndpoint businessService;

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new WithOnExceptionRouteBuilder("classpath:testEndpoints.properties");
    }

    @Test
    public void testRouteWithException() throws InterruptedException {
        businessService = getMockEndpoint("mock:businessService");
        outputService = getMockEndpoint("mock:outputService");
        exceptionService = getMockEndpoint("mock:exceptionService");

        businessService.setExpectedMessageCount(4);
        outputService.setExpectedMessageCount(0);
        exceptionService.setExpectedMessageCount(1);

        businessService.whenAnyExchangeReceived(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                System.out.println("sayHello method invoked");
                String message = exchange.getIn().getBody(String.class);
                if (TransactionalBeanImpl.EXCEPTION_PAYLOAD.equals(message)) {
                    throw new TransactionalException();
                }
            }
        });

        template.sendBody("direct:inputService", TransactionalBeanImpl.EXCEPTION_PAYLOAD);

        businessService.assertIsSatisfied();
        outputService.assertIsSatisfied();
        exceptionService.assertIsSatisfied();
    }

    @Test
    public void testRoute() throws InterruptedException {

        businessService = getMockEndpoint("mock:businessService");
        outputService = getMockEndpoint("mock:outputService");
        exceptionService = getMockEndpoint("mock:exceptionService");

        businessService.setExpectedMessageCount(1);
        outputService.setExpectedMessageCount(1);
        exceptionService.setExpectedMessageCount(0);

        template.sendBody("direct:inputService", "anything");

        businessService.assertIsSatisfied();
        outputService.assertIsSatisfied();
        exceptionService.assertIsSatisfied();
    }
}
