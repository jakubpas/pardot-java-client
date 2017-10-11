package net.jakubpas.pardot.api.parser.user;

import net.jakubpas.pardot.api.parser.JacksonFactory;
import net.jakubpas.pardot.api.parser.ResponseParser;
import net.jakubpas.pardot.api.response.user.UserQueryResponse;

import java.io.IOException;

/**
 * Handles parsing UserQuery API responses into POJOs.
 */
public class UserQueryResponseParser implements ResponseParser<UserQueryResponse.Result> {

    @Override
    public UserQueryResponse.Result parseResponse(final String responseStr) throws IOException {
        return JacksonFactory.newInstance().readValue(responseStr, UserQueryResponse.class).getResult();
    }
}
