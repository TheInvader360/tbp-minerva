package controllers;

import play.*;
import play.modules.search.*;
import play.mvc.*;
import utils.DataUtils;

import java.util.*;
import models.*;

public class Application extends Controller {

	public static void index() {
		renderTemplate("Application/search.html");
	}

	public static void listAllBooks() {
		List<Book> books = Book.find("order by sku asc").fetch();
		renderTemplate("Application/bookList.html", books);
	}
	
	public static void search(String searchQuery) {
		// create an empty list
		List<Book> books = new ArrayList<Book>();
		// if a search query has been keyed, search for matching books and populate books list
		if (!searchQuery.isEmpty()) {
			Query query = Search.search("title:"+searchQuery+" OR author:"+searchQuery+" OR isbn:"+searchQuery+" OR sku:"+searchQuery, Book.class);
			books = query.fetch();
		}
		// render books list
		renderTemplate("Application/bookList.html", books);
	}
	
	public static void listByAuthor(String author) {
		List<Book> books = Book.find("author = ? order by sku asc", author).fetch();
		renderTemplate("Application/bookList.html", books);
	}

	public static void showBookDetail(String isbn, String tag) {
		// there will only be one match (unique isbn per book) so fetching first of one is fine
		Book book = Book.find("isbn = ?", isbn).first();
		
		// if sales channel is null, the template handles it, so don't worry - be happy :)
		SalesChannel selectedSalesChannel = SalesChannel.find("tag = ?", tag).first();

		List<SalesChannel> allSalesChannels = SalesChannel.find("order by tag asc").fetch();
		
		// make data utils available to template
		renderArgs.put("DataUtils", new DataUtils());		
		renderTemplate("Application/bookDetail.html", book, selectedSalesChannel, allSalesChannels);
	}
}
