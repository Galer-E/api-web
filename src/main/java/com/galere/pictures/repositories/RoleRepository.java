package com.galere.pictures.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.galere.pictures.entities.Role;

/**
 * <b>
 * 	Interface du repository permettant de gérer les rôles stoqués en base de données. Implémente la class JpaRepository.
 * </b>
 * 
 * @see JpaRepository
 * @see Role
 * 
 * @author Ilias HATTANE
 * @version 1.0
 *
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
		
}
