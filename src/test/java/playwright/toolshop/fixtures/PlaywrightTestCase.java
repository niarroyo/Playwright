package playwright.toolshop.fixtures;

import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

public class PlaywrightTestCase {

    protected static ThreadLocal<Playwright> playwright
            = ThreadLocal.withInitial(() -> {
                Playwright playwright = Playwright.create();
                playwright.selectors().setTestIdAttribute("data-test");
                return playwright;
            }
    );

    protected static ThreadLocal<Browser> browser = ThreadLocal.withInitial(() ->
            playwright.get().chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(true)
                            .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
            )
    );

    protected BrowserContext browserContext;

    protected Page page;

    @BeforeEach
    void setUpBrowserContext() {
        browserContext = browser.get().newContext();
        page = browserContext.newPage();
        page.navigate("https://practicesoftwaretesting.com");
    }

    @AfterEach
    void closeContext() {
        takeScreenshot("End of test");
        browserContext.close();
    }

    protected void takeScreenshot(String name) {
        var screenshot = page.screenshot(
                new Page.ScreenshotOptions().setFullPage(true)
        );
        Allure.addAttachment(name, new ByteArrayInputStream(screenshot));
    }

    @AfterAll
    static void tearDown() {
        browser.get().close();
        browser.remove();

        playwright.get().close();
        playwright.remove();
    }
}
