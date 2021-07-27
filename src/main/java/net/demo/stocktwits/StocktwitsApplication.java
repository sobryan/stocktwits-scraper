package net.demo.stocktwits;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import net.demo.stocktwits.pages.messages.StockTwitsMessageScrapeTask;

@SpringBootApplication
@EnableScheduling
public class StocktwitsApplication implements CommandLineRunner {
	
	private static final int THREADS_COUNT = 7;

	Logger logger = LoggerFactory.getLogger(StocktwitsApplication.class);
	
	@Value("${symbols}")
	String[] symbols;
	
	public static void main(String[] args) {
		SpringApplication.run(StocktwitsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		logger.info("SiteCheckerApplication in run");
		
		String baseUrl = "https://stocktwits.com/symbol/";
		
		for(String symbol: symbols) {
			logger.info("Working with symbol: " + symbol);
			StockTwitsMessageScrapeTask scrapeTask = new StockTwitsMessageScrapeTask(symbol + "-scrape", baseUrl+symbol, symbol);
			scrapeTask.run();
		}
		
	}
	
	/*
	 * Required for to run scheduled tasks at the same time
	 */
	
	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
	    ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
	    threadPoolTaskScheduler.setPoolSize(THREADS_COUNT);
	    return threadPoolTaskScheduler;
	}
	

}
