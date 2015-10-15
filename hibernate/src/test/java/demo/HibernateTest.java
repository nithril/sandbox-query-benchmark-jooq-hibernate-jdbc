package demo;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.io.FileUtils;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import antlr.collections.impl.IntRange;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.domain.Author;
import demo.domain.AuthorWithBooks;
import demo.domain.Book;
import demo.repository.AuthorRepository;
import demo.repository.BookRepository;
import demo.service.AuthorQueries;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Commit
@Transactional
public class HibernateTest {


	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private BookRepository bookRepository;


	@Autowired
	private AuthorQueries authorQueries;

	@Test
	public void testQueries() throws Exception {
		Collection<AuthorWithBooks> authorsWithBooksUsingCriteria = sort(authorQueries.findAuthorsWithBooksUsingCriteria());
		Collection<AuthorWithBooks> authorsWithBooksUsingNamedQuery = sort(authorQueries.findAuthorsWithBooksUsingNamedQuery());
		Collection<AuthorWithBooks> authorsWithBooksUsingSpringData = sort(authorQueries.findAuthorsWithBooksUsingSpringData());

		Assert.assertTrue(authorsWithBooksUsingCriteria.containsAll(authorsWithBooksUsingNamedQuery));
		Assert.assertTrue(authorsWithBooksUsingCriteria.containsAll(authorsWithBooksUsingSpringData));

		ObjectMapper objectMapper = new ObjectMapper();
		String actual = objectMapper.writeValueAsString(authorsWithBooksUsingCriteria);
		String expected = FileUtils.readFileToString(new File("../db/src/test/resources/expected.json"));
		Assert.assertEquals(expected , actual);
	}



	@Test
	public void testLoadData() throws Exception {

		bookRepository.deleteAll();
		authorRepository.deleteAll();

		for (int i = 0; i < 100; i++) {

			Author author = new Author();
			author.setName("AUTHOR_" + i);

			author.setBooks(IntStream.range(0 , ThreadLocalRandom.current().nextInt(1,10)).mapToObj(b -> {
				Book book = new Book();
				book.setTitle("TITLE_" + b);
				return bookRepository.save(book);
			}).collect(Collectors.toList()));

			author = authorRepository.save(author);
		}
	}



	private Collection<AuthorWithBooks> sort(Collection<AuthorWithBooks> authorWithBooks){
		return authorWithBooks.stream()
				.sorted((l, r) -> l.getAuthor().getId().compareTo(r.getAuthor().getId()))
				.map(ab -> {
					ab.getBooks().sort((l, r) -> l.getId().compareTo(r.getId()));
					return ab;
				})
				.collect(Collectors.toList());
	}

}
