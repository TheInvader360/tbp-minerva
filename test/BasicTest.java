import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {
	
	@Test
	public void createAndRetrieveBook() {
		// Tear down any existing data to start with a clean sheet
		Fixtures.deleteDatabase();

		// Create and save a book
		new Book("True Blood Collection", "Charlaine Harris", "9781407238876", "TBLD", 2.75, 5.00).save();
		
		// Retrieve the book with sku TBLD
    	Book book = Book.find("bySku", "TBLD").first();
    	
    	// Test
    	assertNotNull(book);
    	assertEquals("True Blood Collection", book.title);
    }
	
	@Test
	public void bookSalesHistory() {
		// Tear down any existing data to start with a clean sheet
		Fixtures.deleteDatabase();

		// Create a new Book and save it
	    Book book = new Book("True Blood Collection", "Charlaine Harris", "9781407238876", "TBLD", 2.75, 5.00).save();
	 
	    // Create some SalesHistory
	    new SalesSummary(book, new Date(), 14, "cat").save();
	    new SalesSummary(book, new Date(), 16, "cat").save();
	    new SalesSummary(book, new Date(), 10, "cat").save();
	    
	    // Retrieve all SalesHistory
	    List<SalesSummary> bookSalesHistory = SalesSummary.find("byBook", book).fetch();
	 
	    // Tests
	    assertEquals(3, bookSalesHistory.size());
	 
	    SalesSummary first = bookSalesHistory.get(0);
	    assertNotNull(first);
	    assertEquals(14, first.salesQuantity);
	    assertNotNull(first.summaryDate);
	 
	    SalesSummary second = bookSalesHistory.get(1);
	    assertNotNull(second);
	    assertEquals(16, second.salesQuantity);
	    assertNotNull(second.summaryDate);

	    SalesSummary third = bookSalesHistory.get(2);
	    assertNotNull(third);
	    assertEquals(10, third.salesQuantity);
	    assertNotNull(third.summaryDate);
	}
	
	@Test
	public void useTheSalesHistoryRelation() {
		// Tear down any existing data to start with a clean sheet
		Fixtures.deleteDatabase();

		// Create a new user and save it
	    Book book = new Book("True Blood Collection", "Charlaine Harris", "9781407238876", "TBLD", 2.75, 5.00).save();
	 
	    // Create some SalesHistory
	    book.addSalesHistory(new Date(), 14, "cat");
	    book.addSalesHistory(new Date(), 16, "cat");
	    book.addSalesHistory(new Date(), 10, "cat");
	    
	    // Count things
	    assertEquals(1, Book.count());
	    assertEquals(3, SalesSummary.count());
	    
	    // Navigate to SalesHistory
	    assertEquals(3, book.salesSummaries.size());
	    assertEquals(14, book.salesSummaries.get(0).salesQuantity);
	    
	    // Delete the book
	    book.delete();
	    
	    // Check that the book and all its sales history has been deleted
	    assertEquals(0, Book.count());
	    assertEquals(0, SalesSummary.count());
	}
	
	@Test
	public void fullTest() {
		// Tear down any existing data to start with a clean sheet
		Fixtures.deleteDatabase();
		
		// Load in the yaml canned data
		Fixtures.loadModels("data.yml");
		
		// Count all books, there should be four...
		assertEquals(4, Book.count());
		
		// Find and count all Jamie Oliver books, there should be two...
		List<Book> jamieBooks = Book.find("author", "Jamie Oliver").fetch();
		assertEquals(2, jamieBooks.size());
		
		// Find the True Blood Collection, verify that it has five SalesHistory rows...
		Book book = Book.find("sku", "TBLD").first();
		assertEquals("True Blood Collection", book.title);
		assertEquals(5, book.salesSummaries.size());
	}
}
