package demo;

import java.io.File;
import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.jooq.DSLContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.domain.AuthorWithBooks;
import demo.service.AuthorQueries;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Commit
@Transactional
public class QueryTests {

	@Autowired
	private AuthorQueries authorQueries;


	@Test
	public void testQueries() throws Exception {
		Collection<AuthorWithBooks> authorsWithBooksGroupBy = sort(authorQueries.findAuthorsWithBooksJooqOldFashionGroupBy());
		Collection<AuthorWithBooks> authorsWithBooksJdbc = sort(authorQueries.findAuthorsWithBooksJdbc());
		Collection<AuthorWithBooks> authorsWithBooksJooqGroupBy = sort(authorQueries.findAuthorsWithBooksJooqIntoGroup());
		Collection<AuthorWithBooks> authorsWithBooksLazyGroupBy = sort(authorQueries.findAuthorsWithBooksJooqFetchLazyOldFashionGroupBy());
		Collection<AuthorWithBooks> authorsWithBooksStreamedGroupBy = sort(authorQueries.findAuthorsWithBooksJooqStreamedGroupBy());

		Assert.assertTrue(authorsWithBooksGroupBy.containsAll(authorsWithBooksJdbc));
		Assert.assertTrue(authorsWithBooksGroupBy.containsAll(authorsWithBooksJooqGroupBy));
		Assert.assertTrue(authorsWithBooksGroupBy.containsAll(authorsWithBooksLazyGroupBy));
		Assert.assertTrue(authorsWithBooksGroupBy.containsAll(authorsWithBooksStreamedGroupBy));

		ObjectMapper objectMapper = new ObjectMapper();
		String actual = objectMapper.writeValueAsString(authorsWithBooksGroupBy);
		String expected = FileUtils.readFileToString(new File("../db/src/test/resources/expected.json"));
		Assert.assertEquals(expected , actual);
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
