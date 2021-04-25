package com.galere.pictures.services;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.galere.pictures.entities.Category;

@ContextConfiguration
@SpringBootTest
public class EncryptionServiceTest {
	
	private final static Logger log = LoggerFactory.getLogger(EncryptionServiceTest.class);
	
	@Autowired
	private IEncryptionService encryption;
	
	@BeforeEach
	void setup() {
		log.trace("Tests : Lancement 'Encryption'...");
		log.trace("");
		assertNotNull(encryption, "Le service n'a pas été initialisé !");
	}
	
	@Test
	@DisplayName("Encrypt and decrypt string")
	@Transactional(readOnly = true)
	void encryptAndDecrypt() {
		
		log.trace("  [Lancement Test] -> encryptAndDecrypt()");
		
		String origin = "original";
		
		try {
			assertTrue(origin.equalsIgnoreCase(encryption.decrypt(encryption.encrypt(origin))));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		log.trace("  [Fin Test]");
		log.trace("");

	}
	
	@AfterEach
	void end() {
		log.trace("Fin Test");
	}
	
}
