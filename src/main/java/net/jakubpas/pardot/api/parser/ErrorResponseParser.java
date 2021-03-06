package net.jakubpas.pardot.api.parser;

import net.jakubpas.pardot.api.response.ErrorResponse;

import java.io.IOException;

/**
 * Parses Error Responses.
 */
public class ErrorResponseParser implements ResponseParser<ErrorResponse> {

    /**
     * Parses response.
     * @param responseStr String representation.
     * @return Parsed response.
     */
    public ErrorResponse parseResponse(final String responseStr) throws IOException {
        return JacksonFactory
            .newInstance()
            .readValue(responseStr, ErrorResponse.class);
    }
}
