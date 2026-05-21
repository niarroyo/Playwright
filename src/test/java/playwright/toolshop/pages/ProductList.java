package playwright.toolshop.pages;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import playwright.toolshop.domain.ProductSummary;

import java.util.List;

public class ProductList {
    private final Page page;

    public ProductList(Page page) {
        this.page = page;
    }

    @Step("Get product name")
    public List<String> getProductNames() {
        return page.getByTestId("product-name").allInnerTexts();
    }

    @Step("Get product summary")
    public List<ProductSummary> getProductSummary() {
        return page.locator(".card").all()
                .stream()
                .map(productCard -> {
                    String productName = productCard.getByTestId("product-name").textContent().strip();
                    String productPrice = productCard.getByTestId("product-price").textContent();
                    return new ProductSummary(productName, productPrice);
                        }).toList();

    }

    @Step("View product details")
    public void viewProductDetails(String productName) {
        page.locator(".card").getByText(productName).click();
    }

    public String getSearchCompletedMessage() { return page.getByTestId("search_completed").textContent(); }
}
