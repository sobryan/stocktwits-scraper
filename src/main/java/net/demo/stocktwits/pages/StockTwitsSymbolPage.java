package net.demo.stocktwits.pages;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.WaitForSelectorOptions;
import com.microsoft.playwright.Playwright;

import net.demo.stocktwits.pages.messages.TwitMessage;

public class StockTwitsSymbolPage extends WebPage {

	Logger logger = LoggerFactory.getLogger(StockTwitsSymbolPage.class);

	String articleSelector = "article";
	String messageSelector = ".st_3SL2gug";
	String sentimentSelector = ".st_11GoBZI";
	String bullishArticleSelector = "article:has(.st_11GoBZI:has-text(\"Bullish\"))";
	String bearishArticleSelector = "article:has(.st_11GoBZI:has-text(\"Bearish\"))";
	
	public StockTwitsSymbolPage(String title, String url, String symbol) throws IOException {
		super(title, url, symbol);
	}

	@Override
	public void executeTasks() {
		try (Playwright playwright = Playwright.create()) {
			logger.info(this.TITLE + "- Starting browser");
			BrowserType browserType = playwright.firefox();
			LaunchOptions launchOptions = new LaunchOptions();
			launchOptions.setHeadless(false);

		try (Browser browser = browserType.launch(new LaunchOptions().setHeadless(false))) {
				BrowserContext context = browser.newContext();
				Page page = context.newPage();
				
				WaitForSelectorOptions options = new WaitForSelectorOptions();
				options.setTimeout(10000);
				
				retrieveMessages(page, options);
				

			} catch (Exception ex) {
				logger.error("Exception in retrieving Stocktwits page", ex);
			}

		}

	}

	@Override
	public void retrieveMessages(Page page, WaitForSelectorOptions options) throws Exception {
		
		page.navigate(this.URL);
		page.waitForSelector(messageSelector);
		loadMoreMessages(page);
		
		
		ArrayList<ElementHandle> bullishTwits = queryForElements(page, bullishArticleSelector, options,"query-for-all-messages");
		ArrayList<ElementHandle> bearishTwits = queryForElements(page, bearishArticleSelector, options,"query-for-all-messages");
		
		ArrayList<TwitMessage> bullishMessages = extractMessages(bullishTwits, Short.parseShort("1"), messageSelector, SYMBOL);
		ArrayList<TwitMessage> bearishMessages = extractMessages(bearishTwits, Short.parseShort("0"), messageSelector, SYMBOL);
		
		logger.info("Bullish Size: " + bullishTwits.size());
		logger.info("Bearish Size: " + bearishTwits.size());
		
		report(bullishMessages);
		report(bearishMessages);
		
		logger.info("Completed selecting twits");
		
	}

	
	
}
