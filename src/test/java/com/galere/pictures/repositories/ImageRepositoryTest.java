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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.galere.pictures.entities.Category;
import com.galere.pictures.entities.Image;
import com.galere.pictures.services.impl.ImageServiceImpl;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration
@SpringBootTest
class ImageRepositoryTest {
	
	private final static Logger log = LoggerFactory.getLogger(ImageRepositoryTest.class);
	
	@Mock
	private ImageRepository repo;
	
	@InjectMocks
	private ImageServiceImpl service;
	
	@Before
    public void setUp() {
		MockitoAnnotations.initMocks(this);
    }
	
	@BeforeEach
	void setup() {
		log.trace("Tests : Lancement 'Image'...");
		log.trace("");
		assertNotNull(repo, "Le repository n'a pas été initialisé !");
	}
	
	@Test
	@DisplayName("Find All Images")
	@Transactional(readOnly = true)
	void findAllImages() {
		
		log.trace("  [Lancement Test] -> findAllImages()");
        when(repo.findAll()).thenReturn(getMock());
		
		List<Image> images = service.getRepository().findAll();
		
		log.trace("  |  Images trouvés : {}", images.size());	
		images.forEach(img -> log.trace("  |     - {}", img.toString()));
		
		assertTrue(images != null);
		assertTrue(images.size() == 2);
		
		log.trace("  [Fin Test]");
		log.trace("");

	}
	
	@Test
	@DisplayName("Search Images")
	@Transactional(readOnly = true)
	void searchImages() {
		
		log.trace("  [Lancement Test] -> searchImages()");
        when(repo.findAll()).thenReturn(getMock());
		
		List<Image> searchCar = service.searchByTags("car");
		List<Image> searchArt = service.searchByTags("art");
		
		assertTrue(searchCar != null);
		assertTrue(searchCar.size() == 1);
		
		assertTrue(searchArt != null);
		assertTrue(searchArt.size() == 2);
		
		log.trace("  [Fin Test]");
		log.trace("");

	}
	
	private List<Image> getMock() {
		List<Image> list = new ArrayList<Image>();
		Image i = new Image();
		i.setId(1L);
		i.setDate(LocalDate.now());
		i.setContent("car - human");
		i.setDescription("First testing image");
		i.setTags("art moderne");
		i.setTitle("Autoportrait d'une voiture");
		i.setUrl("C:/testFolder/image.png");
        list.add(i);
        
        i = new Image();
		i.setId(2L);
		i.setDate(LocalDate.now());
		i.setContent("plane - air");
		i.setDescription("Second testing image");
		i.setTags("technlogie art");
		i.setTitle("Machine d'avenir");
		i.setUrl("C:/testFolder/image.png");
        list.add(i);
        
        return list;
	}
	
	@AfterEach
	void end() {
		log.trace("Fin Test");
	}
	
}
