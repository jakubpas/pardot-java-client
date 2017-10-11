package net.jakubpas.pardot.api.rest;

import net.jakubpas.pardot.api.Configuration;
import net.jakubpas.pardot.api.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Mock Rest Client for testing.
 */
public class MockRestClient implements RestClient {
    private static final Logger logger = LoggerFactory.getLogger(MockRestClient.class);

    @Override
    public void init(final Configuration configuration) {
        // Noop.
    }

    @Override
    public RestResponse submitRequest(final Request request) throws RestException {
        // Not implemented yet.
        return null;
    }

    @Override
    public void close() {
        // Noop.
    }
}
