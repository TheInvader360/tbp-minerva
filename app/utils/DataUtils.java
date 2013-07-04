package utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.Query;

import play.db.jpa.JPA;

import models.Book;
import models.SalesChannel;
import models.SalesSummary;

public class DataUtils {
	
	public String getChartDataPoints(Book book, SalesChannel salesChannel) {
		String points = "";
		List<SalesSummary> list = new ArrayList<SalesSummary>();
		
		if (salesChannel == null) {
			// get all SalesSummary rows for this book
			list = SalesSummary.find("book = ?", book).fetch();
		}
		else {
			// get all SalesSummary rows from a particular sales channel for this book
			list = SalesSummary.find("salesChannel = ? and book = ?", salesChannel, book).fetch();
		}

		// populate a treemap (default sort by key asc) with list values (key:summaryDate / value:salesQuantity)
		Map map = new TreeMap<Date, Integer>();
		for (int i = 0; i < list.size(); i++) {
			if (map.containsKey(list.get(i).summaryDate)) {
				// if summaryDate already exists, sum the values
				map.put(list.get(i).summaryDate, (Integer) map.get(list.get(i).summaryDate) + list.get(i).salesQuantity);
			}
			else {
				// otherwise simply insert the key value pair
				map.put(list.get(i).summaryDate, list.get(i).salesQuantity);
			}
		}

		// form a string that the chart script can make use of
		Set set = map.entrySet(); 
		Iterator i = set.iterator(); 
		while (i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			points = points+"[\'"+me.getKey()+"\',"+me.getValue()+"],";
		}

		return points;
	}
	
	public int getTotalUnitSales(Book book) {
		Query q = JPA.em().createQuery("SELECT SUM(ss.salesQuantity) FROM SalesSummary ss INNER JOIN ss.salesChannel sc WHERE book_id = "+book.id);
		Long sum = (Long) q.getSingleResult();
		if (sum == null) sum = 0l;
		return sum.intValue();
	}
	
	public double getTotalPurchaseCost(Book book) {
		return getTotalUnitSales(book) * book.purchasePrice;
	}
	
	public String getCurrencyFormatTotalPurchaseCost(Book book) {
		return CurrencyUtils.currencyFormat(getTotalPurchaseCost(book), "GBP");
	}
	
	public double getTotalSalesRevenue(Book book) {
		return getTotalUnitSales(book) * book.salePrice;
	}
	
	public String getCurrencyFormatTotalSalesRevenue(Book book) {
		return CurrencyUtils.currencyFormat(getTotalSalesRevenue(book), "GBP");
	}
	
	public double getCostsEstimate(Book book) {
		// Iterate through all sales channels and return the sum of all cost estimates
		double total = 0;
		List<SalesChannel> allSalesChannels = SalesChannel.find("order by tag asc").fetch();
		Iterator<SalesChannel> i = allSalesChannels.iterator();
		while (i.hasNext()) {
			SalesChannel sc = i.next();
			total += getCostsEstimate(book, sc);
		}
		return total;
	}
	public String getCurrencyFormatCostsEstimate(Book book) {
		return CurrencyUtils.currencyFormat(getCostsEstimate(book), "GBP");
	}
	
	public double getProfit(Book book) {
		return getTotalSalesRevenue(book) - (getTotalPurchaseCost(book) + getCostsEstimate(book));
	}
	public String getCurrencyFormatProfit(Book book) {
		return CurrencyUtils.currencyFormat(getProfit(book), "GBP");
	}
	
	public int getTotalUnitSales(Book book, SalesChannel salesChannel) {
		Query q = JPA.em().createQuery("SELECT SUM(ss.salesQuantity) FROM SalesSummary ss INNER JOIN ss.salesChannel sc WHERE sc.tag = '"+salesChannel.tag+"' AND book_id = "+book.id);
		Long sum = (Long) q.getSingleResult();
		if (sum == null) sum = 0l;
		return sum.intValue();
	}
	
	public double getTotalPurchaseCost(Book book, SalesChannel salesChannel) {
		return getTotalUnitSales(book, salesChannel) * book.purchasePrice;
	}
	public String getCurrencyFormatTotalPurchaseCost(Book book, SalesChannel salesChannel) {
		return CurrencyUtils.currencyFormat(getTotalPurchaseCost(book, salesChannel), "GBP");
	}
	
	public double getTotalSalesRevenue(Book book, SalesChannel salesChannel) {
		return getTotalUnitSales(book, salesChannel) * book.salePrice;
	}
	public String getCurrencyFormatTotalSalesRevenue(Book book, SalesChannel salesChannel) {
		return CurrencyUtils.currencyFormat(getTotalSalesRevenue(book, salesChannel), "GBP");
	}

	public double getCostsEstimate(Book book, SalesChannel salesChannel) {
		return getTotalSalesRevenue(book, salesChannel) * (salesChannel.costPercentage / 100d);
	}
	public String getCurrencyFormatCostsEstimate(Book book, SalesChannel salesChannel) {
		return CurrencyUtils.currencyFormat(getCostsEstimate(book, salesChannel), "GBP");
	}
	
	public double getProfit(Book book, SalesChannel salesChannel) {
		return getTotalSalesRevenue(book, salesChannel) - (getTotalPurchaseCost(book, salesChannel) + getCostsEstimate(book, salesChannel));
	}
	public String getCurrencyFormatProfit(Book book, SalesChannel salesChannel) {
		return CurrencyUtils.currencyFormat(getProfit(book, salesChannel), "GBP");
	}
}
