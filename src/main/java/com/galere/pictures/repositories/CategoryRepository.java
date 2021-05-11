package com.galere.pictures.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.galere.pictures.entities.Category;

/**
 * <b>
 * 	Interface du repository permettant de gérer les catégories stoqués en base de données. Implémente la class JpaRepository.
 * </b>
 * 
 * @see JpaRepository
 * @see Category
 * 
 * @author Ilias HATTANE
 * @version 1.0
 *
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
		
}
