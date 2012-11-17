package org.switchyard.quickstarts.camel.impl;

/**
 * @author: <a href="mailto:eduardo.devera@gmail.com">Eduardo de Vera</a>
 * Date: 11/17/12
 * Time: 4:53 PM
 */
public class TransactionalException extends Exception {

    public TransactionalException() {
    }

    public TransactionalException(Throwable cause) {
        super(cause);
    }

    public TransactionalException(String message) {
        super(message);
    }

    public TransactionalException(String message, Throwable cause) {
        super(message, cause);
    }
}
