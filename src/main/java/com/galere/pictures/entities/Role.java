package com.galere.pictures.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <b>
 * 	Symbolise un rôle. Les rôles sont appliqués aux utilisateurs.
 * </b>
 * <p> Un rôle est décrit par : </p>
 * <ul>
 *   <li> Son ID </li>
 *   <li> Son libellé </li>
 *   <li> Son niveau d'accès </li>
 *  </ul>
 * <p>
 *	Tout rôle contient la liste des utilisateurs qui ont ce rôle d'appliqué.
 * </p>
 * 
 * @author Ilias HATTANE
 * @version 1.0
 *
 */
@Entity
@Table(name = "role")
public class Role {
	
	/**
	 * <b> L'id du rôle. </b>
	 * 
	 * @see Role#getId()
	 * @see Role#setId(Long)
	 */
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	/**
	 * <b> Le libellé du rôle. </b>
	 * 
	 * @see Role#getLabel()
	 * @see Role#setLabel(String)
	 */
	@Column(name = "Label")
	@Size(max = 35)
	@NotNull
	private String label;
	
	/**
	 * <b> Niveau d'accès du rôle. </b>
	 * 
	 * @see Role#getLevel()
	 * @see Role#setLevel(Short)
	 */
	@Column(name = "Level")
	@NotNull
	private Short level;
	
	/**
	 * <b> La liste des utilisateurs qui ont ce rôle d'appliqué. </b>
	 * 
	 * @see Role#getUsersToString()
	 */
	@ManyToMany(mappedBy = "roles")
    private List<User> users;
	
	/**
	 * <b> Réupérer la liste des utilisateurs qui ont ce rôle d'appliqué, sous forme d'une chaine de carractères. </b>
	 * 
	 * <p> Chaque nom d'utilisateur est séparé par un tiret. </p>
	 * 
	 * @return La liste des utilisateur sous forme d'une chaine de carractères formattée.
	 */
	public String getUsersToString() {
		String to = "- ";
		
		if (users != null && !users.isEmpty()) {
			for (User u : users)
				to += u.getLogin() + " - ";
		}
		return to;
	}
	
	/**
	 * <b> Récupérer l'id du rôle. </b>
	 * 
	 * @return L'id du rôle.
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * <b> Définir le nouvel id du rôle. </b>
	 *
	 * @param id Le nouvel id.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * <b> Récupérer le libellé du rôle. </b>
	 * 
	 * @return Le libellé du rôle.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * <b> Définir le nouveau libellé du rôle. </b>
	 * 
	 * @param label Le nouveau libellé.
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	/**
	 * <b> Récupérer le niveau d'accès du rôle. </b>
	 * 
	 * @return Le niveau d'accès du rôle.
	 */
	public Short getLevel() {
		return level;
	}

	/**
	 * <b> Définir le nouveau niveau d'accès du rôle. </b>
	 * 
	 * @param level Le nouveau niveau d'accès.
	 */
	public void setLevel(Short level) {
		this.level = level;
	}

	/**
	 * <b> Récupérer une description du rôle. </b>
	 * 
	 * @return La description.
	 */
	@Override
	public String toString() {
		return "[" + getId() + "] " + getLabel();
	}
	
}
