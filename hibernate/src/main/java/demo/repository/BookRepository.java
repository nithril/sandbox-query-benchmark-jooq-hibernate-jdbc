package demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.domain.Book;

/**
 * Created by nlabrot on 01/10/15.
 */
public interface BookRepository extends JpaRepository<Book, Long> {
}
