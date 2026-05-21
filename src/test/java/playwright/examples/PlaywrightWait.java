package playwright.examples;

import com.microsoft.playwright.options.AriaRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import playwright.toolshop.fixtures.PlaywrightTestCase;

import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PlaywrightWait extends PlaywrightTestCase {

    @Nested
    class WaitingForState {

        @BeforeEach
        void openHomePage() {
            page.navigate("https://practicesoftwaretesting.com/");
            page.waitForSelector(".card-img-top");
        }

        @Test
        void shouldShowAllProductNames() {
            List<String> productNames = page.getByTestId("product-name").allInnerTexts();
            Assertions.assertThat(productNames).contains("Pliers", "Bolt Cutters", "Hammer");

        }

        @Test
        void shouldShowAllProductImages() {
            List<String> productImageTitles = page.locator(".card-img-top").all()
                    .stream()
                    .map(img -> img.getAttribute("alt"))
                    .toList();

            Assertions.assertThat(productImageTitles).contains("Pliers", "Bolt Cutters", "Hammer");
        }

    }

    @Nested
    class AutomaticWaits {

        @BeforeEach
        void openHomePage() {
            page.navigate("https://practicesoftwaretesting.com/");
        }

        @Test
        void shouldWaitForTheFilterCheckboxes() {
            var screwdriverFilter = page.getByLabel("Screwdriver");

            screwdriverFilter.click();

            assertThat(screwdriverFilter).isChecked();
        }

        @Test
        void shouldFilterProductsByCategory() {
            page.getByRole(AriaRole.MENUBAR).getByText("Categories").click();
            page.getByRole(AriaRole.MENUBAR).getByText("Power Tools").click();

            page.waitForSelector(".card");
//            page.waitForSelector(".card", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));

            var filterProducts = page.getByTestId("product-name").allInnerTexts();

            Assertions.assertThat(filterProducts).contains("Sheet Sander", "Belt Sander", "Random Orbit Sander");
        }

    }

    @Nested
    class WaitingForElementsToAppearAndDisappear {
        @BeforeEach
        void openHomePage() {
            page.navigate("https://practicesoftwaretesting.com/");
        }

        @Test
        @DisplayName("It should display a toaster message when an item is added to the cart")
        void shouldDisplayToasterMessage() {
            page.getByText("Bolt Cutters").click();
            page.getByText("Add to cart").click();

            //wait for the toaster message to appear
            assertThat(page.getByRole(AriaRole.ALERT)).isVisible();
            assertThat(page.getByRole(AriaRole.ALERT)).hasText("Product added to shopping cart.");

            page.waitForCondition( () -> page.getByRole(AriaRole.ALERT).isHidden());

        }

        @Test
        void shouldUpdateCartItemCount() {
            page.getByText("Bolt Cutters").click();
            page.getByText("Add to cart").click();

            page.waitForCondition( () -> page.getByTestId("cart-quantity").textContent().equals("1"));
        }

    }

    @Nested
    class WaitingForAPICall {
        @BeforeEach
        void openHomePage() {
            page.navigate("https://practicesoftwaretesting.com/");
        }

        @Test
        void sortByDescendingPrice() {
            //wait for api call response
            page.waitForResponse("**/products?page=0&sort=price**",
                    () -> {
                        page.getByTestId("sort").selectOption("Price (High - Low)");
                    });


            //find all the prices on the page
            var productPrices = page.getByTestId("product-price")
                    .allInnerTexts()
                    .stream()
                    .map(WaitingForAPICall::extractPrice)
                    .toList();

            //Are the prices in the correct order
            System.out.println("ProductPrices" + productPrices);
            Assertions.assertThat(productPrices)
                    .isNotEmpty()
                    .isSortedAccordingTo(Comparator.reverseOrder());

        }

        private static double extractPrice(String price) {
            return Double.parseDouble(price.replace("$", ""));
        }
    }
}
