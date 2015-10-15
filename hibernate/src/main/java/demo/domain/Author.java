package demo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by nlabrot on 30/09/15.
 */
@Entity
@Table(name = "AUTHOR")
@NamedEntityGraphs(@NamedEntityGraph(name = "author.graph.author-with-books", attributeNodes = @NamedAttributeNode("books")))
@NamedQueries(
		@NamedQuery(name = "Author.findAllWithBooks" , query = "FROM Author a LEFT JOIN FETCH a.books")
)
public class Author {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@OneToMany
	@JoinColumn(name = "AUTHOR_ID")
	@JsonIgnore
	private List<Book> books;

	@Column(name = "NAME" , nullable = false)
	private String name;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Author author = (Author) o;

		return getId().equals(author.getId());

	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}
}
