package net.jakubpas.pardot.api;

import net.jakubpas.pardot.api.parser.ErrorResponseParser;
import net.jakubpas.pardot.api.parser.ResponseParser;
import net.jakubpas.pardot.api.parser.StringResponseParser;
import net.jakubpas.pardot.api.parser.account.AccountReadResponseParser;
import net.jakubpas.pardot.api.parser.campaign.CampaignQueryResponseParser;
import net.jakubpas.pardot.api.parser.campaign.CampaignReadResponseParser;
import net.jakubpas.pardot.api.parser.email.EmailReadResponseParser;
import net.jakubpas.pardot.api.parser.email.EmailStatsResponseParser;
import net.jakubpas.pardot.api.parser.login.LoginResponseParser;
import net.jakubpas.pardot.api.parser.prospect.ProspectQueryResponseParser;
import net.jakubpas.pardot.api.parser.prospect.ProspectReadResponseParser;
import net.jakubpas.pardot.api.parser.user.UserAbilitiesParser;
import net.jakubpas.pardot.api.parser.user.UserQueryResponseParser;
import net.jakubpas.pardot.api.parser.user.UserReadResponseParser;
import net.jakubpas.pardot.api.request.Request;
import net.jakubpas.pardot.api.request.account.AccountReadRequest;
import net.jakubpas.pardot.api.request.campaign.CampaignCreateRequest;
import net.jakubpas.pardot.api.request.campaign.CampaignQueryRequest;
import net.jakubpas.pardot.api.request.campaign.CampaignReadRequest;
import net.jakubpas.pardot.api.request.campaign.CampaignUpdateRequest;
import net.jakubpas.pardot.api.request.email.EmailReadRequest;
import net.jakubpas.pardot.api.request.email.EmailSendListRequest;
import net.jakubpas.pardot.api.request.email.EmailSendOneToOneRequest;
import net.jakubpas.pardot.api.request.email.EmailStatsRequest;
import net.jakubpas.pardot.api.request.login.LoginRequest;
import net.jakubpas.pardot.api.request.prospect.ProspectAssignRequest;
import net.jakubpas.pardot.api.request.prospect.ProspectCreateRequest;
import net.jakubpas.pardot.api.request.prospect.ProspectDeleteRequest;
import net.jakubpas.pardot.api.request.prospect.ProspectQueryRequest;
import net.jakubpas.pardot.api.request.prospect.ProspectReadRequest;
import net.jakubpas.pardot.api.request.prospect.ProspectUnassignRequest;
import net.jakubpas.pardot.api.request.prospect.ProspectUpdateRequest;
import net.jakubpas.pardot.api.request.prospect.ProspectUpsertRequest;
import net.jakubpas.pardot.api.request.user.UserAbilitiesRequest;
import net.jakubpas.pardot.api.request.user.UserQueryRequest;
import net.jakubpas.pardot.api.request.user.UserReadRequest;
import net.jakubpas.pardot.api.response.ErrorResponse;
import net.jakubpas.pardot.api.response.account.Account;
import net.jakubpas.pardot.api.response.campaign.Campaign;
import net.jakubpas.pardot.api.response.campaign.CampaignQueryResponse;
import net.jakubpas.pardot.api.response.email.Email;
import net.jakubpas.pardot.api.response.email.EmailStatsResponse;
import net.jakubpas.pardot.api.response.login.LoginResponse;
import net.jakubpas.pardot.api.response.prospect.Prospect;
import net.jakubpas.pardot.api.response.prospect.ProspectQueryResponse;
import net.jakubpas.pardot.api.response.user.User;
import net.jakubpas.pardot.api.response.user.UserAbilitiesResponse;
import net.jakubpas.pardot.api.response.user.UserQueryResponse;
import net.jakubpas.pardot.api.rest.HttpClientRestClient;
import net.jakubpas.pardot.api.rest.RestClient;
import net.jakubpas.pardot.api.rest.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Interface for Pardot's API.
 */
public class PardotClient implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(PardotClient.class);

    /**
     * Our API Configuration.
     */
    private final Configuration configuration;

    /**
     * Underlying RestClient to use.
     */
    private final RestClient restClient;

    /**
     * Internal State flag.
     */
    private boolean isInitialized = false;

    /**
     * Default Constructor.
     * @param configuration Pardot Api Configuration.
     */
    public PardotClient(final Configuration configuration) {
        this.configuration = configuration;
        this.restClient = new HttpClientRestClient();
    }

    /**
     * Constructor for injecting a RestClient implementation.
     * Typically only used in testing.
     * @param configuration Pardot Api Configuration.
     * @param restClient RestClient implementation to use.
     */
    public PardotClient(final Configuration configuration, final RestClient restClient) {
        this.configuration = configuration;
        this.restClient = restClient;
    }

    private <T> T submitRequest(final Request request, ResponseParser<T> responseParser) {
        // Ugly hack,
        // avoid doing login check if we're doing a login request.
        if (!(request instanceof LoginRequest)) {
            // Check for auth token
            checkLogin();
        }

        // Submit request
        final RestResponse restResponse = getRestClient().submitRequest(request);
        final int responseCode = restResponse.getHttpCode();
        String responseStr = restResponse.getResponseStr();

        // If we have a valid response
        logger.info("Response: {}", restResponse);

        // Check for invalid http status codes
        if (responseCode >= 200 && responseCode < 300) {
            // These response codes have no values
            if (responseCode == 205 && responseStr == null) {
                // Avoid NPE
                responseStr = "";
            }

            // High level check for error response
            if (restResponse.getResponseStr().contains("<rsp stat=\"fail\"")) {
                try {
                    // Parse error response
                    final ErrorResponse error = new ErrorResponseParser().parseResponse(restResponse.getResponseStr());

                    // throw exception
                    throw new InvalidRequestException(error.getMessage(), error.getCode());
                } catch (IOException exception) {
                    throw new ParserException(exception.getMessage(), exception);
                }
            }
            try {
                return responseParser.parseResponse(restResponse.getResponseStr());
            } catch (IOException exception) {
                throw new ParserException(exception.getMessage(), exception);
            }
        }
        // Otherwise throw an exception.
        throw new InvalidRequestException("Invalid http response code from server: " + restResponse.getHttpCode(), restResponse.getHttpCode());
    }

    /**
     * @return Return Pardot API Configuration.
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Package protected for access in tests.
     * @return Rest Client.
     */
    RestClient getRestClient() {
        // If we haven't initialized.
        if (!isInitialized) {
            // Call Init.
            restClient.init(getConfiguration());

            // Flip state flag
            isInitialized = true;
        }

        // return our rest client.
        return restClient;
    }

    /**
     * Check to see if we're already logged in and have an API key.
     * If no existing API key is found, this will attempt to authenticate and
     * get a new API key.
     */
    private void checkLogin() {
        if (configuration.getApiKey() != null) {
            return;
        }
        // Otherwise attempt to authenticate.
        final LoginResponse response = login(new LoginRequest()
            .withEmail(configuration.getEmail())
            .withPassword(configuration.getPassword())
        );

        // If we have an API key.
        if (response.getApiKey() != null) {
            // Set it.
            getConfiguration().setApiKey(response.getApiKey());
        }
    }

    /**
     * Make login request
     * @param request Login request definition.
     * @return LoginResponse returned from server.
     */
    public LoginResponse login(LoginRequest request) {
        return submitRequest(request, new LoginResponseParser());
    }

    /**
     * Make API request to read the account of the currently authenticated user.
     * @param request Request definition.
     * @return Parsed api response.
     */
    public Account accountRead(final AccountReadRequest request) {
        return submitRequest(request, new AccountReadResponseParser());
    }

    /**
     * Make API request to query one or more users.
     * @param request Request definition.
     * @return Parsed user query response.
     */
    public UserQueryResponse.Result userQuery(final UserQueryRequest request) {
        return submitRequest(request, new UserQueryResponseParser());
    }

    /**
     * Make API request to read the abilities of the currently authenticated user.
     * @param request Request definition.
     * @return Parsed api response.
     */
    public UserAbilitiesResponse.Result userAbilities(final UserAbilitiesRequest request) {
        return submitRequest(request, new UserAbilitiesParser());
    }

    /**
     * Make API request to read a specific user.
     * @param request Request definition.
     * @return Parsed api response.
     */
    public User userRead(final UserReadRequest request) {
        return submitRequest(request, new UserReadResponseParser());
    }

    /**
     * Make API request to query for one or more campaigns.
     * @param request Request definition.
     * @return Parsed api response.
     */
    public CampaignQueryResponse.Result campaignQuery(final CampaignQueryRequest request) {
        return submitRequest(request, new CampaignQueryResponseParser());
    }

    /**
     * Make API request to read a specific campaign.
     * @param request Request definition.
     * @return Parsed api response.
     */
    public Campaign campaignRead(final CampaignReadRequest request) {
        return submitRequest(request, new CampaignReadResponseParser());
    }

    /**
     * Make API request to create a new Campaign.
     * @param request Request definition.
     * @return Parsed api response.
     */
    public Campaign campaignCreate(final CampaignCreateRequest request) {
        return submitRequest(request, new CampaignReadResponseParser());
    }

    /**
     * Make API request to update an existing Campaign.
     * @param request Request definition.
     * @return Parsed api response.
     */
    public Campaign campaignUpdate(final CampaignUpdateRequest request) {
        return submitRequest(request, new CampaignReadResponseParser());
    }

    /**
     * Make API request to read a specific Email.
     * @param request Request definition.
     * @return Parsed api response.
     */
    public Email emailRead(final EmailReadRequest request) {
        return submitRequest(request, new EmailReadResponseParser());
    }

    /**
     * Make API request to retrieve stats about a List Email Send.
     * @param request Request definition.
     * @return Parsed api response.
     */
    public EmailStatsResponse.Stats emailStats(final EmailStatsRequest request) {
        return submitRequest(request, new EmailStatsResponseParser());
    }

    /**
     * Make API request to send a 1-to-1 prospect email.
     * @param request Request definition.
     * @return Parsed api response.
     */
    public Email emailSendOneToOne(final EmailSendOneToOneRequest request) {
        return submitRequest(request, new EmailReadResponseParser());
    }

    /**
     * Make API request to send a list email.
     * @param request Request definition.
     * @return Parsed api response.
     */
    public Email emailSendList(final EmailSendListRequest request) {
        return submitRequest(request, new EmailReadResponseParser());
    }

    /**
     * Make API request to read a prospect.
     * @param request Request definition.
     * @return Parsed api response.
     */
    public Prospect prospectRead(final ProspectReadRequest request) {
        return submitRequest(request, new ProspectReadResponseParser());
    }

    /**
     * Make API request to create a new prospect.
     * @param request Request definition.
     * @return Parsed api response.
     */
    public Prospect prospectCreate(final ProspectCreateRequest request) {
        return submitRequest(request, new ProspectReadResponseParser());
    }

    /**
     * Make API request to update an existing prospect.
     * @param request Request definition.
     * @return Parsed api response.
     */
    public Prospect prospectUpdate(final ProspectUpdateRequest request) {
        return submitRequest(request, new ProspectReadResponseParser());
    }

    /**
     * Make API request to upsert a prospect.
     * @param request Request definition.
     * @return Parsed api response.
     */
    public Prospect prospectUpsert(final ProspectUpsertRequest request) {
        return submitRequest(request, new ProspectReadResponseParser());
    }

    /**
     * Make API request to query prospects.
     * @param request Request definition.
     * @return Parsed api response.
     */
    public ProspectQueryResponse.Result prospectQuery(final ProspectQueryRequest request) {
        return submitRequest(request, new ProspectQueryResponseParser());
    }

    /**
     * Make API request to delete prospects.
     * @param request Request definition.
     * @return true if success, false if error.
     */
    public boolean prospectDelete(final ProspectDeleteRequest request) {
        submitRequest(request, new StringResponseParser());
        return true;
    }

    /**
     * Make API request to assign a prospect.
     * @param request Request definition.
     * @return Parsed api response.
     */
    public Prospect prospectAssign(final ProspectAssignRequest request) {
        return submitRequest(request, new ProspectReadResponseParser());
    }

    /**
     * Make API request to unassign a prospect.
     * @param request Request definition.
     * @return Parsed api response.
     */
    public Prospect prospectUnassign(final ProspectUnassignRequest request) {
        return submitRequest(request, new ProspectReadResponseParser());
    }

    /**
     * Clean up instance, releasing any resources held internally.
     */
    public void close() {
        getRestClient().close();
    }

}