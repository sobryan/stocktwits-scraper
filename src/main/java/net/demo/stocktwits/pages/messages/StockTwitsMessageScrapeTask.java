package net.demo.stocktwits.pages.messages;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.demo.stocktwits.pages.StockTwitsSymbolPage;

//@Component
public class StockTwitsMessageScrapeTask extends StockTwitsSymbolPage{

	Logger logger = LoggerFactory.getLogger(StockTwitsMessageScrapeTask.class);
	
	public StockTwitsMessageScrapeTask(String title, String url, String symbol) throws IOException {
		super(title,url,symbol);
	}
	
	@Override
//	@Scheduled(fixedDelayString = "${fixed-delay.stocktwits}")
	public void run() {
		logger.info("Starting scheduled task - " + TITLE);
		super.run();
	}
	
}
