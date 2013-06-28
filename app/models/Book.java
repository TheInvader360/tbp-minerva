package models;

import javax.persistence.*;
import play.db.jpa.*;
import play.modules.search.*;
import utils.CurrencyUtils;

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
	
	public Book(String title, String author, String isbn, String sku, double purchasePrice, double salePrice) {
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.sku = sku;
		this.purchasePrice = purchasePrice;
		this.salePrice = salePrice;
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
}
