package net.jakubpas.pardot.api.parser.prospect;

import net.jakubpas.pardot.api.parser.JacksonFactory;
import net.jakubpas.pardot.api.parser.ResponseParser;
import net.jakubpas.pardot.api.response.prospect.Prospect;
import net.jakubpas.pardot.api.response.prospect.ProspectReadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Parses Prospect Read API response.
 */
public class ProspectReadResponseParser implements ResponseParser<Prospect> {
    private static final Logger logger = LoggerFactory.getLogger(ProspectReadResponseParser.class);

    @Override
    public Prospect parseResponse(final String responseStr) throws IOException {
        logger.info("{}", responseStr);
        return JacksonFactory
            .newInstance()
            .readValue(responseStr, ProspectReadResponse.class).getProspect();
    }
}
