package com.galere.pictures.config;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.galere.pictures.config.core.ReloadKeyThread;
import com.galere.pictures.services.IEncryptionService;

/**
 * <b>
 * 	Class utilisé lors de l'arrêt de l'application.
 * </b>
 * <p>
 * 	Cette class est directement en lien avec le thread ReloadKeyThread. Lorsque sa méthode destroy() est appelée,
 * 	elle utilisera l'instance static de ReloadKeyThread pour arrêter la mise à jour de la clé de chiffrement.
 * </p>
 * <p>
 * 	Une fois l'instance du thread arrêté, la méthode destroy() appelera la fonction loadInitialKey() du service
 * 	IEncryptionService (son implémentation). Elle permet de rechiffrer les mots de passes avec la clé de
 * 	chiffrement initiale.
 * </p>
 * <p>
 *	Le début et la fin de ces opérations sont marqués par les logs dans la console.
 * </p>
 * 
 * @see ReloadKeyThread
 * @see ReloadKeyThread#end()
 * @see ApplicationEndHandle#destroy()
 * @see IEncryptionService
 * @see IEncryptionService#loadInitialKey()
 * 
 * @author Ilias HATTANE
 * @version 1.0
 *
 */
@Component
public class ApplicationEndHandle {
	
	/**
	 * <b> Logger de la class ApplicationEndHandle. </b>
	 */
	private final static Logger log = LoggerFactory.getLogger(ApplicationEndHandle.class);

	/**
	 * <b> Instance du thread ReloadKeyThread, permet de constroller la mise à jour de la clé de chiffrement. </b>
	 * 
	 * @see ReloadKeyThread
	 */
	public static ReloadKeyThread reloader; 
	
	/**
	 * <b> Implémentation du service IEncryptionService. </b>
	 * 
	 * @see IEncryptionService
	 */
	@Autowired
	private IEncryptionService encryption;
	
	/**
	 * <b> Appelé lorsque l'application se prépare à s'arrêter. </b>
	 * 
	 * <p> 
	 * 	Commence par arrêter le thread responsable de la mise à jour de la clé de chiffrement, puis
	 * 	utilise la méthode loadInitialKey() du service IEncryptionService pour rechiffrer les mots de passes
	 *	avec la clé de chiffrement initiale.
	 * </p>
	 * 
	 * @see ApplicationEndHandle#reloader
	 * @see IEncryptionService#loadInitialKey()
	 */
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
