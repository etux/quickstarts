package org.switchyard.quickstarts.camel.impl;

import org.switchyard.component.bean.Service;
import org.switchyard.quickstarts.camel.api.ITransactionalBean;

/**
 * @author: <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 * Date: 11/17/12
 * Time: 2:12 AM
 */
@Service(name = "BeanService", value = ITransactionalBean.class)
public class TransactionalBeanImpl implements ITransactionalBean {

    public static final String EXCEPTION_PAYLOAD = "exception";

    @Override
    public void sayHello(String name) throws TransactionalException {
        if (EXCEPTION_PAYLOAD.equals(name)) {
            throw new TransactionalException("Exception thrown from TransactionalBeanImpl");
        }
        System.out.println("Hello: "+name);
    }
}
