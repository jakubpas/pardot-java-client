package net.jakubpas.pardot.api;

import categories.IntegrationTest;
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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Integration/End-to-End test over HttpClientRestClient.
 */
@Category(IntegrationTest.class)
public class PardotClientTest {
    private static final Logger logger = LoggerFactory.getLogger(PardotClientTest.class);

    private Configuration testConfig;
    private PardotClient client;

    @Before
    public void setup() throws IOException {
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test_credentials.properties");

        // Load properties
        Properties properties = new Properties();
        properties.load(inputStream);
        inputStream.close();

        // Load in config
        testConfig = new Configuration(
            properties.getProperty("username"),
            properties.getProperty("password"),
            properties.getProperty("user_key")
        );

        logger.info("Config: {}", testConfig);

        // Create client
        client = new PardotClient(testConfig);
    }

    @After
    public void tearDown() {
        testConfig = null;
    }

    /**
     * Attempt to login.
     */
    @Test
    public void loginTest() throws IOException {
        final LoginResponse response = client.login(new LoginRequest()
            .withEmail(testConfig.getEmail())
            .withPassword(testConfig.getPassword())
        );

        logger.info("Response: {}", response);
        assertNotNull("Should not be null", response);
        assertNotNull("Should have non-null property", response.getApiKey());
    }

    /**
     * Attempt to retrieve account.
     */
    @Test
    public void accountReadTest() throws IOException {
        AccountReadRequest readRequest = new AccountReadRequest();

        final Account response = client.accountRead(readRequest);
        assertNotNull("Should not be null", response);
        logger.info("Response: {}", response);
    }

    /**
     * Attempt to retrieve users.
     */
    @Test
    public void userQueryTest() throws IOException {
        UserQueryRequest userQueryRequest = new UserQueryRequest()
            .withIdGreaterThan(10L)
            .withLimit(1)
            .withArchivedUsersOnly(true)
            .withSortByCreatedAt()
            .withSortOrderAscending();

        final UserQueryResponse.Result response = client.userQuery(userQueryRequest);
        assertNotNull("Should not be null", response);
        logger.info("Response: {}", response);
    }

    /**
     * Attempt to retrieve current user's abilities.
     */
    @Test
    public void userAbilitiesTest() throws IOException {
        UserAbilitiesRequest userAbilitiesRequest = new UserAbilitiesRequest();

        final UserAbilitiesResponse.Result response = client.userAbilities(userAbilitiesRequest);
        assertNotNull("Should not be null", response);
        logger.info("Response: {}", response);
    }

    /**
     * Attempt to retrieve a user.
     */
    @Test
    public void userReadTest() throws IOException {
        UserReadRequest readRequest = new UserReadRequest()
            .selectById(3793281L);

        final User response = client.userRead(readRequest);
        assertNotNull("Should not be null", response);
        logger.info("Response: {}", response);
    }

    /**
     * Attempt to query campaigns.
     */
    @Test
    public void campaignQueryTest() throws IOException {
        CampaignQueryRequest request = new CampaignQueryRequest();

        final CampaignQueryResponse.Result response = client.campaignQuery(request);
        assertNotNull("Should not be null", response);
        logger.info("Response: {}", response);
    }

    /**
     * Attempt to read campaign.
     */
    @Test
    public void campaignReadTest() throws IOException {
        CampaignReadRequest request = new CampaignReadRequest()
            .selectById(14885L);

        final Campaign response = client.campaignRead(request);
        assertNotNull("Should not be null", response);
        logger.info("Response: {}", response);
    }

    /**
     * Attempt to create a campaign.
     */
    @Test
    public void campaignCreateTest() throws IOException {
        // Define campaign
        final Campaign campaign = new Campaign();
        campaign.setName("API Test Campaign " + System.currentTimeMillis());
        campaign.setCost(31337);

        // Create request
        CampaignCreateRequest request = new CampaignCreateRequest()
            .withCampaign(campaign);

        // Send Request
        final Campaign response = client.campaignCreate(request);
        assertNotNull("Should not be null", response);
        assertNotNull("Has an Id", response.getId());
        assertEquals("Has correct name", campaign.getName(), response.getName());
        assertEquals("Has correct cost", campaign.getCost(), response.getCost());
        logger.info("Response: {}", response);
    }

    /**
     * Attempt to create a campaign.
     */
    @Test
    public void campaignUpdateTest() throws IOException {
        final long campaignId = 14887L;

        // Define campaign
        final Campaign campaign = new Campaign();
        campaign.setId(campaignId);
        campaign.setName("Updated API Test Campaign " + System.currentTimeMillis());
        campaign.setCost(20);

        // Create request
        CampaignUpdateRequest request = new CampaignUpdateRequest()
            .withCampaign(campaign);

        // Send Request
        final Campaign response = client.campaignUpdate(request);
        assertNotNull("Should not be null", response);
        assertEquals("Has correct Id", campaignId, (long) response.getId());
        assertEquals("Has correct name", campaign.getName(), response.getName());
        assertEquals("Has correct cost", campaign.getCost(), response.getCost());
        logger.info("Response: {}", response);
    }

    /**
     * Test reading a specific email over the api.
     */
    @Test
    public void emailReadTest() throws IOException {
        final long emailId = 167044349L;

        EmailReadRequest request = new EmailReadRequest()
            .selectById(emailId);

        final Email response = client.emailRead(request);
        assertNotNull("Should not be null", response);
        logger.info("Response: {}", response);
    }

    /**
     * Test reading a specific email over the api.
     */
    @Test
    public void emailStatsTest() throws IOException {
        final long listEmailId = 167044401;

        EmailStatsRequest request = new EmailStatsRequest()
            .selectByListEmailId(listEmailId);

        final EmailStatsResponse.Stats response = client.emailStats(request);
        assertNotNull("Should not be null", response);
        logger.info("Response: {}", response);
    }

    /**
     * Test sending a 1-to-1 email to a specific prospect.
     */
    @Test
    public void emailSendOneToOneTest() throws IOException {
        final long campaignId = 14885;
        final long prospectId = 59135263;

        EmailSendOneToOneRequest request = new EmailSendOneToOneRequest()
            .withProspectId(prospectId)
            .withCampaignId(campaignId)
            .withFromNameAndEmail("Test User", "no-reply@example.com")
            .withReplyToEmail("no-reply@example.com")
            .withName("Test Email Send " + System.currentTimeMillis())
            .withOperationalEmail(true)
            .withSubject("Test Email From Api")
            .withTag("Tag 1")
            .withTag("Tag 2")
            .withTextContent("Hello %%first_name%%!")
            .withHtmlContent("<html><body><h1>Hello %%first_name%%!</h1></body></html>");

        final Email response = client.emailSendOneToOne(request);
        assertNotNull("Should not be null", response);
        logger.info("Response: {}", response);
    }

    /**
     * Test sending a 1-to-1 email to a specific prospect.
     */
    @Test
    public void emailSendListTest() {
        final long campaignId = 14885;
        final long listId = 33173;

        EmailSendListRequest request = new EmailSendListRequest()
            .withListId(listId)
            .withCampaignId(campaignId)
            .withFromNameAndEmail("Test User", "no-reply@example.com")
            .withReplyToEmail("no-reply@example.com")
            .withName("Test List Email Send " + System.currentTimeMillis())
            .withOperationalEmail(true)
            .withSubject("Test Email From Api")
            .withTag("Tag 1")
            .withTag("Tag 2")
            .withTextContent("Hello %%first_name%%!")
            .withHtmlContent("<html><body><h1>Hello %%first_name%%!</h1></body></html>");

        final Email response = client.emailSendList(request);
        assertNotNull("Should not be null", response);
        logger.info("Response: {}", response);
    }

    /**
     * Test reading prospect by id.
     */
    @Test
    public void prospectReadTest() {
        final long prospectId = 59135263;

        final Prospect response = client.prospectRead(new ProspectReadRequest()
            .selectById(prospectId)
        );
        assertNotNull("Should not be null", response);
        logger.info("Response: {}", response);
    }

    /**
     * Test creating prospect.
     */
    @Test
    public void prospectCreateTest() {
        final Prospect prospect = new Prospect();
        prospect.setEmail("random-email" + System.currentTimeMillis() + "@example.com");
        prospect.setFirstName("Test");
        prospect.setLastName("User");
        prospect.setCity("Some City");

        final ProspectCreateRequest request = new ProspectCreateRequest()
            .withProspect(prospect);

        // Issue request
        final Prospect response = client.prospectCreate(request);

        assertNotNull("Should not be null", response);
        logger.info("Response: {}", response);
    }

    /**
     * Test creating prospect.
     */
    @Test
    public void prospectUpsertTest() {
        final Prospect prospect = new Prospect();
        prospect.setEmail("random-email" + System.currentTimeMillis() + "@example.com");
        prospect.setFirstName("Test");
        prospect.setLastName("User");
        prospect.setCity("Some City");

        final ProspectUpsertRequest request = new ProspectUpsertRequest()
            .withProspect(prospect);

        // Issue request
        final Prospect response = client.prospectUpsert(request);

        assertNotNull("Should not be null", response);
        logger.info("Response: {}", response);
    }

    /**
     * Test creating prospect.
     */
    @Test
    public void prospectUpdateTest() {
        final long prospectId = 59138429L;

        final Prospect prospect = new Prospect();
        prospect.setId(prospectId);
        prospect.setFirstName("Test");
        prospect.setLastName("User");
        prospect.setCity("Some City");
        prospect.setCompany("New Company");

        final ProspectUpdateRequest request = new ProspectUpdateRequest()
            .withProspect(prospect);

        // Issue request
        final Prospect response = client.prospectUpdate(request);

        assertNotNull("Should not be null", response);
        logger.info("Response: {}", response);
    }

    /**
     * Test creating prospect.
     */
    @Test
    public void prospectDeleteTest() {
        final long prospectId = 59138429L;

        final ProspectDeleteRequest request = new ProspectDeleteRequest()
            .withProspectId(prospectId);

        // Issue request
        final boolean response = client.prospectDelete(request);

        assertNotNull("Should not be null", response);
        logger.info("Response: {}", response);
    }

    /**
     * Test assigning prospect.
     */
    @Test
    public void prospectAssignTest() {
        final long prospectId = 59138429L;
        final long userId = 3793281;

        final ProspectAssignRequest request = new ProspectAssignRequest()
            .withProspectId(prospectId)
            .withUserId(userId);

        // Issue request
        final Prospect response = client.prospectAssign(request);

        assertNotNull("Should not be null", response);
        logger.info("Response: {}", response);
    }

    /**
     * Test unassigning prospect.
     */
    @Test
    public void prospectUnassignTest() {
        final long prospectId = 59138429L;

        final ProspectUnassignRequest request = new ProspectUnassignRequest()
            .withProspectId(prospectId);

        // Issue request
        final Prospect response = client.prospectUnassign(request);

        assertNotNull("Should not be null", response);
        logger.info("Response: {}", response);
    }

    /**
     * Test reading prospect by id.
     */
    @Test
    public void prospectQueryTest() {
        final ProspectQueryRequest request = new ProspectQueryRequest()
            .withArchivedOnly();

        final ProspectQueryResponse.Result response = client.prospectQuery(request);
        assertNotNull("Should not be null", response);
        logger.info("Response: {}", response);
    }
}