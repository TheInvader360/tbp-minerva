import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {
	
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
