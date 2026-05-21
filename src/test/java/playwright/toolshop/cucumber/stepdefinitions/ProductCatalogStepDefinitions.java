package playwright.toolshop.cucumber.stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;
import playwright.toolshop.domain.ProductSummary;
import playwright.toolshop.pages.NavBar;
import playwright.toolshop.pages.ProductList;
import playwright.toolshop.pages.SearchComponent;

import java.util.List;
import java.util.Map;


public class ProductCatalogStepDefinitions {

    NavBar navBar;
    SearchComponent searchComponent;
    ProductList productList;

    @Before
    public void setupPageObjects() {
        navBar = new NavBar(PlaywrightCucumberFixtures.getPage());
        searchComponent = new SearchComponent(PlaywrightCucumberFixtures.getPage());
        productList = new ProductList(PlaywrightCucumberFixtures.getPage());
    }

    @Given("Sally is on the homepage")
    public void sally_is_on_the_homepage() {
        navBar.openHomePage();

    }
    @When("she searches for {string}")
    public void she_searches_for(String searchTerm) {
        searchComponent.searchBy(searchTerm);

    }
    @Then("the {string} product should be displayed")
    public void the_product_should_be_displayed(String productName) {
        var matchingProducts = productList.getProductNames();
        Assertions.assertThat(matchingProducts).contains(productName);

    }

    @DataTableType
    public ProductSummary productSummaryRow(Map<String, String> productData) {
        return new ProductSummary(productData.get("Product"), productData.get("Price"));

    }


    @Then("the following products should be displayed:")
    public void theFollowingProductsShouldBeDisplayed(List<ProductSummary> expectedProductSummaries) {
        List<ProductSummary> matchingProducts = productList.getProductSummary();

//        List<Map<String, String>> expectedProductData = expectedProducts.asMaps();
//        List<ProductSummary> expectedProductSummaries =
//                expectedProductData.stream()
//                        .map(productData -> new ProductSummary(
//                                productData.get("Product"),
//                                productData.get("Price")
//                        )).toList();

        Assertions.assertThat(matchingProducts).containsExactlyInAnyOrderElementsOf(expectedProductSummaries);


    }


    @Then("no products should be displayed")
    public void noProductsShouldBeDisplayed() {
        List<ProductSummary> matchingProducts = productList.getProductSummary();
        Assertions.assertThat(matchingProducts).isEmpty();

    }

    @And("the message {string} should be displayed")
    public void theMessageShouldBeDisplayed(String messageText) {
        String completedMessage = productList.getSearchCompletedMessage();
        Assertions.assertThat(completedMessage).isEqualTo(messageText);
    }

    @And("she filters by {string}")
    public void sheFiltersBy(String filterName) {
        searchComponent.filterBy(filterName);

    }

    @When("she sorts by {string}")
    public void sheSortsBy(String sortFilter) {
        searchComponent.sortBy(sortFilter);
        
    }

    @Then("the first product displayed should be {string}")
    public void theFirstProductDisplayedShouldBe(String firstProductName) {
        List <String> productNames = productList.getProductNames();
        Assertions.assertThat(productNames).startsWith(firstProductName);

    }
}
