package com.galere.pictures.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user")
public class Image {
	
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "Url")
	@NotNull
	private String url;
	
	@Column(name = "Title")
	@Size(max = 100)
	@NotNull
	private String title;
	
	@Column(name = "Description")
	@NotNull
	private String description;

	@Column(name = "Tags")
	@NotNull
	private String tags;
	
	@Column(name = "Content")
	private String Content;
	
	@Column(name = "Date")
	@NotNull
	private LocalDate date;
	
	@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "image_category", 
      joinColumns = @JoinColumn(name = "IdImage", referencedColumnName = "Id"), 
      inverseJoinColumns = @JoinColumn(name = "IdCategory", referencedColumnName = "Id"))
	private List<Category> categories;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	
}
