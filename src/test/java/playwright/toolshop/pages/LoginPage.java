package playwright.toolshop.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import playwright.toolshop.domain.User;

public class LoginPage {
    private final Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    @Step("Open Login Page")
    public void open() {
        page.navigate("https://practicesoftwaretesting.com/auth/login");
    }

    @Step("Log in with valid user")
    public void loginAs(User user) {
        page.getByPlaceholder("Your email").fill(user.email());
        page.getByPlaceholder("Your password").fill(user.password());
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Login")).click();

    }

    public String title() {
        return page.getByTestId("page-title").textContent();
    }

    @Step("Get log in error message")
    public String loginErrorMessage() {
        return page.getByTestId("login-error").textContent();
    }
}
