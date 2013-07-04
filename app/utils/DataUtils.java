package utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import models.Book;
import models.SalesChannel;
import models.SalesSummary;

public class DataUtils {
	
	public String getChartDataPoints(Book book, SalesChannel salesChannel) {
		String points = "";
		List<SalesSummary> list = new ArrayList<SalesSummary>();
		
		if (salesChannel == null) {
			// get all SalesSummary rows for this book
			list = SalesSummary.find("book = ?", book).fetch();
		}
		else {
			// get all SalesSummary rows from a particular sales channel for this book
			list = SalesSummary.find("salesChannel = ? and book = ?", salesChannel, book).fetch();
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
