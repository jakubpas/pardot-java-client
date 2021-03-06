package net.jakubpas.pardot.api.request.campaign;

import net.jakubpas.pardot.api.request.BaseQueryRequest;
import net.jakubpas.pardot.api.request.DateParameter;

/**
 * Used to query Campaigns over the Pardot API.
 */
public class CampaignQueryRequest extends BaseQueryRequest<CampaignQueryRequest> {
    @Override
    public String getApiEndpoint() {
        return "campaign/do/query";
    }

    // Filter Options
    public CampaignQueryRequest withName(final String name) {
        return setParam("name", name);
    }

    public CampaignQueryRequest withUpdatedAfter(final DateParameter dateParameter) {
        return super.withUpdatedAfter(dateParameter);
    }

    public CampaignQueryRequest withUpdatedBefore(final DateParameter dateParameter) {
        return super.withUpdatedBefore(dateParameter);
    }

    // Sorting Options
    public CampaignQueryRequest withSortById() {
        return super.withSortById();
    }

    public CampaignQueryRequest withSortByCreatedAt() {
        return super.withSortByCreatedAt();
    }

    public CampaignQueryRequest withSortByName() {
        return super.withSortByName();
    }

    public CampaignQueryRequest withSortByUpdatedAt() {
        return super.withSortByUpdatedAt();
    }

    public CampaignQueryRequest withSortByCost() {
        return super.withSortBy("cost");
    }


}
