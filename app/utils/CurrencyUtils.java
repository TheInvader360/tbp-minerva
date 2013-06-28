package utils;

import java.text.DecimalFormat;

public class CurrencyUtils {
	
	public static String currencyFormat(double amount, String currency) {
		// format to two decimal places
		DecimalFormat currencyPattern = new DecimalFormat("0.00");
		String result = currencyPattern.format(amount);
		
		// prefix with relevant currency symbol (or not if invalid/unknown currency code is passed in)
		if (currency == "GBP") result = "£" + result;
		if (currency == "EUR") result = "€" + result;
		if (currency == "USD") result = "$" + result;
		
		return result;
	}
}
