package net.jakubpas.pardot.api.parser.prospect;

import net.jakubpas.pardot.api.parser.JacksonFactory;
import net.jakubpas.pardot.api.parser.ResponseParser;
import net.jakubpas.pardot.api.response.prospect.ProspectQueryResponse;

import java.io.IOException;

/**
 * Handles parsing ProspectQuery API responses into POJOs.
 */
public class ProspectQueryResponseParser implements ResponseParser<ProspectQueryResponse.Result> {

    @Override
    public ProspectQueryResponse.Result parseResponse(final String responseStr) throws IOException {
        return JacksonFactory.newInstance().readValue(responseStr, ProspectQueryResponse.class).getResult();
    }
}
