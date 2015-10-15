package demo.service;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.domain.Author;
import demo.domain.AuthorWithBooks;
import demo.domain.Book;
import demo.repository.AuthorRepository;
import demo.repository.BookRepository;


/**
 * Created by nlabrot on 30/09/15.
 */
@Service
public class AuthorQueries {

	@Autowired
	private AuthorRepository authorRepository;

	@PersistenceContext
	private EntityManager entityManager;


	@Transactional(readOnly = true)
	public List<AuthorWithBooks> findAuthorsWithBooksUsingSpringData() {
		return toAuthor(authorRepository.findAllWithBooks());
	}

	@Transactional(readOnly = true)
	public List<AuthorWithBooks> findAuthorsWithBooksUsingNamedQuery() {
		TypedQuery<Author> query = entityManager.createNamedQuery("Author.findAllWithBooks", Author.class);
		return toAuthor(query.getResultList());
	}

	@Transactional(readOnly = true)
	public List<AuthorWithBooks> findAuthorsWithBooksUsingCriteria() {
		Session session = (Session) entityManager.getDelegate();
		Criteria criteria = session.createCriteria(Author.class)
				.setFetchMode("books", FetchMode.JOIN)
				.setReadOnly(true);

		return toAuthor(criteria.list());
	}

	private List<AuthorWithBooks> toAuthor(List<Author> authors) {
		return authors.stream()
				.distinct()
				.map(author -> new AuthorWithBooks(author, author.getBooks())).collect(Collectors.toList());
	}
}
