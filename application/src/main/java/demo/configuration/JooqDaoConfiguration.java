package demo.configuration;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import demo.repository.AuthorRepository;
import demo.repository.BookRepository;


/**
 * Created by nlabrot on 28/09/15.
 */
@Configuration
public class JooqDaoConfiguration {

	@Autowired
	private DSLContext dslContext;

	@Bean
	public AuthorRepository authorRepository() {
		return new AuthorRepository(dslContext.configuration());
	}

	@Bean
	public BookRepository bookRepository() {
		return new BookRepository(dslContext.configuration());
	}

}
