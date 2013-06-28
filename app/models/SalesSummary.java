package models;
 
import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
 
@Entity
public class SalesSummary extends Model {
    @ManyToOne
    public Book book;
    public Date summaryDate;
    public int salesQuantity;
    
    public SalesSummary(Book book, Date summaryDate, int salesQuantity) {
        this.book = book;
        this.summaryDate = summaryDate;
        this.salesQuantity = salesQuantity;
    }
}
