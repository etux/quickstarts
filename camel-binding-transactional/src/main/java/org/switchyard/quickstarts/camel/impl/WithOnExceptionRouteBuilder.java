package org.switchyard.quickstarts.camel.impl;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.model.ModelCamelContext;
import org.switchyard.component.camel.Route;
import org.switchyard.quickstarts.camel.api.CamelTransactionalService;

@Route(name = "TransactionalRoutingService", value = CamelTransactionalService.class)
public class WithOnExceptionRouteBuilder extends RouteBuilder {

    private String _endpointProperties;
    private String _inputService;
    private String _businessService;
    private String _outputService;
    private String _exceptionService;

    public WithOnExceptionRouteBuilder() {
        this("classpath:endpoints.properties");
    }

    public WithOnExceptionRouteBuilder(String endpointProperties) {
        this._endpointProperties = endpointProperties;
    }

    /**
     * The Camel route is configured via this method.  The from:
     * endpoint is required to be a SwitchYard service.
     */
    public void configure() throws Exception {
        prepareContext();
        from(_inputService)
            .onException(TransactionalException.class)
                    .maximumRedeliveries(3)
                    .handled(true)
                    .log("Exception handled")
                    .to(_exceptionService)
                    .end()
            .to(_businessService)
            .to(_outputService);
    }

    private void prepareContext() throws Exception {
        ModelCamelContext context = getContext();
        PropertiesComponent props = context.getComponent("properties", PropertiesComponent.class);
        props.setLocation(_endpointProperties);
        _inputService = context.resolvePropertyPlaceholders("{{inputService}}");
        _businessService = context.resolvePropertyPlaceholders("{{businessService}}");
        _outputService = context.resolvePropertyPlaceholders("{{outputService}}");
        _exceptionService = context.resolvePropertyPlaceholders("{{exceptionService}}");
    }
}
