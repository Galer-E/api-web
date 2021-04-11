package com.galere.pictures.repositories;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.galere.pictures.entities.User;

@SpringBootTest
class UserRepositoryTest {
	
	private final static Logger log = LoggerFactory.getLogger(UserRepositoryTest.class);
	
	@Autowired
	private UserRepository repo;
	
	@BeforeEach
	void setup() {
		log.trace("Tests : Lancement 'User'...");
		log.trace("");
		assertNotNull(repo, "Le repository n'a pas été initialisé !");
	}
	
	@Test
	@DisplayName("Find All Users")
	@Transactional(readOnly = true)
	void findAllUsers() {
		
		log.trace("  [Lancement Test] -> findAllUsers()");
		
		List<User> list = repo.findAll();
		
		log.trace("  |  Utilisateurs trouvés : {}", list.size());	
		list.forEach(user -> log.trace("  |   - {}", user.toString()));
		
		assertTrue(list != null);
		
		log.trace("  [Fin Test]");
		log.trace("");

	}
	
	@AfterEach
	void end() {
		log.trace("Fin Test");
	}
	
}
