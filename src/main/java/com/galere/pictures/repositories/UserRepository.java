package com.galere.pictures.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.galere.pictures.entities.User;

/**
 * <b>
 * 	Interface du repository permettant de gérer les utilisateurs stoqués en base de données. Implémente la class JpaRepository.
 * </b>
 * 
 * @see JpaRepository
 * @see User
 * 
 * @author Ilias HATTANE
 * @version 1.0
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	/**
	 * <b> Rechercher la liste des utilisateurs dont le nom d'utilisateur contient la chaine de carractères pasée en paramètre. </b>
	 * 
	 * @param loginContains La chaine que doit contenir le nom d'utilisateur recherché. 
	 * 
	 * @return La liste des utilisateurs touvés à partir de la chaine passée en paamètre.
	 */
	List<User> findAllByLoginContains(String loginContains);
	
}
