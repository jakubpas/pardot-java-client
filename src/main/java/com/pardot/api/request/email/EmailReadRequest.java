package com.pardot.api.request.email;

import com.pardot.api.request.BaseRequest;

/**
 * Used to generate a User read request.
 */
public class EmailReadRequest extends BaseRequest<EmailReadRequest> {

    @Override
    public String getApiEndpoint() {
        return "email/do/read";
    }


    public EmailReadRequest selectById(final Long id) {
        return setParam("id", id);
    }

    public EmailReadRequest withIncludeMessageInResponse(boolean includeMessage) {
        String value = "true";
        if (!includeMessage) {
            value = "false";
        }
        return setParam("include_message", value);
    }
}
