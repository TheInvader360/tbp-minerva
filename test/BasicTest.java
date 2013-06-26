import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {
	
	@Test
	public void createAndRetrieveBook() {
		// Create and save a book
		new Book("True Blood Collection", "Charlaine Harris", "9781407238876", "TBLD", 2.75, 5.00).save();
		
		// Retrieve the book with sku TBLD
    	Book book = Book.find("bySku", "TBLD").first();
    	
    	// Test
    	assertNotNull(book);
    	assertEquals("True Blood Collection", book.title);
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
	}
}
