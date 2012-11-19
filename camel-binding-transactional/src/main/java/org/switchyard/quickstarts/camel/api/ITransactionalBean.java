package org.switchyard.quickstarts.camel.api;

import javax.ejb.Local;

import org.switchyard.quickstarts.camel.impl.TransactionalException;

/**
 * @author: <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 * Date: 11/17/12
 * Time: 2:12 AM
 */
@Local
public interface ITransactionalBean {

    String sayHello(String name) throws TransactionalException;
}
