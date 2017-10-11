package net.jakubpas.pardot.api.parser.user;

import net.jakubpas.pardot.api.parser.JacksonFactory;
import net.jakubpas.pardot.api.parser.ResponseParser;
import net.jakubpas.pardot.api.response.user.User;
import net.jakubpas.pardot.api.response.user.UserReadResponse;

import java.io.IOException;

/**
 * Handles parsing UserRead API responses into POJOs.
 */
public class UserReadResponseParser implements ResponseParser<User> {

    @Override
    public User parseResponse(final String responseStr) throws IOException {
        return JacksonFactory.newInstance().readValue(responseStr, UserReadResponse.class).getUser();
    }
}
