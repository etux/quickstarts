/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.switchyard.quickstarts.bpel.service;

import org.switchyard.test.mixins.HTTPMixIn;

/**
 * Simple client to send a SOAP message.
 */
public final class BPELCorrelationClient {

    private static final String URL = "http://localhost:8080/HelloGoodbyeService/HelloGoodbyeService";
    private static final String XML1 = "src/test/resources/xml/hello_request1.xml";
    private static final String XML2 = "src/test/resources/xml/goodbye_request1.xml";

    /**
     * Private no-args constructor.
     */
    private BPELCorrelationClient() {
    }

    /**
     * Only execution point for this application.
     * @param ignored not used.
     * @throws Exception if something goes wrong.
     */
    public static void main(final String[] ignored) throws Exception {

        HTTPMixIn soapMixIn = new HTTPMixIn();
        soapMixIn.initialize();

        try {
            String result = soapMixIn.postFile(URL, XML1);
            System.out.println("SOAP Reply:\n" + result);

            result = soapMixIn.postFile(URL, XML2);
            System.out.println("SOAP Reply:\n" + result);
        } finally {
            soapMixIn.uninitialize();
        }
    }
}
