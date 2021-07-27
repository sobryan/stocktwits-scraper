package net.demo.stocktwits.pages.messages;

public class TwitMessage {

	String symbol;
	String message;
	Short sentiment;
	public TwitMessage(String symbol, String message, Short sentiment) {
		this.symbol = symbol;
		this.message = message;
		this.sentiment = sentiment;
	}
	
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Short getSentiment() {
		return sentiment;
	}
	public void setSentiment(Short sentiment) {
		this.sentiment = sentiment;
	}
	
	
	
	
}
