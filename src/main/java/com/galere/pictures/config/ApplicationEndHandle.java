package com.galere.pictures.config;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.galere.pictures.config.core.ReloadKeyThread;
import com.galere.pictures.services.IEncryptionService;

@Component
public class ApplicationEndHandle {
	
	private final static Logger log = LoggerFactory.getLogger(ApplicationEndHandle.class);

	public static ReloadKeyThread reloader; 
	
	@Autowired
	private IEncryptionService encryption;
	
    @PreDestroy
    public void destroy() {
        log.info("----- END OF APPLICATION DETECTED -----");
        reloader.end();
        log.info("Saving users..."); 
        encryption.loadInitialKey();
        log.info("OK");
        log.info("----- END OF APPLICATION DETECTED -----");
    }

}
