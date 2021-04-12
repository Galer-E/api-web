package com.galere.pictures.config.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galere.pictures.services.IEncryptionService;

public class ReloadKeyThread extends Thread {
	
	private final static Logger log = LoggerFactory.getLogger(ReloadKeyThread.class);
	
	private boolean continu = true;
	
	private final long SKIP = 1000 * 60 * 60;
	
	private IEncryptionService encryption;
	
	public ReloadKeyThread(IEncryptionService service) {
		encryption = service;
	}
	
	@Override
	public void run() {
		try {
			while (continu) {
				log.info("START RELOADING KEY");
				encryption.reloadKey();
				log.info("END OF KEY RELOADING");
				for (int i = 0; i < SKIP/1000 && continu; i++)
					sleep(1000); 
			}
		} catch (Exception e) {e.printStackTrace();}
		log.info("Thread Ended");
	}
	
	public void end() {
		log.info("End of reloadKeyThread...");
		continu = false;
		this.stop();
		log.info("OK");
	}
	
}
