package com.galere.pictures.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.galere.pictures.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	List<User> findAllByLoginContains(String loginContains);
	
}
