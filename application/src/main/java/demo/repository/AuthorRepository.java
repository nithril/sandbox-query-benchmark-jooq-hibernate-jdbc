package demo.repository;

import org.jooq.Configuration;

import demo.dom.tables.daos.AuthorDao;

/**
 * Created by nlabrot on 03/10/15.
 */
public class AuthorRepository extends AuthorDao {

	public AuthorRepository(Configuration configuration) {
		super(configuration);
	}

}
