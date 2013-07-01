package models;

import javax.persistence.*;
import play.db.jpa.*;
import play.modules.search.*;
import utils.CurrencyUtils;
import java.util.*;

@Entity
@Indexed
public class Book extends Model {
	@Field
	public String title;
	@Field
	public String author;
	@Field
	public String isbn;
	@Field
	public String sku;
	public double purchasePrice;
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
	
	public Book addSalesHistory(Date date, int salesQuantity, String type) {
	    SalesSummary newSalesSummary = new SalesSummary(this, date, salesQuantity, type).save();
	    this.salesSummaries.add(newSalesSummary);
	    this.save();
	    return this;
	}
	
	public String getPhotoUrl() {
		String url = "http://images.thebookpeople.co.uk/images/books/thumbnail/"+sku+".jpg";		
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
			list = SalesSummary.find("type = ? and book = ?", chartType, this).fetch();
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
}
