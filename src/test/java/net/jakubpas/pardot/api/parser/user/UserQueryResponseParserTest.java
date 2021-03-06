package net.jakubpas.pardot.api.parser.user;

import net.jakubpas.pardot.api.parser.BaseResponseParserTest;
import net.jakubpas.pardot.api.response.user.User;
import net.jakubpas.pardot.api.response.user.UserQueryResponse;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 *
 */
public class UserQueryResponseParserTest extends BaseResponseParserTest {
    private static final Logger logger = LoggerFactory.getLogger(UserQueryResponseParserTest.class);

    /**
     * Validates we can parse a User query with multiple users response A-OK.
     */
    @Test
    public void testMultpleUsers() throws IOException {
        final String input = readFile("userQuery.xml");
        final UserQueryResponse.Result response = new UserQueryResponseParser().parseResponse(input);
        logger.info("Result: {}", response);

        assertNotNull("Should not be null", response);
        assertEquals("Should have 2 results", 2, (int) response.getTotalResults());
        assertEquals("Should have 2 results", 2, response.getUsers().size());
        validateUser1(response.getUsers().get(0));
    }

    /**
     * Validates we can parse a User query with single user response A-OK.
     */
    @Test
    public void testSingleUser() throws IOException {
        final String input = readFile("userQuery2.xml");
        final UserQueryResponse.Result response = new UserQueryResponseParser().parseResponse(input);
        logger.info("Result: {}", response);

        assertNotNull("Should not be null", response);
        assertEquals("Should have 1 results", 1, (int) response.getTotalResults());
        assertEquals("Should have 1 results", 1, response.getUsers().size());
        validateUser1(response.getUsers().get(0));
    }

    private void validateUser1(final User user) {
        assertEquals("Has correct id", 1212L, (long) user.getId());
        assertEquals("Has correct accountId", 1L, (long) user.getAccount());
        assertEquals("Has correct first name", "Api", user.getFirstName());
        assertEquals("Has correct last name", "Test", user.getLastName());
        assertEquals("Has correct email", "apitest@gmail.com", user.getEmail());
        assertEquals("Has correct jobTitle", "Engineer", user.getJobTitle());
        assertEquals("Has correct role", "Administrator", user.getRole());
        assertEquals("Has correct createdAt", "2017-08-11T01:20:15.000", user.getCreatedAt().toString());
        assertEquals("Has correct updatedAt", "2017-08-11T21:20:15.000", user.getUpdatedAt().toString());
    }

    private void validateUser2(final User user) {
        assertEquals("Has correct id", 1214L, (long) user.getId());
        assertEquals("Has correct accountId", 2L, (long) user.getAccount());
        assertEquals("Has correct first name", "Random", user.getFirstName());
        assertEquals("Has correct last name", "User", user.getLastName());
        assertEquals("Has correct email", "apitest+1@gmail.com", user.getEmail());
        assertNull("Has null jobTitle", user.getJobTitle());
        assertEquals("Has correct role", "Sales", user.getRole());
        assertEquals("Has correct createdAt", "2016-01-04T10:39:29.000", user.getCreatedAt().toString());
        assertEquals("Has correct updatedAt", "2017-08-09T18:28:24.000", user.getUpdatedAt().toString());
    }

}