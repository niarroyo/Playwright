package playwright.toolshop.tests.ui;

import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import playwright.toolshop.fixtures.PlaywrightTestCase;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;


import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ContactFormsTest extends PlaywrightTestCase {

    @DisplayName("Interacting with text fields")
    @Nested
    @Feature("Contacts")
    class WhenInteractingWithTextFields {

        @BeforeEach
        void openContactPage() {
            page.navigate("https://practicesoftwaretesting.com/contact");
        }

        @DisplayName("Input fields")
        @Story("Contact Form")
        @Test
        void completeForm() throws URISyntaxException {
            var firstNameField = page.getByLabel("First name");
            var lastNameField = page.getByLabel("Last name");
            var emailField = page.getByLabel("Email address");
            var messageField= page.getByLabel("Message");
            var subjectField = page.getByLabel("Subject");
            var uploadField = page.getByLabel("Attachment");

            firstNameField.fill("Chloe");
            lastNameField.fill("Villarroel");
            emailField.fill("chlov@example.com");
            messageField.fill("Hello, world!");
            subjectField.selectOption("Payments");
            //subjectField.selectOption(new SelectOption().setIndex(2));

            Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/sample-data.txt").toURI());

            page.setInputFiles("#attachment", fileToUpload);


            assertThat(firstNameField).hasValue("Chloe");
            assertThat(lastNameField).hasValue("Villarroel");
            assertThat(emailField).hasValue("chlov@example.com");
            assertThat(messageField).hasValue("Hello, world!");
            assertThat(subjectField).hasValue("payments");

            String uploadedFile = uploadField.inputValue();
            org.assertj.core.api.Assertions.assertThat(uploadedFile).endsWith("sample-data.txt");
        }

        @DisplayName("Mandatory fields")
        @Story("Contact Form")
        @ParameterizedTest
        @ValueSource(strings = {"First name", "Last name", "Email", "Message"})
        void mandatoryFields(String fieldName) {
            var firstNameField = page.getByLabel("First name");
            var lastNameField = page.getByLabel("Last name");
            var emailField = page.getByLabel("Email");
            var messageField= page.getByLabel("Message");
            var subjectField = page.getByLabel("Subject");
            var sendButton = page.getByText("Send");

            //fill in the field names
            firstNameField.fill("Chloe");
            lastNameField.fill("Villarroel");
            emailField.fill("chlov@example.com");
            messageField.fill("Hello, world!");
            subjectField.selectOption("Payments");

            //clear the fields
            page.getByLabel(fieldName).clear();

            //submit the form
            sendButton.click();

            //check the error message for that field

            var errorMessage = page.getByRole(AriaRole.ALERT).getByText(fieldName + " is required");

            assertThat(errorMessage).isVisible();

        }

    }
}
