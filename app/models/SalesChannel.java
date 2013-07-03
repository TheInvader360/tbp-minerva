package models;
 
import java.util.*;

import javax.persistence.*;
 
import play.db.jpa.*;
 
@Entity
public class SalesChannel extends Model {
    public String tag;
    public float costPercentage;
	@OneToMany(mappedBy="salesChannel", cascade=CascadeType.ALL)
	public List<SalesSummary> salesSummaries;
    
    public SalesChannel(String tag, float costPercentage) {
    	this.tag = tag;
    	this.costPercentage = costPercentage;
    	this.salesSummaries = new ArrayList<SalesSummary>();
    }
}
