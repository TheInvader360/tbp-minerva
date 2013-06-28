package controllers;

import play.*;
import play.mvc.*;
import java.util.*;
import models.*;

public class Application extends Controller {

	public static void index() {
		List<Book> allBooks = Book.find("order by sku asc").fetch();
		render(allBooks);
	}
	
	public static void showBookDetail(String isbn) {
		// isbn is unique (one per product) - fetching the 'first of one' is safe
		Book book = Book.find("isbn = ?", isbn).first();
		render(book);
	}
}
