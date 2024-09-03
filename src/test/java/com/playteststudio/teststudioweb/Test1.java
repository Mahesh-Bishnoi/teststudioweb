package com.playteststudio.teststudioweb;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.*;

import org.junit.jupiter.api.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.*;

//@UsePlaywright
public class Test1 {

  // Shared between all tests in this class.
  static Playwright playwright;
  static Browser browser;

  // New instance for each test method.
  BrowserContext context;
  Page page;

  @BeforeAll
  static void launchBrowser() {
    playwright = Playwright.create();
    browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(3000));
  }

  @AfterAll
  static void closeBrowser() {
    playwright.close();
  }

  @BeforeEach
  void createContextAndPage() {
    context = browser.newContext();
    page = context.newPage();
  }

  @AfterEach
  void closeContext() {
    context.close();
  }

  @Test
  void test(/*Page page*/) {
    page.navigate("https://www.google.com/");
    assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Paris Games tennis"))).isVisible();
    assertThat(page.getByLabel("Search", new Page.GetByLabelOptions().setExact(true))).isVisible();
  }
}
