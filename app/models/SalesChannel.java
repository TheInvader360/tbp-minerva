package models;
 
import java.util.*;

import javax.persistence.*;
 
import play.data.validation.Required;
import play.db.jpa.*;
 
@Entity
public class SalesChannel extends Model {
	@Required
	public String name;
	@Required
	public String tag;
	@Required
    public double costPercentage;
	@OneToMany(mappedBy="salesChannel", cascade=CascadeType.ALL)
	public List<SalesSummary> salesSummaries;
    
    public SalesChannel(String name, String tag, float costPercentage) {
    	this.name = name;
    	this.tag = tag;
    	this.costPercentage = costPercentage;
    	this.salesSummaries = new ArrayList<SalesSummary>();
    }
    
	public String toString() {
	    return name;
	}
}
