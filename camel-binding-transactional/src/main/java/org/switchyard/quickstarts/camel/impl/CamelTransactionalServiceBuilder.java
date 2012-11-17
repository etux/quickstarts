package org.switchyard.quickstarts.camel.impl;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.model.ModelCamelContext;
import org.switchyard.component.camel.Route;
import org.switchyard.quickstarts.camel.api.CamelTransactionalService;

@Route(name = "TransactionalRoutingService", value = CamelTransactionalService.class)
public class CamelTransactionalServiceBuilder extends RouteBuilder {

    private String endpointProperties;
    private String inputService;
    private String businessService;
    private String outputService;
    private String exceptionService;

    public CamelTransactionalServiceBuilder() {
        this("classpath:endpoints.properties");
    }

    public CamelTransactionalServiceBuilder(String endpointProperties) {
        this.endpointProperties = endpointProperties;
    }

    /**
     * The Camel route is configured via this method.  The from:
     * endpoint is required to be a SwitchYard service.
     */
    public void configure() throws Exception {
        prepareContext();


        from(inputService)
            .onException(TransactionalException.class)
                    .maximumRedeliveries(3)
                    .handled(true)
                    .to(exceptionService)
                    .end()
            .to(businessService)
            .to(outputService);
    }

    private void prepareContext() throws Exception {
        ModelCamelContext context = getContext();
        PropertiesComponent props = context.getComponent("properties", PropertiesComponent.class);
        props.setLocation(endpointProperties);
        inputService = context.resolvePropertyPlaceholders("{{inputService}}");
        businessService = context.resolvePropertyPlaceholders("{{businessService}}");
        outputService = context.resolvePropertyPlaceholders("{{outputService}}");
        exceptionService = context.resolvePropertyPlaceholders("{{exceptionService}}");
    }
}
