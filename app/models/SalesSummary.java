package models;
 
import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
 
@Entity
public class SalesSummary extends Model {
    @ManyToOne
    public Book book;
    @ManyToOne
    public SalesChannel salesChannel;
    public Date summaryDate;
    public int salesQuantity;
    
    public SalesSummary(Book book, SalesChannel salesChannel, Date summaryDate, int salesQuantity) {
        this.book = book;
        this.salesChannel = salesChannel;
        this.summaryDate = summaryDate;
        this.salesQuantity = salesQuantity;
    }
}
