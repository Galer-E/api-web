package com.galere.pictures.services;

import java.util.List;

import com.galere.pictures.entities.Role;
import com.galere.pictures.repositories.RoleRepository;

/**
 * <b>
 * 	Interface du service permettant de gérer les rôles stoqués en base de données.
 * </b>
 * 
 * @see RoleRepository
 * @see Role
 * 
 * @author Ilias HATTANE
 * @version 1.0
 *
 */
public interface IRoleService {
	
	/**
	 * <b> Récupérer une instance du repository RoleRepository, et l'accès à la gestion des rôles stoqués en base de données. </b>
	 * 
	 * @return L'instance du repository.
	 * 
	 * @see RoleRepository
	 */
	public RoleRepository getRepository();
	
	/**
	 * <b> Permet de vérifier si un rôle existe à partir de son libellé. </b>
	 * 
	 * @param label Le libellé du rôle recherché.
	 * @return Un boolean : true si le rôle existe, false sinon.
	 */
	public boolean existsRole(String label);
	
	/**
	 * <b> Rechercher les rôles en lien avec les mots clés passés en paramètre. </b>
	 * 
	 * <p> La recherche est effectuée par rapport aux champs : </p>
	 * 	<ul>
	 * 		<li> Libellé </li>
	 * 		<li> Liste des utilisateurs qui ont ce rôle </li>
	 * 		<li> Id </li>
	 * 	</ul>
	 * 
	 * @param tags Les mots clés en lien avec la recherche, séparés par un espace.
	 * @return La liste des rôles qui correspondent aux mots clés.
	 * 
	 * @see Role
	 */
	public List<Role> searchByTags(String tags);
	
}
