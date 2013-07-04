import org.junit.*;
import java.util.*;
import play.test.*;
import utils.DataUtils;
import models.*;

public class BasicTest extends UnitTest {
	
	@Test
	public void fullTest() {
		// Tear down any existing data to start with a clean sheet
		Fixtures.deleteDatabase();
		
		// Load in the yaml canned data
		Fixtures.loadModels("data.yml");
		
		// Verify admin user account exists
		assertNotNull(User.connect("admin", "password"));
		
		// Count all books, there should be five...
		assertEquals(5, Book.count());
		
		// Find and count all Jamie Oliver books, there should be two...
		List<Book> jamieBooks = Book.find("author", "Jamie Oliver").fetch();
		assertEquals(2, jamieBooks.size());
		
		// Find the True Blood Collection, verify that it has fifteen SalesSummary rows...
		Book book = Book.find("sku", "TBLD").first();
		assertEquals("True Blood Collection", book.title);
		assertEquals(15, book.salesSummaries.size());
		
		// Verify that it has five catalogue SalesSummary rows...
		List<SalesSummary> tbldCatalogueRows = SalesSummary.find("book.sku=? and salesChannel.tag=?", "TBLD", "cat").fetch();
		assertEquals(5, tbldCatalogueRows.size());
		
		// Get a SalesChannel
		SalesChannel salesChannel = SalesChannel.find("tag", "cat").first();
		assertEquals("Catalogue", salesChannel.name);
		
		
		// Verify DataUtils is working correctly
		DataUtils dataUtils = new DataUtils();
		
		String data = dataUtils.getChartDataPoints(book, salesChannel);
		assertEquals("['2012-11-01 00:00:00.0',1323],['2012-12-01 00:00:00.0',1222],['2013-01-01 00:00:00.0',1141],['2013-02-01 00:00:00.0',864],['2013-03-01 00:00:00.0',926],", data);
		
		int totalUnitSalesAll = dataUtils.getTotalUnitSales(book);
		assertEquals(21865, totalUnitSalesAll);
		
		String currencyFormatTotalPurchaseCostAll = dataUtils.getCurrencyFormatTotalPurchaseCost(book);
		assertEquals("£60128.75", currencyFormatTotalPurchaseCostAll);
		
		String currencyFormatTotalSalesRevenueAll = dataUtils.getCurrencyFormatTotalSalesRevenue(book);
		assertEquals("£109325.00", currencyFormatTotalSalesRevenueAll);
		
		String currencyFormatCostsEstimateAll = dataUtils.getCurrencyFormatCostsEstimate(book);
		assertEquals("£17349.50", currencyFormatCostsEstimateAll);
		
		String currencyFormatProfitAll = dataUtils.getCurrencyFormatProfit(book);
		assertEquals("£31846.75", currencyFormatProfitAll);
		
		int totalUnitSalesCat = dataUtils.getTotalUnitSales(book, salesChannel);
		assertEquals(5476, totalUnitSalesCat);
		
		String currencyFormatTotalPurchaseCostCat = dataUtils.getCurrencyFormatTotalPurchaseCost(book, salesChannel);
		assertEquals("£15059.00", currencyFormatTotalPurchaseCostCat);
		
		String currencyFormatTotalSalesRevenueCat = dataUtils.getCurrencyFormatTotalSalesRevenue(book, salesChannel);
		assertEquals("£27380.00", currencyFormatTotalSalesRevenueCat);
		
		String currencyFormatCostsEstimateCat = dataUtils.getCurrencyFormatCostsEstimate(book, salesChannel);
		assertEquals("£4107.00", currencyFormatCostsEstimateCat);
		
		String currencyFormatProfitCat = dataUtils.getCurrencyFormatProfit(book, salesChannel);
		assertEquals("£8214.00", currencyFormatProfitCat);
	}
}
