package com.galere.pictures.services;

import java.util.List;

import com.galere.pictures.entities.Category;
import com.galere.pictures.repositories.CategoryRepository;

/**
 * <b>
 * 	Interface du service permettant de gérer les catégories stoqués en base de données.
 * </b>
 * 
 * @see CategoryRepository
 * @see Category
 * 
 * @author Ilias HATTANE
 * @version 1.0
 *
 */
public interface ICategoryService {
	
	/**
	 * <b> Récupérer le repository CategoryRepository, l'accès à la gestion des éléments en base de données. </b>
	 * 
	 * @return Le repository.
	 * 
	 * @see CategoryRepository
	 * @see Category
	 */
	public CategoryRepository getRepository();
	
	/**
	 * <b> Vérifier si une catégorie existe grâce à son libellé. </b>
	 * 
	 * @param label Le libellé recherché.
	 * 
	 * @return Un boolean : true si elle existe, false sinon.
	 */
	public boolean existsCategory(String label);
	
	/**
	 * <b> Permet de rechercher, parmis les catégories sotquées en base de données, celles qui correspondent aux mots clés. </b>
	 * 
	 * <p> La recherche est effectuée par rapport au libellé et à l'id. </p>
	 * 
	 * @param tags Les mots clés en lien avec la recherche, séparés par un espace.
	 * 
	 * @return La liste des catégories en lien avec les mots clés.
	 * 
	 * @see Category
	 */
	public List<Category> searchByTags(String tags);
	
}
