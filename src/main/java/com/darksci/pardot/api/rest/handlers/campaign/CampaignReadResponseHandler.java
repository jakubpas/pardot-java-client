package com.pardot.api.rest.handlers.campaign;

import com.pardot.api.response.campaign.Campaign;
import com.pardot.api.response.campaign.CampaignReadResponse;
import com.pardot.api.rest.handlers.BaseResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Handles parsing CampaignRead API responses into POJOs.
 */
public class CampaignReadResponseHandler extends BaseResponseHandler<Campaign> {
    private static final Logger logger = LoggerFactory.getLogger(CampaignReadResponseHandler.class);

    @Override
    public Campaign parseResponse(final String responseStr) throws IOException {
        return getMapper().readValue(responseStr, CampaignReadResponse.class).getCampaign();
    }
}