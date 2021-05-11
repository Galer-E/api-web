package com.galere.pictures.services;

import java.util.List;

import com.galere.pictures.entities.User;
import com.galere.pictures.repositories.UserRepository;

/**
 * <b>
 * 	Interface du service permettant de gérer les utilisateurs stoqués en base de données.
 * </b>
 * 
 * @see UserRepository
 * @see User
 * 
 * @author Ilias HATTANE
 * @version 1.0
 *
 */
public interface IUserService {
	
	/**
	 * <b> Récupérer une instance du repository UserRepository, et l'accès à la gestion des utilisateurs stoqués en base de données. </b>
	 * 
	 * @return L'instance du repository.
	 * 
	 * @see UserRepository
	 */
	public UserRepository getRepository();
	
	/**
	 * <b> Permet de vérifier si un utilisateur existe dans la base de données à partir de son nom d'utilisateur. </b>
	 * 
	 * @param login Le nom d'utilisateur recherché.
	 * @return Un boolean : true si l'utilisateur existe, false sinon.
	 */
	public boolean existsUser(String login);
	
	/**
	 * <b> Rechercher les utilisateurs en lien avec les mots clés passés en paramètre. </b>
	 * 
	 * <p> La recherche est effectuée par rapport aux champs : </p>
	 * 	<ul>
	 * 		<li> Nom d'utilisateur </li>
	 * 		<li> Rôle le plus haut </li>
	 * 		<li> Date d'inscription </li>
	 * 		<li> Id </li>
	 * 	</ul>
	 * 
	 * @param tags Les mots clés en lien avec la recherche, séparés par un espace.
	 * @return La liste des utilisateurs qui correspondent aux mots clés.
	 * 
	 * @see User
	 */
	public List<User> searchByTags(String tags);

}
