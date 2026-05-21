package playwright.toolshop.pages;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class NavBar {
    private final Page page;

    public NavBar(Page page) {
        this.page = page;
    }
    @Step("Click on Cart")
    public void openCart() {
        page.getByTestId("nav-cart").click();
    }

    @Step("Open Home page")
    public void openHomePage() { page.navigate("https://practicesoftwaretesting.com");
    }

    @Step("Open the Contact page")
    public void toTheContactPage() {
        page.navigate("https://practicesoftwaretesting.com/contact");
    }
}
