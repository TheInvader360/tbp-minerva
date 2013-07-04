package models;

import javax.persistence.*;
import javax.persistence.Query;

import play.data.validation.Required;
import play.db.jpa.*;
import play.modules.search.*;
import utils.CurrencyUtils;

import java.util.*;

@Entity
@Indexed
public class Book extends Model {
	@Field
	@Required
	public String title;
	@Field
	@Required
	public String author;
	@Field
	@Required
	public String isbn;
	@Field
	@Required
	public String sku;
	@Required
	public double purchasePrice;
	@Required
	public double salePrice;
	@OneToMany(mappedBy="book", cascade=CascadeType.ALL)
	public List<SalesSummary> salesSummaries;
	
	public Book(String title, String author, String isbn, String sku, double purchasePrice, double salePrice) {
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.sku = sku;
		this.purchasePrice = purchasePrice;
		this.salePrice = salePrice;
		this.salesSummaries = new ArrayList<SalesSummary>();
	}
	
	public String getPhotoUrl() {
		String url = "//images.thebookpeople.co.uk/images/books/thumbnail/"+sku+".jpg";		
		return url;
	}
	
	public String getCurrencyFormatPuchasePrice() {
		return CurrencyUtils.currencyFormat(purchasePrice, "GBP");
	}

	public String getCurrencyFormatSalePrice() {
		return CurrencyUtils.currencyFormat(salePrice, "GBP");
	}
	
	public String getChartData(String chartType) {
		String points = "";
		List<SalesSummary> list = new ArrayList<SalesSummary>();
		
		if (chartType == "all") {
			// get all SalesSummary rows for this book
			list = SalesSummary.find("book = ?", this).fetch();
		}
		else {
			// get all SalesSummary rows of a particular type for this book
			list = SalesSummary.find("salesChannel.tag = ? and book = ?", chartType, this).fetch();
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
	
	public int getTotalUnitSales() {
		Query q = JPA.em().createQuery("SELECT SUM(ss.salesQuantity) FROM SalesSummary ss INNER JOIN ss.salesChannel sc WHERE book_id = "+this.id);
		Long sum = (Long) q.getSingleResult();
		if (sum == null) sum = 0l;
		return sum.intValue();
	}
	
	public double getTotalPurchaseCost() {
		return getTotalUnitSales() * this.purchasePrice;
	}
	public String getCurrencyFormatTotalPurchaseCost() {
		return CurrencyUtils.currencyFormat(getTotalPurchaseCost(), "GBP");
	}
	
	public double getTotalSalesRevenue() {
		return getTotalUnitSales() * this.salePrice;
	}
	public String getCurrencyFormatTotalSalesRevenue() {
		return CurrencyUtils.currencyFormat(getTotalSalesRevenue(), "GBP");
	}
	
	public double getCostsEstimate() {
		// Iterate through all sales channels and return the sum of all cost estimates
		double total = 0;
		List<SalesChannel> allSalesChannels = SalesChannel.find("order by tag asc").fetch();
		Iterator<SalesChannel> i = allSalesChannels.iterator();
		while (i.hasNext()) {
			SalesChannel sc = i.next();
			total += getCostsEstimate(sc.tag);
		}
		return total;
	}
	public String getCurrencyFormatCostsEstimate() {
		return CurrencyUtils.currencyFormat(getCostsEstimate(), "GBP");
	}
	
	public double getProfit() {
		return getTotalSalesRevenue() - (getTotalPurchaseCost() + getCostsEstimate());
	}
	public String getCurrencyFormatProfit() {
		return CurrencyUtils.currencyFormat(getProfit(), "GBP");
	}
	
	public int getTotalUnitSales(String channelTag) {
		Query q = JPA.em().createQuery("SELECT SUM(ss.salesQuantity) FROM SalesSummary ss INNER JOIN ss.salesChannel sc WHERE sc.tag = '"+channelTag+"' AND book_id = "+this.id);
		Long sum = (Long) q.getSingleResult();
		if (sum == null) sum = 0l;
		return sum.intValue();
	}
	
	public double getTotalPurchaseCost(String channelTag) {
		return getTotalUnitSales(channelTag) * this.purchasePrice;
	}
	public String getCurrencyFormatTotalPurchaseCost(String channelTag) {
		return CurrencyUtils.currencyFormat(getTotalPurchaseCost(channelTag), "GBP");
	}
	
	public double getTotalSalesRevenue(String channelTag) {
		return getTotalUnitSales(channelTag) * this.salePrice;
	}
	public String getCurrencyFormatTotalSalesRevenue(String channelTag) {
		return CurrencyUtils.currencyFormat(getTotalSalesRevenue(channelTag), "GBP");
	}

	public double getCostsEstimate(String channelTag) {
		SalesChannel salesChannel = SalesChannel.find("byTag", channelTag).first();
		return getTotalSalesRevenue(channelTag) * (salesChannel.costPercentage / 100d);
	}
	public String getCurrencyFormatCostsEstimate(String channelTag) {
		return CurrencyUtils.currencyFormat(getCostsEstimate(channelTag), "GBP");
	}
	
	public double getProfit(String channelTag) {
		return getTotalSalesRevenue(channelTag) - (getTotalPurchaseCost(channelTag) + getCostsEstimate(channelTag));
	}
	public String getCurrencyFormatProfit(String channelTag) {
		return CurrencyUtils.currencyFormat(getProfit(channelTag), "GBP");
	}
	
	public String toString() {
	    return sku+" - "+title;
	}
}
