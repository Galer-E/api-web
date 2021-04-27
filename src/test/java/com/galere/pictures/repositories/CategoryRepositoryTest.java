package com.galere.pictures.repositories;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.galere.pictures.entities.Category;
import com.galere.pictures.services.impl.CategoryServiceImpl;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration
@SpringBootTest
class CategoryRepositoryTest {
	
	private final static Logger log = LoggerFactory.getLogger(CategoryRepositoryTest.class);
	
	@Mock
	private CategoryRepository repo;
	
	@InjectMocks
	private CategoryServiceImpl service;
	
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
	void findAllCategories() {
		
		log.trace("  [Lancement Test] -> findAllCategories()");
        when(repo.findAll()).thenReturn(getMock());

		List<Category> categories = service.getRepository().findAll();
		
		log.trace("  |  Roles trouvés : {}", categories.size());	
		categories.forEach(cat -> log.trace("  |     - {}", cat.toString()));
		
		assertTrue(categories != null);
		assertTrue(categories.size() == 2);
		
		log.trace("  [Fin Test]");
		log.trace("");

	}
	
	@Test
	@DisplayName("Search Categories")
	@Transactional(readOnly = true)
	void searchCategories() {
		
		log.trace("  [Lancement Test] -> searchCategories()");
        when(repo.findAll()).thenReturn(getMock());
		
		List<Category> searchLabel = service.searchByTags("Art");
		List<Category> searchId = service.searchByTags("2");
		
		assertTrue(searchLabel != null);
		assertTrue(searchLabel.size() == 1);
		
		assertTrue(searchId != null);
		assertTrue(searchId.size() == 1);
		
		log.trace("  [Fin Test]");
		log.trace("");

	}
	
	private List<Category> getMock() {
		List<Category> list = new ArrayList<Category>();
		Category c = new Category();
		c.setId(1L);
		c.setLabel("Art");
        list.add(c);
        
        c = new Category();
        c.setId(2L);
        c.setLabel("Contemporain");
        list.add(c);
        return list;
	}
	
	@AfterEach
	void end() {
		log.trace("Fin Test");
	}
	
}
