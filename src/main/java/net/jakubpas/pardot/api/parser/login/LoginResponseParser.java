package net.jakubpas.pardot.api.parser.login;

import net.jakubpas.pardot.api.parser.JacksonFactory;
import net.jakubpas.pardot.api.parser.ResponseParser;
import net.jakubpas.pardot.api.response.login.LoginResponse;

import java.io.IOException;

/**
 * Handles login responses.
 */
public class LoginResponseParser implements ResponseParser<LoginResponse> {
    @Override
    public LoginResponse parseResponse(final String responseStr) throws IOException {
        return JacksonFactory.newInstance().readValue(responseStr, LoginResponse.class);
    }
}
