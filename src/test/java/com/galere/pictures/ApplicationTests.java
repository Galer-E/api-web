package com.galere.pictures;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

	public static final String space4 = "    ";
	public static final String space8 = "        ";
	
	private final static Logger log = LoggerFactory.getLogger(ApplicationTests.class);
	
	@Test
	void contextLoads() {
		log.trace("Lancement Tests Unitaires...");
		assertTrue(true);
	}

}
