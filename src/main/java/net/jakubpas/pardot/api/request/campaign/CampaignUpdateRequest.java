package net.jakubpas.pardot.api.request.campaign;

import net.jakubpas.pardot.api.request.BaseRequest;
import net.jakubpas.pardot.api.response.campaign.Campaign;

/**
 * For Updating existing Campaigns using Pardot's API.
 */
public class CampaignUpdateRequest extends BaseRequest<CampaignCreateRequest> {
    @Override
    public String getApiEndpoint() {
        return "campaign/do/update";
    }

    /**
     * Define the campaign you want to update in pardot.
     * @param campaign The campaign you want to update in pardot.
     * @return CampaignUpdateRequest builder.
     */
    public CampaignUpdateRequest withCampaign(final Campaign campaign) {
        setParam("id", campaign.getId());
        setParam("name", campaign.getName());
        setParam("cost", campaign.getCost());

        // This is an optional paramter.
        setParam("folder_id", campaign.getFolderId());
        return this;
    }
}
