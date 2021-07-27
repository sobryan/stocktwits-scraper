package net.demo.stocktwits.pages;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.WaitForSelectorOptions;

import net.demo.stocktwits.pages.messages.TwitMessage;

public abstract class WebPage {

	Logger logger = LoggerFactory.getLogger(WebPage.class);

	protected final String TITLE;
	protected final String URL;
	protected final String SYMBOL;
	protected CSVPrinter csvPrinter;

	public WebPage(String title, String url, String symbol) throws IOException {
		this.TITLE = title;
		this.URL = url;
		this.SYMBOL = symbol;

		BufferedWriter writer = Files.newBufferedWriter(Paths.get("./" + symbol + "-output.csv"));
		csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("symbol", "message", "sentiment"));

	}

	public void run() {
		executeTasks();
	}

	public abstract void retrieveMessages(Page page, WaitForSelectorOptions options) throws Exception;

	public abstract void executeTasks();

	public void report(ArrayList<TwitMessage> twitMessages) throws IOException {
		for (TwitMessage twitMessage : twitMessages) {
			csvPrinter.printRecord(twitMessage.getSymbol(),twitMessage.getMessage(),twitMessage.getSentiment());
		}
		csvPrinter.flush();

	}

	public void captureScreenshot(Page page, String step) {
		logger.info(this.TITLE + " step:" + step + " - creating screenshot");
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
			Files.createDirectories(Paths.get("screenshots/" + SYMBOL));

			page.screenshot(new Page.ScreenshotOptions()
					.setPath(Paths
							.get("screenshots/" + SYMBOL + "/" + step + "-" + formatter.format(new Date()) + ".png"))
					.setFullPage(true));
			logger.info(this.TITLE + " step:" + step + " - screenshot created");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(this.TITLE + " step:" + step + " - Unable to capture screenshort", e);
		}
	}

	
	/*
	 * Note: amount of times that page down pressed is hard coded
	 * This is how we get more messages
	 */
	public void loadMoreMessages(Page page) throws InterruptedException {

		for (int i = 0; i < 2000; i++) {
			page.keyboard().press("PageDown");
		}
		logger.info("pagedown success");

	}

	public ElementHandle queryForElement(Page page, String selector, WaitForSelectorOptions waitForSelectorOptions,
			String step) throws Exception {

		try {
			logger.error(this.TITLE + " step:" + step + " selector: " + selector + " - selecting element");
			page.waitForSelector(selector);
//			loadMoreMessages(page);
			ElementHandle elementHandle = page.waitForSelector(selector, waitForSelectorOptions);
			captureScreenshot(page, step);
			logger.error(this.TITLE + " step:" + step + " selector: " + selector + " - returning selected element");
			return elementHandle;
		} catch (Exception ex) {
			logger.error(this.TITLE + " step:" + step + " selector: " + selector + " - Failed to select element", ex);
			captureScreenshot(page, step);
			throw ex;

		}

	}

	ArrayList<TwitMessage> extractMessages(ArrayList<ElementHandle> elementHandles, Short sentiment,
			String messageSelector, String symbol) {

		ArrayList<TwitMessage> twitMessages = new ArrayList<TwitMessage>();
		for (ElementHandle elementHandle : elementHandles) {

			String message = elementHandle.querySelector(messageSelector).innerText();

			if (StringUtils.isNotBlank(message)) {
				twitMessages.add(new TwitMessage(symbol, StringUtils.normalizeSpace(message), sentiment));
			}

		}

		return twitMessages;
	}

	ArrayList<ElementHandle> queryForElements(Page page, String selector, WaitForSelectorOptions waitForSelectorOptions,
			String step) throws Exception {

		ArrayList<ElementHandle> elementHandles = new ArrayList<>();

		try {
			logger.error(this.TITLE + " step:" + step + " selector: " + selector + " - selecting element");
			elementHandles = (ArrayList<ElementHandle>) page.querySelectorAll(selector);
//			captureScreenshot(page, step);
			logger.error(this.TITLE + " step:" + step + " selector: " + selector + " - returning selected element");
			return elementHandles;
		} catch (Exception ex) {
			logger.error(this.TITLE + " step:" + step + " selector: " + selector + " - Failed to select element", ex);
//			captureScreenshot(page, step);
			throw ex;

		}

	}

}
