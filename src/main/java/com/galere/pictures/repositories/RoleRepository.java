package com.galere.pictures.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.galere.pictures.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
		
}
