package net.jakubpas.pardot.api.parser.email;

import net.jakubpas.pardot.api.parser.JacksonFactory;
import net.jakubpas.pardot.api.parser.ResponseParser;
import net.jakubpas.pardot.api.response.email.Email;
import net.jakubpas.pardot.api.response.email.EmailReadResponse;

import java.io.IOException;

/**
 * Parses Email Read Requests into an Email POJO.
 */
public class EmailReadResponseParser implements ResponseParser<Email> {
    @Override
    public Email parseResponse(final String responseStr) throws IOException {
        return JacksonFactory.newInstance().readValue(responseStr, EmailReadResponse.class).getEmail();
    }
}
