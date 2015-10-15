package demo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by nlabrot on 30/09/15.
 */
@Entity
@Table(name = "BOOK")
public class Book {
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@Column(name = "TITLE" , nullable = false)
	private String title;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
