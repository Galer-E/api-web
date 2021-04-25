package com.galere.pictures.repositories;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.galere.pictures.entities.Role;
import com.galere.pictures.entities.User;
import com.galere.pictures.services.impl.UserServiceImpl;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class UserRepositoryTest {
	
	private final static Logger log = LoggerFactory.getLogger(UserRepositoryTest.class);
	
	@Mock
	private UserRepository repo;
	
	@InjectMocks
	private UserServiceImpl service;
	
	@Before
    public void setUp() {
		MockitoAnnotations.initMocks(this);
    }
	
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
        when(repo.findAll()).thenReturn(getMock());

		List<User> users = service.getRepository().findAll();
		
		log.trace("  |  Utilisateurs trouvés : {}", users.size());	
		users.forEach(user -> log.trace("  |   - {}", user.toString()));
		
		assertTrue(users != null);
		assertTrue(users.size() == 2);
		
		log.trace("  [Fin Test]");
		log.trace("");

	}
	
	@Test
	@DisplayName("Search Users")
	@Transactional(readOnly = true)
	void searchUsers() {
		
		log.trace("  [Lancement Test] -> searchUsers()");
        when(repo.findAll()).thenReturn(getMock());
		
		List<User> searchLabel = service.searchByTags("ilias");
		List<User> searchId = service.searchByTags("2");
		
		assertTrue(searchLabel != null);
		assertTrue(searchLabel.size() == 2);
		
		assertTrue(searchId != null);
		assertTrue(searchId.size() == 1);
		
		log.trace("  [Fin Test]");
		log.trace("");

	}
	
	private List<User> getMock() {
		Role r = new Role();
		r.setId(1L);
		r.setLabel("Gestionnaire");
		r.setLevel((short)1);
		
		Set<Role> roles = new HashSet<Role>();
		roles.add(r);
		
		List<User> list = new ArrayList<User>();
		User u = new User();
		u.setEntry(LocalDate.now());
		u.setId(1L);
		u.setLogin("ilias.hattane");
		u.setPass("1234");
		u.setRoles(roles);
		list.add(u);
		
		u = new User();
		u.setEntry(LocalDate.now());
		u.setId(2L);
		u.setLogin("ilias.fathis");
		u.setPass("1234");
		u.setRoles(roles);
		list.add(u);
		return list;
	}
	
	@AfterEach
	void end() {
		log.trace("Fin Test");
	}
	
}
