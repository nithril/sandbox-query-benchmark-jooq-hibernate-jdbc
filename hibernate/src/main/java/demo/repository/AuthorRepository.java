package demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import demo.domain.Author;

/**
 * Created by nlabrot on 01/10/15.
 */
public interface AuthorRepository extends JpaRepository<Author, Long> {
	@Query("FROM Author a LEFT JOIN FETCH a.books")
	List<Author> findAllWithBooks();
}
