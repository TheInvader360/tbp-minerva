package models;
 
import java.util.*;
import javax.persistence.*;
 
import play.data.validation.Required;
import play.db.jpa.*;
 
@Entity
public class SalesSummary extends Model {
    @ManyToOne
    @Required
    public Book book;
    @ManyToOne
    @Required
    public SalesChannel salesChannel;
    @Required
    public Date summaryDate;
    @Required
    public int salesQuantity;
    
    public SalesSummary(Book book, SalesChannel salesChannel, Date summaryDate, int salesQuantity) {
        this.book = book;
        this.salesChannel = salesChannel;
        this.summaryDate = summaryDate;
        this.salesQuantity = salesQuantity;
    }
    
    public String toString() {
    	return book.toString()+" | "+salesChannel.toString()+" | "+summaryDate;
    }
}
