package com.galere.pictures.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * <b>
 * 	Symbolise une image.
 * </b>
 * <p> Une image est décrite par : </p>
 * <ul>
 *   <li> Son ID </li>
 *   <li> Son titre </li>
 *   <li> Sa description </li>
 *   <li> Ses tags </li>
 *   <li> Sa date d'ajout </li>
 *   <li> Son chemin d'accès sur la machine </li>
 *   <li> La description de son contenu </li>
 *  </ul>
 * <p>
 *	Toute image contient la liste de ses catégories.
 * </p>
 * 
 * @author Ilias HATTANE
 * @version 1.0
 *
 */
@Entity
@Table(name = "image")
public class Image {
	
	/**
	 * <b> L'id de l'image. </b>
	 * 
	 * @see Image#getId()
	 * @see Image#setId(Long)
	 */
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * <b> Checmin d'accès à l'image sur la machine. </b>
	 * 
	 * @see Image#getUrl()
	 * @see Image#setUrl(String)
	 */
	@Column(name = "Url")
	@NotNull
	private String url;
	
	/**
	 * <b> Titre décrivant l'image. </b>
	 * 
	 * <p> Ne dépasse pas 100 carractères. </p>
	 * 
	 * @see Image#getTitle()
	 * @see Image#setTitle(String)
	 */
	@Column(name = "Title")
	@Size(max = 100)
	@NotNull
	private String title;
	
	/**
	 * <b> Description de l'image. </b>
	 * 
	 * @see Image#getDescription()
	 * @see Image#setDescription(String)
	 */
	@Column(name = "Description")
	@NotNull
	private String description;

	/**
	 * <b> Liste des mots clés décrivant l'image (séparés par un espace). </b>
	 * 
	 * @see Image#getTags()
	 * @see Image#setTags(String)
	 */
	@Column(name = "Tags")
	@NotNull
	private String tags;
	
	/**
	 * <b> Contenu de l'image. </b>
	 * 
	 * <p> Lors de l'ajout d'une image, cette dernière est analysée pour extraire certains objets présents dessus </p>
	 * 
	 * @see Image#getContent()
	 * @see Image#setContent(String)
	 */
	@Column(name = "Content")
	private String content;
	
	/**
	 * <b> Date d'ajout de l'image </b>
	 * 
	 * @see Image#getDate()
	 * @see Image#setDate(LocalDate)
	 */
	@Column(name = "Date")
	@NotNull
	private LocalDate date;
	
	/**
	 * <b> Liste des catégories de l'image. </b>
	 * 
	 * @see Image#getCategories()
	 * @see Image#setCategories(List)
	 */
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "image_category", 
      joinColumns = @JoinColumn(name = "IdImage", referencedColumnName = "Id"), 
      inverseJoinColumns = @JoinColumn(name = "IdCategory", referencedColumnName = "Id"))
	private List<Category> categories;
	
	/**
	 * <b> Récupérer les catégories de l'image, sous forme d'une chaine de carractères au format HTML. </b>
	 * 
	 * <p> Le résultat sera l'affichage d'une catégorie par ligne. </p>
	 * 
	 * @return La liste des catégories sous forme d'une chaine de carractères au format HTML.
	 */
	public String getStringCategories() {
		String to = "";
		if (categories != null && !categories.isEmpty()) {
			for (Category cat : categories)
				to += cat.getLabel() + " <br/> ";
		}
		return to;
	}
	
	/**
	 * <b> Récupérer les catégories de l'image, sous forme d'une chaine de carractères au format HTML. </b>
	 * 
	 * <p> Le résultat sera l'affichage d'une catégorie par ligne. </p>
	 * 
	 * @return La liste des catégories sous forme d'une chaine de carractères au format HTML.
	 */
	public String getStringCategoriesOnly() {
		String to = "";
		if (categories != null && !categories.isEmpty()) {
			for (Category cat : categories)
				to += cat.getLabel() + " ";
		}
		return to;
	}
	
	/**
	 * <b> Récupérer l'id de l'image. </b>
	 * 
	 * @return L'id de l'image.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * <b> Définir le nouvel id de l'image. </b>
	 * 
	 * @param id Le nouvel id.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * <b> Récupérer l'url d'accès à l'image sur la machine. </b>
	 * 
	 * @return Le chemin d'accès à l'image.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * <b> Définir le nouveau chemin d'accès à l'image. </b>
	 * 
	 * @param url Le nouveau chemin d'accès.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * <b> Récupérer le titre de l'image. </b>
	 * 
	 * @return Le titre de l'image.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * <b> Définir le nouveau titre de l'image. </b>
	 * 
	 * @param title Le nouveau titre.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * <b> Récupérer la description de l'image. </b>
	 * 
	 * @return La description de l'image.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <b> Définir la nouvelles description de l'image. </b>
	 * 
	 * @param description La nouvelle description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * <b> Récupérer les tags de l'image. </b>
	 * 
	 * @return Les tags de l'image séparés par un espace.
	 */
	public String getTags() {
		return tags;
	}

	/**
	 * <b> Définir les nouveaux tags de l'image. </b>
	 * 
	 * @param tags Les nouveaux tags.
	 */
	public void setTags(String tags) {
		this.tags = tags;
	}

	/**
	 * <b> Récupérer le contenu de l'image. </b>
	 * 
	 * @return Le contneu de l'image.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * <b> Définir le nouveau contenu de l'image. </b>
	 * 
	 * @param content Le nouveau contneu de l'image.
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * <b> Récupérer la date d'ajout de l'image. </b>
	 * 
	 * @return La date d'ajout.
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * <b> Définir la date d'ajout de l'image. </b>
	 * 
	 * @param date La date d'ajout.
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}

	/**
	 * <b> Récupérer la liste des catégories de l'image. </b>
	 * 
	 * @return La liste des catégories de l'image.
	 */
	public List<Category> getCategories() {
		return categories;
	}
	/**
	 * <b> Définir la nouvelle liste des catégories de l'image. </b>
	 * 
	 * @param categories La nouvelle liste de catégories.
	 */
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	
}
