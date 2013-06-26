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
}
