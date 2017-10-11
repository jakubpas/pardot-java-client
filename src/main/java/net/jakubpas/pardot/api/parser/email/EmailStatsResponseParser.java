package net.jakubpas.pardot.api.parser.email;

import net.jakubpas.pardot.api.parser.JacksonFactory;
import net.jakubpas.pardot.api.parser.ResponseParser;
import net.jakubpas.pardot.api.response.email.EmailStatsResponse;

import java.io.IOException;

/**
 * Handles parsing EmailStats API responses into POJOs.
 */
public class EmailStatsResponseParser implements ResponseParser<EmailStatsResponse.Stats> {
    @Override
    public EmailStatsResponse.Stats parseResponse(final String responseStr) throws IOException {
        return JacksonFactory.newInstance().readValue(responseStr, EmailStatsResponse.class).getStats();
    }
}