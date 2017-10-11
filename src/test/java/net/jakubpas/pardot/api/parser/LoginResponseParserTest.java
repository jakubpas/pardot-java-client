package net.jakubpas.pardot.api.parser;

import net.jakubpas.pardot.api.parser.login.LoginResponseParser;
import net.jakubpas.pardot.api.response.login.LoginResponse;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LoginResponseParserTest extends BaseResponseParserTest {

    /**
     * Validates we can parse the login response A-OK.
     */
    @Test
    public void test() throws IOException {
        final String input = readFile("login.xml");

        final LoginResponse loginResponse = new LoginResponseParser().parseResponse(input);
        assertNotNull("Should not be null", loginResponse);
        assertEquals("Has correct api_key", "5a1698a233e73d7c8ccd60d775fbc68a", loginResponse.getApiKey());
    }
}