package playwright.examples;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;
import playwright.toolshop.fixtures.PlaywrightTestCase;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class PlaywrightLocator extends PlaywrightTestCase {

    
    @DisplayName("Locating an element by text contents")
    @Test
    void byText() {
        page.getByText("Bolt Cutters").click();

        PlaywrightAssertions.assertThat(page.getByText("MightyCraft Hardware")).isVisible();
    }

    @DisplayName("Using alt text")
    @Test
    void byAltText() {
        page.navigate("https://practicesoftwaretesting.com/");
        page.getByAltText("Combination Pliers").click();;

        PlaywrightAssertions.assertThat(page.getByText("ForgeFlex Tools")).isVisible();
    }

    @DisplayName("Using Title")
    @Test
    void byTitle() {
        page.getByAltText("Combination Pliers").click();
        page.getByTitle("Practice Software Testing - Toolshop").click();
    }

    @DisplayName("Using Label")
    @Test
    void byLabel() {
        page.getByText("Sign in").click();

        page.getByLabel("Email address *").fill("Norma@test.com");
        page.getByLabel("Password *").fill("TestQA25$");

    }

    @DisplayName("Using Role")
    @Test
    void byRole() {
        page.getByLabel("Search").fill("Hammer");

        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Search")).click();

        PlaywrightAssertions.assertThat(page.getByText("Hammer")).isVisible();
    }

    //customizing the data-test field
    @DisplayName("Using TestId")
    @Test
    void byTestId() {
        page.getByTestId("search-query").fill("pliers");
        page.getByTestId("search-submit").click();
    }

    @DisplayName("Using By CSS")
    @Test
    void locateSendButtonByCss() {
        page.getByText("Contact").click();
        page.locator("#first_name").fill("Norma");
        page.locator(".btnSubmit").click();

        List<String> alertMessages = page.locator(".alert").allTextContents();
        assertThat(!alertMessages.isEmpty());
    }

    @DisplayName("Nested Elements")
    @Test
    void locatingNestedElement(){
        page.getByRole(AriaRole.MENUBAR, new Page.GetByRoleOptions().setName("Main Menu"))
                .getByText("Contact")
                .click();
    }

    @DisplayName("Filtering Results")
    @Test
    void filterResults(){
        List<String> allProducts = page.locator(".card")
                .filter(new Locator.FilterOptions().setHas(page.getByText("Out of stock")))
                .getByTestId("product-name")
                .allTextContents();
    }

    @DisplayName("Search for pliers")
    @Test
    void searchForPliers() {
        page.getByPlaceholder("Search").fill("Pliers");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();

        PlaywrightAssertions.assertThat(page.locator(".card")).hasCount(4);
        
        List<String> productNames = page.getByTestId("product-name").allTextContents();
        assertThat(productNames).allMatch(name -> name.contains("Pliers"));
        System.out.println(productNames.toString());

        Locator outOfStockItem = page.locator(".card")
                .filter(new Locator.FilterOptions().setHasText("Out of stock"))
                .getByTestId("product-name");

        PlaywrightAssertions.assertThat(outOfStockItem).hasCount(1);
        PlaywrightAssertions.assertThat(outOfStockItem).hasText("Long Nose Pliers");

    }


}
