package net.demo.stocktwits;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;

import net.demo.stocktwits.pages.messages.StockTwitsMessageScrapeTask;

public class StocktwitsRunner implements CommandLineRunner {

	Logger logger = LoggerFactory.getLogger(StocktwitsRunner.class);

	private final String baseUrl = "https://stocktwits.com/symbol/";
	
	@Value("${symbols}")
	String[] symbols;

	@Override
	public void run(String... args) throws Exception {
		logger.info("StocktwitsRunner in run");

		for (String symbol : symbols) {
			logger.info("Working with symbol: " + symbol);
			createScraper(symbol);
		}
	}

	@Async
	public void createScraper(String symbol) throws IOException {

		StockTwitsMessageScrapeTask scrapeTask = new StockTwitsMessageScrapeTask(symbol + "-scrape",
				baseUrl + symbol, symbol);
		scrapeTask.run();

	}

}
