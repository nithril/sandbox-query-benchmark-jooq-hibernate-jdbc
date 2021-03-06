package demo.service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.Result;
import org.jooq.TableField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.dom.tables.TAuthor;
import demo.dom.tables.TBook;
import demo.dom.tables.pojos.Author;
import demo.dom.tables.pojos.Book;
import demo.dom.tables.records.RAuthorRecord;
import demo.domain.AuthorWithBooks;
import demo.repository.AuthorRepository;
import demo.repository.BookRepository;

import static demo.dom.tables.TAuthor.AUTHOR;
import static demo.dom.tables.TBook.BOOK;

/**
 * Created by nlabrot on 30/09/15.
 */
@Service
public class AuthorQueries {

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private DSLContext dslContext;

	@Autowired
	private JdbcTemplate jdbcTemplate;


	@Transactional(readOnly = true)
	public Long reference() {
		Set<Long> set = new HashSet<>();
		AtomicLong aLong = new AtomicLong();
		jdbcTemplate.query("SELECT AUTHOR.*, BOOK.* FROM AUTHOR LEFT OUTER JOIN BOOK ON AUTHOR.ID = BOOK.AUTHOR_ID", r -> {
			Long authorId = r.getLong("AUTHOR.ID");
			if (!set.contains(authorId)) {
				set.add(authorId);
				aLong.addAndGet(r.getString("AUTHOR.NAME").length());
			}
			aLong.addAndGet(r.getLong("BOOK.ID"));
			aLong.addAndGet(r.getString("BOOK.TITLE").length());
		});
		return aLong.get();

	}

	@Transactional(readOnly = true)
	public Collection<AuthorWithBooks> findAuthorsWithBooksJdbc() {
		Map<Long, AuthorWithBooks> booksMap = new HashMap<>();
		jdbcTemplate.query("SELECT AUTHOR.*, BOOK.* FROM AUTHOR LEFT OUTER JOIN BOOK ON AUTHOR.ID = BOOK.AUTHOR_ID", r -> {
			Long authorId = r.getLong("AUTHOR.ID");
			AuthorWithBooks authorWithBooks = booksMap.get(authorId);
			if (authorWithBooks == null) {
				authorWithBooks = new AuthorWithBooks();
				authorWithBooks.setAuthor(new Author(authorId, r.getString("AUTHOR.NAME")));
				authorWithBooks.setBooks(new ArrayList<>());
				booksMap.put(authorId, authorWithBooks);
			}
			Book book = new Book(r.getLong("BOOK.ID"), r.getString("BOOK.TITLE"), authorId);
			authorWithBooks.getBooks().add(book);

		});
		return booksMap.values();
	}


	@Transactional(readOnly = true)
	public Collection<AuthorWithBooks> findAuthorsWithBooksJdbcStreamed() {

		Map<Object, List<Object[]>> collect = jdbcTemplate.query(connection -> {
			return connection.prepareStatement("SELECT AUTHOR.*, BOOK.* FROM AUTHOR LEFT OUTER JOIN BOOK ON AUTHOR.ID = BOOK.AUTHOR_ID");
		}, (r, i) -> {
			return new Object[]{r.getLong("AUTHOR.ID"),
					r.getString("AUTHOR.NAME"),
					r.getLong("BOOK.ID"),
					r.getString("BOOK.TITLE")};
		}).stream().collect(Collectors.groupingBy(r -> r[0]));

		return collect.entrySet().stream().map(e -> {
			AuthorWithBooks authorWithBooks = new AuthorWithBooks();
			authorWithBooks.setAuthor(new Author((Long)e.getValue().get(0)[0] , (String)e.getValue().get(0)[1]));
			List<Book> books = e.getValue().stream().map(r -> new Book((Long)r[2] , (String)r[3] , (Long)r[0])).collect(Collectors.toList());
			authorWithBooks.setBooks(books);
			return authorWithBooks;
		}).collect(Collectors.toList());
	}


	@Transactional(readOnly = true)
	public Collection<AuthorWithBooks> findAuthorsWithBooksJooqStreamedGroupBy() {
		Result<Record> records = dslContext.select()
				.from(AUTHOR.leftOuterJoin(BOOK).on(BOOK.AUTHOR_ID.equal(AUTHOR.ID)))
				.fetch();

		Map<Long, List<Record>> collect = records.stream().collect(Collectors.groupingBy(r -> r.getValue(TAuthor.AUTHOR.ID)));

		return collect.entrySet().stream().map(e -> {
			AuthorWithBooks authorWithBooks = new AuthorWithBooks();
			authorWithBooks.setAuthor(authorRepository.mapper().map(e.getValue().get(0).into(TAuthor.AUTHOR)));
			List<Book> books = e.getValue().stream().map(r -> bookRepository.mapper().map(r.into(TBook.BOOK))).collect(Collectors.toList());
			authorWithBooks.setBooks(books);
			return authorWithBooks;
		}).collect(Collectors.toList());
	}


	@Transactional(readOnly = true)
	public Collection<AuthorWithBooks> findAuthorsWithBooksJooqIntoGroup() {
		return dslContext.select()
				.from(AUTHOR.leftOuterJoin(BOOK).on(BOOK.AUTHOR_ID.equal(AUTHOR.ID)))
				.fetch().intoGroups(TAuthor.AUTHOR)
				.entrySet()
				.stream()
				.map(e -> {
					Author author = authorRepository.mapper().map(e.getKey());
					List<Book> stream = e.getValue().stream()
							.map(r -> bookRepository.mapper().map(r.into(TBook.BOOK))).collect(Collectors.toList());
					return new AuthorWithBooks(author, stream);
				}).collect(Collectors.toList());
	}


	@Transactional(readOnly = true)
	public Collection<AuthorWithBooks> findAuthorsWithBooksJooqOldFashionGroupBy() {

		Result<Record> records = dslContext.select()
				.from(AUTHOR.leftOuterJoin(BOOK).on(BOOK.AUTHOR_ID.equal(AUTHOR.ID)))
				.fetch();

		Map<Long, AuthorWithBooks> booksMap = new HashMap<>(records.size() / 4);

		records.stream()
				.forEach(r -> {
					Long authorId = r.getValue(TAuthor.AUTHOR.ID);

					AuthorWithBooks authorWithBooks = booksMap.get(authorId);

					if (authorWithBooks == null) {
						authorWithBooks = new AuthorWithBooks();
						authorWithBooks.setAuthor(new Author(authorId, r.getValue(TAuthor.AUTHOR.NAME)));
						authorWithBooks.setBooks(new ArrayList<>());
						booksMap.put(authorId, authorWithBooks);
					}

					Book book = new Book(r.getValue(TBook.BOOK.ID), r.getValue(TBook.BOOK.TITLE), authorId);
					authorWithBooks.getBooks().add(book);
				});
		return booksMap.values();
	}


	@Transactional(readOnly = true)
	public Collection<AuthorWithBooks> findAuthorsWithBooksJooqFetchLazyOldFashionGroupBy() {

		try (Cursor<Record> records = dslContext.select()
				.from(AUTHOR.leftOuterJoin(BOOK).on(BOOK.AUTHOR_ID.equal(AUTHOR.ID)))
				.fetchLazy()) {

			Map<Long, AuthorWithBooks> booksMap = new HashMap<>();

			for (Record r : records) {

				Long authorId = r.getValue(TAuthor.AUTHOR.ID);

				AuthorWithBooks authorWithBooks = booksMap.get(authorId);

				if (authorWithBooks == null) {
					authorWithBooks = new AuthorWithBooks();
					authorWithBooks.setAuthor(new Author(authorId, r.getValue(TAuthor.AUTHOR.NAME)));
					authorWithBooks.setBooks(new ArrayList<>());
					booksMap.put(authorId, authorWithBooks);
				}

				Book book = new Book(r.getValue(TBook.BOOK.ID), r.getValue(TBook.BOOK.TITLE), authorId);
				authorWithBooks.getBooks().add(book);
			}

			return booksMap.values();
		}
	}
}
