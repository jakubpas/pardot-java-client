package net.jakubpas.pardot.api.request.campaign;


import net.jakubpas.pardot.api.request.BaseRequest;

/**
 * Used to generate a Campaign read request.
 */
public class CampaignReadRequest extends BaseRequest<CampaignReadRequest> {

    @Override
    public String getApiEndpoint() {
        return "campaign/do/read";
    }

    public CampaignReadRequest selectById(final Long id) {
        return setParam("id", id);
    }
}
