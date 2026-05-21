package playwright.examples;

import com.microsoft.playwright.options.LoadState;
import org.junit.jupiter.api.*;
import org.assertj.core.api.Assertions;
import playwright.toolshop.fixtures.PlaywrightTestCase;

import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PlaywrightAssertion extends PlaywrightTestCase {

    @DisplayName("Making assertions about the contents of a field")
    @Nested
    class LocatingElementsUsingCss {

        @BeforeEach
        void openContactPage() {
            page.navigate("https://practicesoftwaretesting.com/contact");

        }

        @DisplayName("Checking the value of a field")
        @Test
        void fieldValues(){
            var firstNameField = page.getByLabel("First name");

            firstNameField.fill("Sarah-Jane");

            assertThat(firstNameField).hasValue("Sarah-Jane");

            assertThat(firstNameField).not().isDisabled();
            assertThat(firstNameField).isVisible();
            assertThat(firstNameField).isEditable();

        }

        @DisplayName("Making assertions about datat values")
        @Nested
        class MakingAssertionsAboutDataValues {

            @BeforeEach
            void openHomePage() {
                page.navigate("https://practicesoftwaretesting.com");
                page.waitForCondition(() -> page.getByTestId("product-name").count() > 0);
            }

            @Test
            void allProductPricesShouldBeCorrectedValues() {
                List<Double> prices = page.getByTestId("product-price")
                        .allInnerTexts()
                        .stream()
                        .map(price -> Double.parseDouble(price.replace("$", "")))
                        .toList();

                Assertions.assertThat(prices)
                        .isNotEmpty()
                        .allMatch(price -> price > 0)
                        .doesNotContain(0.0)
                        .allMatch(price -> price < 1000)
                        .allSatisfy(price ->
                                Assertions.assertThat(price)
                                        .isGreaterThan(0.0)
                                        .isLessThan(1000.0));

            }

            @Test
            void shouldSortInAlphabeticalOrder() {
                page.getByLabel("Sort").selectOption("Name (A - Z)");
                page.waitForLoadState(LoadState.NETWORKIDLE);

                List<String> productNames = page.getByTestId("product-name").allTextContents();

                Assertions.assertThat(productNames).isSortedAccordingTo(String.CASE_INSENSITIVE_ORDER);
            }

            @Test
            void shouldSortInReverseAlphabeticalOrder() {
                page.getByLabel("Sort").selectOption("Name (Z - A)");
                page.waitForLoadState(LoadState.NETWORKIDLE);

                List<String> productNames = page.getByTestId("product-name").allTextContents();

                Assertions.assertThat(productNames).isSortedAccordingTo(Comparator.reverseOrder());
            }




        }








    }




}
