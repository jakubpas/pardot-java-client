package net.jakubpas.pardot.api.parser.user;

import net.jakubpas.pardot.api.parser.JacksonFactory;
import net.jakubpas.pardot.api.parser.ResponseParser;
import net.jakubpas.pardot.api.response.user.UserAbilitiesResponse;

import java.io.IOException;

/**
 * Handles parsing UserAbilities API responses into POJOs.
 */
public class UserAbilitiesParser implements ResponseParser<UserAbilitiesResponse.Result> {

    @Override
    public UserAbilitiesResponse.Result parseResponse(final String responseStr) throws IOException {
        return JacksonFactory.newInstance().readValue(responseStr, UserAbilitiesResponse.class).getResult();
    }
}
