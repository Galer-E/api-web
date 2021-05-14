package com.galere.pictures.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.galere.pictures.entities.Image;

/**
 * <b>
 * 	Interface du repository permettant de gérer les images stoqués en base de données. Implémente la class JpaRepository.
 * </b>
 * 
 * @see JpaRepository
 * @see Image
 * 
 * @author Ilias HATTANE
 * @version 1.0
 *
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
		
}
