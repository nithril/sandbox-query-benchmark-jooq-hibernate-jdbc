package demo.domain;

import java.util.Collections;
import java.util.List;

import demo.dom.tables.pojos.Author;
import demo.dom.tables.pojos.Book;

/**
 * Created by nlabrot on 30/09/15.
 */
public class AuthorWithBooks {

	private Author author;
	private List<Book> books = Collections.emptyList();

	public AuthorWithBooks() {
	}

	public AuthorWithBooks(Author author, List<Book> books) {
		this.author = author;
		this.books = books;
	}

	private AuthorWithBooks(Builder builder) {
		setAuthor(builder.author);
		setBooks(builder.books);
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static Builder newBuilder(AuthorWithBooks copy) {
		Builder builder = new Builder();
		builder.author = copy.author;
		builder.books = copy.books;
		return builder;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}


	public static final class Builder {
		private Author author;
		private List<Book> books;

		private Builder() {
		}

		public Builder author(Author val) {
			author = val;
			return this;
		}

		public Builder books(List<Book> val) {
			books = val;
			return this;
		}

		public AuthorWithBooks build() {
			return new AuthorWithBooks(this);
		}
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AuthorWithBooks that = (AuthorWithBooks) o;

		if (!getAuthor().equals(that.getAuthor())) return false;
		return getBooks().equals(that.getBooks());

	}

	@Override
	public int hashCode() {
		int result = getAuthor().hashCode();
		result = 31 * result + getBooks().hashCode();
		return result;
	}
}
