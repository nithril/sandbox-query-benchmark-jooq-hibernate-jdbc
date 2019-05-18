package demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.domain.Author;
import demo.domain.AuthorWithBooks;
import demo.domain.Book;
import demo.repository.AuthorRepository;
import demo.repository.BookRepository;
import demo.service.AuthorQueries;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
//@SpringApplicationConfiguration(classes = Application.class)
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
			authorRepository.save(author);
		}
	}



	private Collection<AuthorWithBooks> sort(Collection<AuthorWithBooks> authorWithBooks){
		return authorWithBooks.stream()
				.sorted(Comparator.comparing(authorWithBooks1 -> authorWithBooks1.getAuthor().getId()))
				.peek(ab -> ab.getBooks().sort(Comparator.comparing(Book::getId)))
				.collect(Collectors.toList());
	}

}
