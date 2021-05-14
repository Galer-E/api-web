package com.galere.pictures.entities;

import java.time.LocalDate;
import java.util.Set;

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
 * 	Symbolise un utilisateur.
 * </b>
 * <p> Un utilisateur est décrit par : </p>
 * <ul>
 *   <li> Son ID </li>
 *   <li> Son nom d'utilisateur </li>
 *   <li> Son mot de passe (chiffré) </li>
 *   <li> Sa date d'inscription </li>
 *   <li> La liste de ses rôles </li>
 *  </ul>
 * <p>
 *	Tout utilisateur a accès à la liste de ses rôles.
 * </p>
 * 
 * @author Ilias HATTANE
 * @version 1.0
 *
 */
@Entity
@Table(name = "user")
public class User {
	
	/**
	 * <b> L'id de l'utilisateur. </b>
	 * 
	 * @see User#getId()
	 * @see User#setId(Long)
	 */
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * <b> Le pseudo de l'utilisateur. </b>
	 * 
	 * @see User#getLogin()
	 * @see User#setLogin(String)
	 */
	@Column(name = "Login")
	@Size(max = 50)
	@NotNull
	private String login;
	
	/**
	 * <b> Le mot de passe de l'utilisateur. </b>
	 * 
	 * @see User#getPass()
	 * @see User#setPass(String)
	 */
	@Column(name = "Pass")
	@NotNull
	private String pass;

	/**
	 * <b> La date d'inscription de l'utilisateur. </b>
	 * 
	 * @see User#getEntry()
	 * @see User#setEntry(LocalDate)
	 */
	@Column(name = "Entry")
	private LocalDate entry;
	
	/**
	 * <b> Liste des rôles de l'utilisateur. </b>
	 * 
	 * @see User#getRoles()
	 * @see User#setRoles(Set)
	 */
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", 
      joinColumns = @JoinColumn(name = "IdUser", referencedColumnName = "Id"), 
      inverseJoinColumns = @JoinColumn(name = "IdRole", referencedColumnName = "Id"))
	@NotNull
	private Set<Role> roles;
	
	/**
	 * <b> Remplace le contenu de la variable 'mot de passe' par une chaine de carractère masquée, constante. </b>
	 * 
	 * <p> Utilisé pour rendre le mot de passe illisible dans la mémoire vive. </p>
	 */
	public void hidePass() {
		setPass("******");
	}
	
	/**
	 * <b> Récupérer l'id de l'utilisateur. </b>
	 * 
	 * @return L'id de l'utilisateur.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * <b> Définir le nouvel id de l'utilisateur. </b>
	 * 
	 * @param id Le nouvel id.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * <b> Récupérer la liste des rôles de l'utilisateur. </b>
	 * 
	 * @return La liste des rôles.
	 */
	public Set<Role> getRoles() {
		return roles;
	}
	
	/**
	 * <b> Récupérer la liste des rôles de l'utilisateur sous forme d'une chaine de carractères au format HTML. </b>
	 * 
	 * <p> Formatté de facon à n'avoir qu'un rôle par ligne. </p>
	 * 
	 * @return La liste des rôles.
	 */
	public String getStringRoles() {
		String stringRoles = "";
		for (Role r : this.roles)
			stringRoles += r.getLabel() + "<br/>";
		return stringRoles;
	}

	/**
	 * <b> Définir la nouvelle liste de rôles de l'utilisateur. </b>
	 * 
	 * @param roles La nouvelle liste de rôles.
	 */
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	/**
	 * <b> Récupérer le nom de l'utilisateur. </b>
	 * 
	 * @return Le nom de l'utilisateur.
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * <b> Définir le nom de l'utilisateur. </b>
	 * 
	 * @param login Le nouveau nom de l'utilisateur.
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * <b> Récupérer le mot de passe chiffré de l'utilisateur. </b>
	 * 
	 * @return Le mot de passe chiffré l'utilisateur.
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * <b> Définir le nouveau mot de passe chiffré de l'utilisateur. </b>
	 * 
	 * @param pass Le nouveau mot de passe chiffré.
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	/**
	 * <b> Récupérer une description de l'utilisateur. </b>
	 * 
	 * @return La description.
	 */
	@Override
	public String toString() {
		return "[" + getId() + "] " + getLogin() + " : " + getRoles().toString();
	}

	/**
	 * <b> Récupérer la date d'inscription de l'utilisateur. </b>
	 * 
	 * @return La date d'inscription de l'utilisateur.
	 */
	public LocalDate getEntry() {
		return entry;
	}

	/**
	 * <b> Définir la date d'inscription de l'utilisateur. </b>
	 * 
	 * @param entry La date d'inscription de l'utilisateur.
	 */
	public void setEntry(LocalDate entry) {
		this.entry = entry;
	}

	/**
	 * <b> Récupérer le niveau d'accès le plus haut de l'utilisateur. </b>
	 * 
	 * @return Le plus haut niveau d'accès de l'utilisateur.
	 */
	public Role getHeightRole() {
		
		Role role = null;
		if (roles != null && !roles.isEmpty()) {
			for (Role r : roles) {
				if (role != null && r.getLevel() > role.getLevel())
					role = r;
				else if (role == null)
					role = r;
			}
		}
		
		return role;
		
	}
	
}
