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
	
	public String toString() {
	    return sku+" - "+title;
	}
}
