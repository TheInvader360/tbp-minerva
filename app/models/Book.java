package models;

import javax.persistence.*;
import play.db.jpa.*;

@Entity
public class Book extends Model {
	public String title;
	public String author;
	public String isbn;
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
		// TODO - Force result to be to two decimal places e.g. £1.99, £2.50, £5.00, etc
		return "£"+purchasePrice;
	}

	public String getCurrencyFormatSalePrice() {
		// TODO - Force result to be to two decimal places e.g. £1.99, £2.50, £5.00, etc
		return "£"+salePrice;
	}
}
