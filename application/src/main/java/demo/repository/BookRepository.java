package demo.repository;

import org.jooq.Configuration;

import demo.dom.tables.daos.BookDao;

/**
 * Created by nlabrot on 03/10/15.
 */
public class BookRepository extends BookDao {

	public BookRepository(Configuration configuration) {
		super(configuration);
	}
}
