/**
 * This class is generated by jOOQ
 */
package demo.dom.tables.daos;


import demo.dom.tables.TDatabasechangeloglock;
import demo.dom.tables.pojos.Databasechangeloglock;
import demo.dom.tables.records.RDatabasechangeloglockRecord;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DatabasechangeloglockDao extends DAOImpl<demo.dom.tables.records.RDatabasechangeloglockRecord, demo.dom.tables.pojos.Databasechangeloglock, Integer> {

	/**
	 * Create a new DatabasechangeloglockDao without any configuration
	 */
	public DatabasechangeloglockDao() {
		super(demo.dom.tables.TDatabasechangeloglock.DATABASECHANGELOGLOCK, demo.dom.tables.pojos.Databasechangeloglock.class);
	}

	/**
	 * Create a new DatabasechangeloglockDao with an attached configuration
	 */
	public DatabasechangeloglockDao(Configuration configuration) {
		super(demo.dom.tables.TDatabasechangeloglock.DATABASECHANGELOGLOCK, demo.dom.tables.pojos.Databasechangeloglock.class, configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Integer getId(demo.dom.tables.pojos.Databasechangeloglock object) {
		return object.getId();
	}

	/**
	 * Fetch records that have <code>ID IN (values)</code>
	 */
	public List<demo.dom.tables.pojos.Databasechangeloglock> fetchByTId(Integer... values) {
		return fetch(demo.dom.tables.TDatabasechangeloglock.DATABASECHANGELOGLOCK.ID, values);
	}

	/**
	 * Fetch a unique record that has <code>ID = value</code>
	 */
	public demo.dom.tables.pojos.Databasechangeloglock fetchOneByTId(Integer value) {
		return fetchOne(demo.dom.tables.TDatabasechangeloglock.DATABASECHANGELOGLOCK.ID, value);
	}

	/**
	 * Fetch records that have <code>LOCKED IN (values)</code>
	 */
	public List<demo.dom.tables.pojos.Databasechangeloglock> fetchByTLocked(Boolean... values) {
		return fetch(demo.dom.tables.TDatabasechangeloglock.DATABASECHANGELOGLOCK.LOCKED, values);
	}

	/**
	 * Fetch records that have <code>LOCKGRANTED IN (values)</code>
	 */
	public List<demo.dom.tables.pojos.Databasechangeloglock> fetchByTLockgranted(Timestamp... values) {
		return fetch(demo.dom.tables.TDatabasechangeloglock.DATABASECHANGELOGLOCK.LOCKGRANTED, values);
	}

	/**
	 * Fetch records that have <code>LOCKEDBY IN (values)</code>
	 */
	public List<demo.dom.tables.pojos.Databasechangeloglock> fetchByTLockedby(String... values) {
		return fetch(demo.dom.tables.TDatabasechangeloglock.DATABASECHANGELOGLOCK.LOCKEDBY, values);
	}
}