package playwright.toolshop.pages;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import playwright.toolshop.fixtures.ScreenshotManager;

public class ProductDetails {

    private final Page page;

    public ProductDetails(Page page) {
        this.page = page;
    }

    @Step("Increase cart quantity")
    public void increaseQuantityBy(int increment) {
        for (int i = 1; i<= increment; i++) {
            page.getByTestId("increase-quantity").click();
        }
        ScreenshotManager.takeScreenshot(page, "Quantity increased by " + increment);

    }

    @Step("Add to Cart")
    public void addToCart() {
        page.waitForResponse(
                response -> response.url().contains("/carts") && response.request().method().equals("POST"),
                () ->  page.getByText("Add to cart").click()
        );

        page.locator("#lblCartCount").waitFor();
        ScreenshotManager.takeScreenshot(page, "Added to cart");


    }
}
