package net.jakubpas.pardot.api.parser.campaign;

import net.jakubpas.pardot.api.parser.JacksonFactory;
import net.jakubpas.pardot.api.parser.ResponseParser;
import net.jakubpas.pardot.api.response.campaign.Campaign;
import net.jakubpas.pardot.api.response.campaign.CampaignReadResponse;

import java.io.IOException;

/**
 * Handles parsing CampaignRead API responses into POJOs.
 */
public class CampaignReadResponseParser implements ResponseParser<Campaign> {

    @Override
    public Campaign parseResponse(final String responseStr) throws IOException {
        return JacksonFactory.newInstance().readValue(responseStr, CampaignReadResponse.class).getCampaign();
    }
}
