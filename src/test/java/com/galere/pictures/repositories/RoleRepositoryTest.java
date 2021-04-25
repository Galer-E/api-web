package com.galere.pictures.repositories;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

import com.galere.pictures.entities.Category;
import com.galere.pictures.entities.Image;
import com.galere.pictures.entities.Role;
import com.galere.pictures.services.impl.RoleServiceImpl;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class RoleRepositoryTest {
	
	private final static Logger log = LoggerFactory.getLogger(RoleRepositoryTest.class);
	
	@Mock
	private RoleRepository repo;
	
	@InjectMocks
	private RoleServiceImpl service;
	
	@Before
    public void setUp() {
		MockitoAnnotations.initMocks(this);
    }
	
	@BeforeEach
	void setup() {
		log.trace("Tests : Lancement 'Role'...");
		log.trace("");
		assertNotNull(repo, "Le repository n'a pas été initialisé !");
	}
	
	@Test
	@DisplayName("Find All Roles")
	@Transactional(readOnly = true)
	void findAllRoleTypes() {
		
		log.trace("  [Lancement Test] -> findAllRoleTypes()");
        when(repo.findAll()).thenReturn(getMock());
		
		List<Role> roles = repo.findAll();
		
		log.trace("  |  Roles trouvés : {}", roles.size());	
		roles.forEach(role -> log.trace("  |     - {}", role.toString()));
		
		assertTrue(roles != null);
		assertTrue(roles.size() == 2);
		
		log.trace("  [Fin Test]");
		log.trace("");

	}
	
	@Test
	@DisplayName("Search Roles")
	@Transactional(readOnly = true)
	void searchRoles() {
		
		log.trace("  [Lancement Test] -> searchRoles()");
        when(repo.findAll()).thenReturn(getMock());
		
		List<Role> searchLabel = service.searchByTags("Gestionnaire");
		List<Role> searchId = service.searchByTags("2");
		
		assertTrue(searchLabel != null);
		assertTrue(searchLabel.size() == 1);
		
		assertTrue(searchId != null);
		assertTrue(searchId.size() == 1);
		
		log.trace("  [Fin Test]");
		log.trace("");

	}
	
	private List<Role> getMock() {
		List<Role> list = new ArrayList<Role>();
		Role r = new Role();
		r.setId(1L);
		r.setLabel("Gestionnaire");
        list.add(r);
        
        r = new Role();
        r.setId(2L);
		r.setLabel("User");
        list.add(r);
        return list;
	}
	
	@AfterEach
	void end() {
		log.trace("Fin Test");
	}
	
}
