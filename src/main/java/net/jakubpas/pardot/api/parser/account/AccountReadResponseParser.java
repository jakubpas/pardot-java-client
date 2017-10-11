package net.jakubpas.pardot.api.parser.account;

import net.jakubpas.pardot.api.parser.JacksonFactory;
import net.jakubpas.pardot.api.parser.ResponseParser;
import net.jakubpas.pardot.api.response.account.Account;
import net.jakubpas.pardot.api.response.account.AccountReadResponse;

import java.io.IOException;

/**
 * Handles parsing AccountRead API responses into POJOs.
 */
public class AccountReadResponseParser implements ResponseParser<Account> {
    @Override
    public Account parseResponse(final String responseStr) throws IOException {
        return JacksonFactory.newInstance().readValue(responseStr, AccountReadResponse.class).getAccount();
    }
}
