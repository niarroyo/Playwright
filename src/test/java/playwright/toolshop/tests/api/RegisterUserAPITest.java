package playwright.toolshop.tests.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.RequestOptions;
import net.datafaker.Faker;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import playwright.toolshop.domain.Address;
import playwright.toolshop.domain.User;


@UsePlaywright
public class RegisterUserAPITest {

    private APIRequestContext request;
    private Gson gson = new Gson();

    @BeforeEach
    void setUp(Playwright playwright) {
        request = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL("https://api.practicesoftwaretesting.com")
        );

    }

    @AfterEach
    void tearDown() {
        if (request != null) {
            request.dispose();
        }
    }

    @Test
    void should_register_user() {
        User validUser = User.randomUser();

        var response = request.post("/users/register",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(validUser)
        );

        String responseBody = response.text();

        System.out.println(responseBody);

        User createdUser = gson.fromJson(responseBody, User.class);

        JsonObject responseObject = gson.fromJson(responseBody, JsonObject.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.status())
                    .as("Registration should return 201 created status code")
                    .isEqualTo(201);

            softly.assertThat(createdUser)
                    .as("Created user should match the specified user without password")
                    .isEqualTo(validUser.withPassword(null));

            softly.assertThat(responseObject.get("id").getAsString())
                    .as("Registered user should have an id")
                    .isNotEmpty();

            softly.assertThat(
                            response.headers().get("content-type"))
                    .contains("application/json");

        });

    }

    @Test
    void fistNameIsMandatory() {

        Faker fake = new Faker();

        User invalidUser = new User(
                null,
                fake.name().lastName(),
                new Address(
                        fake.address().streetName(),
                        fake.address().city(),
                        fake.address().state(),
                        fake.address().country(),
                        fake.address().postcode()
                ),
                fake.phoneNumber().phoneNumber(),
                "1990-01-01",
                "Az123!&xyz",
                fake.internet().emailAddress()
        );

        var response = request.post("/users/register",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(invalidUser)
        );

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.status()).isEqualTo(422);

            JsonObject responseObject = gson.fromJson(response.text(), JsonObject.class);

            System.out.println(responseObject);

            softly.assertThat(responseObject.has("first_name")).isTrue();

            String errorMessage = responseObject.get("first_name").getAsString();

            softly.assertThat(errorMessage).isEqualTo("The first name field is required.");


        });

    }
}
